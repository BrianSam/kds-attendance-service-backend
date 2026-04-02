package com.example.kds_attendance_service_backend.service;

import com.example.kds_attendance_service_backend.dto.AttendanceRequestDto;
import com.example.kds_attendance_service_backend.dto.DailyAttendanceResponseDto;
import org.springframework.data.domain.Page;

import java.time.LocalDate;

public interface AttendanceService {
    void markAttendance(AttendanceRequestDto request);
    Page<DailyAttendanceResponseDto> getDailyReport(
            Long areaId,
            LocalDate date,
            int page,
            int size
    );

}
