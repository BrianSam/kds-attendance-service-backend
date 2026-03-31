package com.example.kds_attendance_service_backend.service;

import com.example.kds_attendance_service_backend.dto.HolidayRequestDto;

public interface HolidayService {
    void createHoliday(HolidayRequestDto requestDto);
}
