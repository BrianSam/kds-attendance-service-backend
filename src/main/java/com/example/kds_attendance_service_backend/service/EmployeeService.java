package com.example.kds_attendance_service_backend.service;

import com.example.kds_attendance_service_backend.dto.CreateEmployeeDto;
import com.example.kds_attendance_service_backend.model.Employee;

import java.time.LocalDate;

public interface EmployeeService {
    String createEmployee(CreateEmployeeDto employeeDto);
    void markEmployeeExit(Long employeeId, LocalDate exitDate);
}
