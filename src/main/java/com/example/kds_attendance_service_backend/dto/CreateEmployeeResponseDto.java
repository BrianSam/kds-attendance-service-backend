package com.example.kds_attendance_service_backend.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CreateEmployeeResponseDto {

    private String mobileNo;
    private String password;
}
