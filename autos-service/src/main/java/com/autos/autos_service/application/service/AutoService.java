package com.autos.autos_service.application.service;

import com.autos.autos_service.domain.model.Auto;
import com.autos.autos_service.domain.port.in.ManageAutosUseCase;
import com.autos.autos_service.domain.port.out.AutoRepositoryPort;
import org.springframework.stereotype.Service;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class AutoService implements ManageAutosUseCase {

    private final AutoRepositoryPort autoRepositoryPort;

    public AutoService(AutoRepositoryPort autoRepositoryPort) {
        this.autoRepositoryPort = autoRepositoryPort;
    }

    @Override
    public Auto createAuto(Auto auto, String userId) {
        auto.setUserId(userId);
        if (auto.getPhoto() == null || auto.getPhoto().isEmpty()) {
            auto.setPhoto("simulated_photo_url.jpg");
        }
        if (autoRepositoryPort.existsByPlate(auto.getPlate())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "La placa ya está registrada");
        }
        return autoRepositoryPort.save(auto);
    }

    @Override
    public Auto getAutoById(Long id, String userId) {
        return autoRepositoryPort.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new RuntimeException("Auto no encontrado o no te pertenece"));
    }

    @Override
    public List<Auto> getAllAutos(String userId) {
        return autoRepositoryPort.findByUserId(userId);
    }

    @Override
    public Auto updateAuto(Long id, Auto updatedData, String userId) {
        Auto existingAuto = getAutoById(id, userId);
        
        existingAuto.setPlate(updatedData.getPlate());
        existingAuto.setBrand(updatedData.getBrand());
        existingAuto.setModel(updatedData.getModel());
        existingAuto.setYear(updatedData.getYear());
        existingAuto.setColor(updatedData.getColor());
        existingAuto.setPhoto(updatedData.getPhoto());
        
        return autoRepositoryPort.save(existingAuto);
    }

    @Override
    public void deleteAuto(Long id, String userId) {
        getAutoById(id, userId);
        autoRepositoryPort.deleteById(id);
    }

    @Override
    public List<Auto> searchAutos(String userId, String query) {
        return autoRepositoryPort.searchAutos(userId, query);
    }

    @Override
    public List<Auto> filterByYear(String userId, Integer year) {
        return autoRepositoryPort.filterByYear(userId, year);
    }

    @Override
    public List<Auto> filterByBrand(String userId, String brand) {
        return autoRepositoryPort.filterByBrand(userId, brand);
    }
}