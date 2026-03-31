package com.example.kds_attendance_service_backend.service;

import com.example.kds_attendance_service_backend.dto.AttendanceRequestDto;

public interface AttendanceService {
    void markAttendance(AttendanceRequestDto request);

}
