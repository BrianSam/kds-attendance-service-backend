package com.example.kds_attendance_service_backend.dto;

import lombok.Data;

@Data
public class AttendanceRequestDto {
    private Double latitude;
    private Double longitude;
    private String photoUrl;
}
