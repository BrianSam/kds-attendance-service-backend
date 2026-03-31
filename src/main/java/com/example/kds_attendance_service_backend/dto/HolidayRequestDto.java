package com.example.kds_attendance_service_backend.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class HolidayRequestDto {

    private LocalDate date;
    private String name;
    private Long areaId; // optional
}
