package com.example.kds_attendance_service_backend.dto;

import jakarta.persistence.Column;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CreateAreaResponseDto {
    private Long id;
    private String name;


    private Double latitude;


    private Double longitude;


    private String qrCode;
}
