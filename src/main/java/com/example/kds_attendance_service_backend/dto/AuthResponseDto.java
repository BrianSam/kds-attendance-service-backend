package com.example.kds_attendance_service_backend.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AuthResponseDto {
    private String token;
    private String name;
    private String role;
    private Long employeeId;
}
