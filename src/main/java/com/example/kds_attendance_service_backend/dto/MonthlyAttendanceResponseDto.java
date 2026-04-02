package com.example.kds_attendance_service_backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MonthlyAttendanceResponseDto {

    private String employeeName;
    private int presentDays;
    private int absentDays;
    private int lateDays;
    private int holidayDays;
    private int workingDays;
}
