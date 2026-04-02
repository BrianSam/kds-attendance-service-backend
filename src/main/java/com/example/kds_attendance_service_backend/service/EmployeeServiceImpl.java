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
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
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
        LocalDate joiningDate = employeeDto.getJoiningDate() != null
                ? employeeDto.getJoiningDate()
                : LocalDate.now();

        Employee employee = Employee.builder()
                .name(employeeDto.getName())
                .phoneNumber(employeeDto.getPhoneNumber()).
                password(encodePassword)
                .role(Role.EMPLOYEE)
                .status(Status.ACTIVE)
                .area(area)
                .joiningDate(joiningDate)
                .build();

        try {
            employeeRepository.save(employee);
            log.info("employee with mobile no : {} and name {} created !",employee.getPhoneNumber(),employee.getName() );
        } catch (Exception e) {
            throw new RuntimeException(e);
        }


        return rawPassword;
    }

    @Override
    @Transactional
    public void markEmployeeExit(Long employeeId, LocalDate exitDate) {
        Employee emp = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found"));

        // Validation 1: exitDate null
        if (exitDate == null) {
            throw new BadRequestException("Exit date is required");
        }

        // Validation 2: before joining
        if (exitDate.isBefore(emp.getJoiningDate())) {
            throw new BadRequestException("Exit date cannot be before joining date");
        }

        // Validation 3: already exited
        if (emp.getExitDate() != null) {
            throw new BadRequestException("Employee already has an exit date");
        }

        // Validation 4: future date (optional rule)
        if (exitDate.isAfter(LocalDate.now())) {
            throw new BadRequestException("Exit date cannot be in future");
        }

        emp.setExitDate(exitDate);

        employeeRepository.save(emp);
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
