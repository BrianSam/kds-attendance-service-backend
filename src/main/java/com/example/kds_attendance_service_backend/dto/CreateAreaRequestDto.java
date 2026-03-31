package com.example.kds_attendance_service_backend.dto;

import jakarta.persistence.Column;
import lombok.Builder;
import lombok.Data;

@Data
public class CreateAreaRequestDto {

    private String name;

    private Double latitude;


    private Double longitude;



}
