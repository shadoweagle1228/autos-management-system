package com.autos.user_auth_service.application.service;

import com.autos.user_auth_service.domain.model.User;
import com.autos.user_auth_service.domain.port.in.RegisterUserUseCase;
import com.autos.user_auth_service.domain.port.out.UserRepositoryPort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService implements RegisterUserUseCase {

    private final UserRepositoryPort userRepositoryPort;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepositoryPort userRepositoryPort, PasswordEncoder passwordEncoder) {
        this.userRepositoryPort = userRepositoryPort;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public User register(User user) {
        if (userRepositoryPort.existsByUsername(user.getUsername())) {
            throw new IllegalArgumentException("El nombre de usuario ya está registrado.");
        }
        
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        
        if (user.getRole() == null || user.getRole().isEmpty()) {
            user.setRole("ROLE_USER");
        }

        return userRepositoryPort.save(user);
    }
}