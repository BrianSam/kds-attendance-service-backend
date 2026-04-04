package com.example.kds_attendance_service_backend.controller;

import com.example.kds_attendance_service_backend.dto.HolidayRequestDto;
import com.example.kds_attendance_service_backend.service.HolidayService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/holidays")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")

public class HolidayController {

    private final HolidayService holidayService;

    @PostMapping("/createHoliday")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> createHoliday(
            @RequestBody HolidayRequestDto request
    ) {
        holidayService.createHoliday(request);
        return ResponseEntity.ok("Holiday created successfully");
    }
}