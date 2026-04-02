package com.example.kds_attendance_service_backend.service;

import com.example.kds_attendance_service_backend.dto.AttendanceRequestDto;
import com.example.kds_attendance_service_backend.dto.DailyAttendanceResponseDto;
import com.example.kds_attendance_service_backend.dto.MonthlyAttendanceResponseDto;
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

    Page<MonthlyAttendanceResponseDto> getMonthlyReport(
            Long areaId,
            int year,
            int month,
            int page,
            int size
    );

}
