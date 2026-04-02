package com.example.kds_attendance_service_backend.dto;

import lombok.Data;

import java.time.LocalDate;


    @Data
    public class ExitEmployeeRequestDto {
        private LocalDate exitDate;
    }

