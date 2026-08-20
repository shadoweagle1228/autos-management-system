package com.autos.autos_service.domain.port.in;

import com.autos.autos_service.domain.model.Auto;
import java.util.List;

public interface ManageAutosUseCase {
    Auto createAuto(Auto auto, String userId);
    Auto getAutoById(Long id, String userId);
    List<Auto> getAllAutos(String userId);
    Auto updateAuto(Long id, Auto auto, String userId);
    void deleteAuto(Long id, String userId);
    List<Auto> searchAutos(String userId, String query);
    List<Auto> filterByYear(String userId, Integer year);
    List<Auto> filterByBrand(String userId, String brand);
}