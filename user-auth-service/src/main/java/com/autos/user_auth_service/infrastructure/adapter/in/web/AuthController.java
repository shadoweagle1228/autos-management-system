package com.autos.user_auth_service.infrastructure.adapter.in.web;

import com.autos.user_auth_service.domain.model.User;
import com.autos.user_auth_service.domain.port.in.RegisterUserUseCase;
import com.autos.user_auth_service.domain.port.out.UserRepositoryPort;
import com.autos.user_auth_service.infrastructure.adapter.in.web.dto.AuthResponse;
import com.autos.user_auth_service.infrastructure.adapter.in.web.dto.LoginRequest;
import com.autos.user_auth_service.infrastructure.adapter.in.web.dto.RegisterRequest;
import com.autos.user_auth_service.infrastructure.config.JwtService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final RegisterUserUseCase registerUserUseCase;
    private final UserRepositoryPort userRepositoryPort;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthController(RegisterUserUseCase registerUserUseCase, 
                          UserRepositoryPort userRepositoryPort, 
                          PasswordEncoder passwordEncoder, 
                          JwtService jwtService) {
        this.registerUserUseCase = registerUserUseCase;
        this.userRepositoryPort = userRepositoryPort;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest request) {
        try {
            // Mapeamos el DTO al modelo de Dominio
            User userToRegister = new User(null, request.getUsername(), request.getPassword(), "ROLE_USER");
            
            // Ejecutamos el caso de uso
            User registeredUser = registerUserUseCase.register(userToRegister);
            
            // Generamos el token automáticamente para que el usuario inicie sesión al registrarse
            String token = jwtService.generateToken(registeredUser.getUsername(), registeredUser.getRole());
            
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new AuthResponse(token, registeredUser.getUsername(), "Usuario registrado exitosamente"));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new AuthResponse(null, null, e.getMessage()));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        // Buscamos al usuario en la base de datos a través del puerto
        Optional<User> userOptional = userRepositoryPort.findByUsername(request.getUsername());

        if (userOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new AuthResponse(null, null, "Credenciales inválidas"));
        }

        User user = userOptional.get();

        // Verificamos que la contraseña plana coincida con el hash de la BD
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new AuthResponse(null, null, "Credenciales inválidas"));
        }

        // Generamos el token JWT
        String token = jwtService.generateToken(user.getUsername(), user.getRole());

        return ResponseEntity.ok(new AuthResponse(token, user.getUsername(), "Login exitoso"));
    }
}