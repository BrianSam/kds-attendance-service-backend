package com.example.kds_attendance_service_backend.controller;

import com.example.kds_attendance_service_backend.dto.AuthResponseDto;
import com.example.kds_attendance_service_backend.dto.LoginRequestDto;
import com.example.kds_attendance_service_backend.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDto>login(@RequestBody LoginRequestDto requestDto){
        return ResponseEntity.ok(authService.login(requestDto));
    }
}
