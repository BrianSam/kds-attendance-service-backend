package com.example.kds_attendance_service_backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DailyAttendanceResponseDto {
    private String employeeName;
    private String status;
    private String photoUrl;
}
