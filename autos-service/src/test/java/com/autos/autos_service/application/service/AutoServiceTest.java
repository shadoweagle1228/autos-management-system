package com.autos.autos_service.application.service;

import com.autos.autos_service.domain.model.Auto;
import com.autos.autos_service.domain.port.out.AutoRepositoryPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AutoServiceTest {

    // Mockeamos el puerto de salida
    @Mock
    private AutoRepositoryPort autoRepositoryPort;

    // Inyectamos los mocks en el servicio real que vamos a probar
    @InjectMocks
    private AutoService autoService;

    private Auto sampleAuto;
    private final String USER_ID = "test_user";

    // 3. Preparamos datos de prueba antes de cada test
    @BeforeEach
    void setUp() {
        sampleAuto = Auto.builder()
                .id(1L)
                .plate("XYZ-123")
                .brand("Toyota")
                .model("Corolla")
                .year(2024)
                .color("Rojo")
                .photo("")
                .userId(USER_ID)
                .build();
    }

    // --- TEST 1: Crear Auto ---
    @Test
    void createAuto_ShouldAssignUserAndDefaultPhoto() {
        // Arrange: Le decimos al mock qué responder
        when(autoRepositoryPort.save(any(Auto.class))).thenReturn(sampleAuto);

        // Act: Ejecutamos el método real
        Auto createdAuto = autoService.createAuto(sampleAuto, USER_ID);

        // Assert: Verificamos que la lógica de negocio funcionó
        assertNotNull(createdAuto);
        assertEquals(USER_ID, createdAuto.getUserId()); // Validamos que se asignó el usuario
        assertEquals("simulated_photo_url.jpg", sampleAuto.getPhoto()); // Validamos la foto por defecto
        verify(autoRepositoryPort, times(1)).save(sampleAuto); // Validamos que se llamó al repositorio
    }

    // --- TEST 2: Buscar Auto Exitoso ---
    @Test
    void getAutoById_ShouldReturnAuto_WhenExistsAndBelongsToUser() {
        // Arrange
        when(autoRepositoryPort.findByIdAndUserId(1L, USER_ID)).thenReturn(Optional.of(sampleAuto));

        // Act
        Auto foundAuto = autoService.getAutoById(1L, USER_ID);

        // Assert
        assertNotNull(foundAuto);
        assertEquals("XYZ-123", foundAuto.getPlate());
        verify(autoRepositoryPort, times(1)).findByIdAndUserId(1L, USER_ID);
    }

    // --- TEST 3: Buscar Auto No Existente o de Otro Usuario ---
    @Test
    void getAutoById_ShouldThrowException_WhenNotFoundOrNotBelongsToUser() {
        // Arrange
        when(autoRepositoryPort.findByIdAndUserId(1L, USER_ID)).thenReturn(Optional.empty());

        // Act & Assert: Verificamos que lance la excepción correcta
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            autoService.getAutoById(1L, USER_ID);
        });
        
        assertEquals("Auto no encontrado o no te pertenece", exception.getMessage());
        verify(autoRepositoryPort, times(1)).findByIdAndUserId(1L, USER_ID);
    }

    // --- TEST 4: Listar todos los Autos ---
    @Test
    void getAllAutos_ShouldReturnUserAutos() {
        // Arrange
        when(autoRepositoryPort.findByUserId(USER_ID)).thenReturn(List.of(sampleAuto));

        // Act
        List<Auto> autos = autoService.getAllAutos(USER_ID);

        // Assert
        assertFalse(autos.isEmpty());
        assertEquals(1, autos.size());
        verify(autoRepositoryPort, times(1)).findByUserId(USER_ID);
    }
}