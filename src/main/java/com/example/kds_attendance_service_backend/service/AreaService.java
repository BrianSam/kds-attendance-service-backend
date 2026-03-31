package com.example.kds_attendance_service_backend.service;

import com.example.kds_attendance_service_backend.dto.CreateAreaRequestDto;
import com.example.kds_attendance_service_backend.model.Area;

public interface AreaService {

    Long createArea(CreateAreaRequestDto createAreaRequestDto);
}
