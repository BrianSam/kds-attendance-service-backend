package com.example.kds_attendance_service_backend.service;

import com.example.kds_attendance_service_backend.dto.AuthResponseDto;
import com.example.kds_attendance_service_backend.dto.LoginRequestDto;
import com.example.kds_attendance_service_backend.exception.BadRequestException;
import com.example.kds_attendance_service_backend.model.Employee;
import com.example.kds_attendance_service_backend.repository.EmployeeRepository;
import com.example.kds_attendance_service_backend.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
@Slf4j
public class AuthServiceImpl implements AuthService{

    private final EmployeeRepository employeeRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final JwtUtil jwtUtil;
    @Override
    public AuthResponseDto login(LoginRequestDto requestDto) {

        Employee employee = employeeRepository.findByPhoneNumber(requestDto.getPhoneNumber()).orElseThrow(
                ()-> new BadRequestException("Invalid credentials")
        );

       if(!bCryptPasswordEncoder.matches(requestDto.getPassword(),employee.getPassword())){
           throw new BadRequestException("Invalid credentials");
       }

        String token = null;
       token = jwtUtil.generateToken(employee.getId(),employee.getRole().name());
       log.info("token for user {} with id {} created",employee.getName(),employee.getId());


        return  AuthResponseDto.builder()
               .name(employee.getName())
               .token(token)
               .role(employee.getRole().name())
                .employeeId(employee.getId())
               .build();
    }
}
