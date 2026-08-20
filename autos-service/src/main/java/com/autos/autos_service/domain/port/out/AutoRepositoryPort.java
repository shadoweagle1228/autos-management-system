package com.autos.autos_service.domain.port.out;

import com.autos.autos_service.domain.model.Auto;
import java.util.List;
import java.util.Optional;

public interface AutoRepositoryPort {
    Auto save(Auto auto);
    Optional<Auto> findById(Long id, String userId);
    Optional<Auto> findByIdAndUserId(Long id, String userId);
    Optional<Auto> findByPlate(String plate);
    List<Auto> findByUserId(String userId);
    void deleteById(Long id);
    List<Auto> searchAutos(String userId, String query);
    List<Auto> filterByYear(String userId, Integer year);
    List<Auto> filterByBrand(String userId, String brand);
    boolean existsByPlate(String plate);
}