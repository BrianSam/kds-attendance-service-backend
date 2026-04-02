package com.example.kds_attendance_service_backend.dto;

import com.example.kds_attendance_service_backend.model.Employee;
import lombok.Data;

import java.time.LocalDate;

@Data
public class CreateEmployeeDto {
    private String name;
    private String phoneNumber;
    private Long areaId;
    private LocalDate joiningDate;

    public Employee dtoToEmployee(CreateEmployeeDto dto){
        Employee employee = Employee.builder().
                name(dto.getName())
                .phoneNumber(dto.phoneNumber)
                .build();
        return employee;

    }

}
