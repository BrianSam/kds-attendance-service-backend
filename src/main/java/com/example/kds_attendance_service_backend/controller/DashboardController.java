package com.example.kds_attendance_service_backend.controller;

import com.example.kds_attendance_service_backend.dto.DashBoardAreaResponseDto;
import com.example.kds_attendance_service_backend.service.AreaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/dashboard")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
public class DashboardController {

    private final AreaService areaService;

    @GetMapping("/getAreaList")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<DashBoardAreaResponseDto>> getAreaList(){
        List<DashBoardAreaResponseDto> responseDtoList= areaService.getAreaList();

        return ResponseEntity.ok(responseDtoList);
    }
}
