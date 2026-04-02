package com.example.kds_attendance_service_backend.controller;

import com.example.kds_attendance_service_backend.dto.AttendanceRequestDto;
import com.example.kds_attendance_service_backend.dto.DailyAttendanceResponseDto;
import com.example.kds_attendance_service_backend.dto.PageResponse;
import com.example.kds_attendance_service_backend.service.AttendanceService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

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

    @GetMapping("/report/daily")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PageResponse<DailyAttendanceResponseDto>> getDailyReport(
            @RequestParam Long areaId,
            @RequestParam String date,
            @RequestParam int page,
            @RequestParam int size
    ) {
        LocalDate localDate = LocalDate.parse(date);

      Page<DailyAttendanceResponseDto>pageResult = attendanceService.getDailyReport(areaId,localDate,page,size);

      PageResponse<DailyAttendanceResponseDto> response = new PageResponse<>(
              pageResult.getContent(),
              pageResult.getNumber(),
              pageResult.getSize(),
              pageResult.getTotalElements(),
              pageResult.getTotalPages()
      );
      return ResponseEntity.ok(response);

    }
}
