package com.example.kds_attendance_service_backend.service;

import com.example.kds_attendance_service_backend.dto.CreateEmployeeDto;
import com.example.kds_attendance_service_backend.exception.BadRequestException;
import com.example.kds_attendance_service_backend.exception.ResourceNotFoundException;
import com.example.kds_attendance_service_backend.model.Area;
import com.example.kds_attendance_service_backend.model.Employee;
import com.example.kds_attendance_service_backend.model.Role;
import com.example.kds_attendance_service_backend.model.Status;
import com.example.kds_attendance_service_backend.repository.AreaRepository;
import com.example.kds_attendance_service_backend.repository.EmployeeRepository;
import com.example.kds_attendance_service_backend.util.AuthContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Random;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmployeeServiceImpl implements EmployeeService{
    private final EmployeeRepository employeeRepository;
    private final AreaRepository areaRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    @Override
    public String createEmployee(CreateEmployeeDto employeeDto) {

        if(employeeRepository.findByPhoneNumber(employeeDto.getPhoneNumber()).isPresent()){
            throw new BadRequestException("Employee already exists with this phone number");
        }

         Area area = areaRepository.findById(employeeDto.getAreaId()).orElseThrow(
                () ->  new ResourceNotFoundException("Area not found")
        );




        String rawPassword = generatePassword();
        String encodePassword = bCryptPasswordEncoder.encode(rawPassword);

        Employee employee = Employee.builder()
                .name(employeeDto.getName())
                .phoneNumber(employeeDto.getPhoneNumber()).
                password(encodePassword)
                .role(Role.EMPLOYEE)
                .status(Status.ACTIVE)
                .area(area)
                .build();

        try {
            employeeRepository.save(employee);
            log.info("employee with mobile no : {} and name {} created !",employee.getPhoneNumber(),employee.getName() );
        } catch (Exception e) {
            throw new RuntimeException(e);
        }


        return rawPassword;
    }

    private String generatePassword() {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder password = new StringBuilder();
        Random random = new Random();

        for (int i = 0; i < 8; i++) {
            password.append(chars.charAt(random.nextInt(chars.length())));
        }

        return password.toString();
    }
}
