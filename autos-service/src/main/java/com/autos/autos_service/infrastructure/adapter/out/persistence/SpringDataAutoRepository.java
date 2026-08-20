package com.autos.autos_service.infrastructure.adapter.out.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface SpringDataAutoRepository extends JpaRepository<AutoEntity, Long> {
    Optional<AutoEntity> findByPlate(String plate);
    List<AutoEntity> findByUserId(String userId);
    Optional<AutoEntity> findByIdAndUserId(Long id, String userId);
    @Query("SELECT a FROM AutoEntity a WHERE a.userId = :userId AND (LOWER(a.plate) LIKE LOWER(CONCAT('%', :query, '%')) OR LOWER(a.model) LIKE LOWER(CONCAT('%', :query, '%')))")
    List<AutoEntity> searchByPlateOrModel(@Param("userId") String userId, @Param("query") String query);
    List<AutoEntity> findByUserIdAndYear(String userId, Integer year);
    List<AutoEntity> findByUserIdAndBrandIgnoreCase(String userId, String brand);
    boolean existsByPlate(String plate);
}