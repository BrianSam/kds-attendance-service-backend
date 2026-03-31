package com.example.kds_attendance_service_backend.service;

import com.example.kds_attendance_service_backend.dto.AuthResponseDto;
import com.example.kds_attendance_service_backend.dto.LoginRequestDto;

public interface AuthService {
    AuthResponseDto login(LoginRequestDto requestDto);
}
