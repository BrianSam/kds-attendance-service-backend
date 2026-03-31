package com.example.kds_attendance_service_backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class ErrorResponseDto {

    private String status;
    private String message;
    private LocalDateTime timestamp;
}
