package com.example.kds_attendance_service_backend.service;

import com.example.kds_attendance_service_backend.dto.CreateEmployeeDto;
import com.example.kds_attendance_service_backend.model.Employee;

public interface EmployeeService {
    String createEmployee(CreateEmployeeDto employeeDto);
}
