package com.example.kds_attendance_service_backend.dto;

import lombok.Data;

@Data
public class LoginRequestDto {
    private String phoneNumber;
    private String password;
}
