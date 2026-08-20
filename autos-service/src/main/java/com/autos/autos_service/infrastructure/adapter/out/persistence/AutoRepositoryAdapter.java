package com.autos.autos_service.infrastructure.adapter.out.persistence;

import com.autos.autos_service.domain.model.Auto;
import com.autos.autos_service.domain.port.out.AutoRepositoryPort;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class AutoRepositoryAdapter implements AutoRepositoryPort {

    private final SpringDataAutoRepository repository;

    public AutoRepositoryAdapter(SpringDataAutoRepository repository) {
        this.repository = repository;
    }

    @Override
    public Auto save(Auto auto) {
        AutoEntity entity = toEntity(auto);
        AutoEntity savedEntity = repository.save(entity);
        return toDomain(savedEntity);
    }

    @Override
    public Optional<Auto> findById(Long id, String userId) {
        return repository.findByIdAndUserId(id, userId).map(this::toDomain);
    }

    @Override
    public Optional<Auto> findByIdAndUserId(Long id, String userId) {
        return repository.findByIdAndUserId(id, userId).map(this::toDomain);
    }

    @Override
    public Optional<Auto> findByPlate(String plate) {
        return repository.findByPlate(plate).map(this::toDomain);
    }

    @Override
    public List<Auto> findByUserId(String userId) {
        return repository.findByUserId(userId).stream()
                .map(this::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteById(Long id) {
        repository.deleteById(id);
    }

    @Override
    public List<Auto> searchAutos(String userId, String query) {
        return repository.searchByPlateOrModel(userId, query).stream()
                .map(this::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Auto> filterByYear(String userId, Integer year) {
        return repository.findByUserIdAndYear(userId, year).stream()
                .map(this::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Auto> filterByBrand(String userId, String brand) {
        return repository.findByUserIdAndBrandIgnoreCase(userId, brand).stream()
                .map(this::toDomain)
                .collect(Collectors.toList());
    }

    @Override
        public boolean existsByPlate(String plate) {
        return repository.existsByPlate(plate);
    }

    // --- MAPEOS ---
    private AutoEntity toEntity(Auto auto) {
        AutoEntity entity = new AutoEntity();
        entity.setId(auto.getId());
        entity.setPlate(auto.getPlate());
        entity.setBrand(auto.getBrand());
        entity.setModel(auto.getModel());
        entity.setYear(auto.getYear());
        entity.setColor(auto.getColor());
        entity.setPhoto(auto.getPhoto());
        entity.setUserId(auto.getUserId());
        return entity;
    }

    private Auto toDomain(AutoEntity entity) {
        return Auto.builder()
                .id(entity.getId())
                .plate(entity.getPlate())
                .brand(entity.getBrand())
                .model(entity.getModel())
                .year(entity.getYear())
                .color(entity.getColor())
                .photo(entity.getPhoto())
                .userId(entity.getUserId())
                .build();
    }
}