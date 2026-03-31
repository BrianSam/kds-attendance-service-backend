package com.example.kds_attendance_service_backend.dto;

import com.example.kds_attendance_service_backend.service.AttendanceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/attendance")

@RequiredArgsConstructor
public class AttendanceController {

    private final AttendanceService attendanceService;


    @PreAuthorize("hasRole('EMPLOYEE')")
    @PostMapping("/mark")
    public ResponseEntity<String> markAttendance(@RequestBody AttendanceRequestDto requestDto){
        attendanceService.markAttendance(requestDto);
        return ResponseEntity.ok("attendance marked successfully");
    }
}
