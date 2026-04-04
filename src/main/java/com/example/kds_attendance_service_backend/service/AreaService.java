package com.example.kds_attendance_service_backend.service;

import com.example.kds_attendance_service_backend.dto.CreateAreaRequestDto;
import com.example.kds_attendance_service_backend.dto.DashBoardAreaResponseDto;
import com.example.kds_attendance_service_backend.model.Area;

import java.util.List;

public interface AreaService {

    Long createArea(CreateAreaRequestDto createAreaRequestDto);

    List<DashBoardAreaResponseDto>getAreaList();
}
