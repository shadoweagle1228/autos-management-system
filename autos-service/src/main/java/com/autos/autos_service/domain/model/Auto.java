package com.autos.autos_service.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Auto {
    private Long id;
    private String plate;
    private String brand;
    private String model;
    private Integer year;
    private String color;
    private String photo;
    private String userId;
    private String status;
}