package com.example.kds_attendance_service_backend.controller;

import com.example.kds_attendance_service_backend.dto.*;
import com.example.kds_attendance_service_backend.service.AreaService;
import com.example.kds_attendance_service_backend.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

   private final EmployeeService employeeService;
   private final AreaService areaService;

   @PostMapping("/createEmployee")
   @PreAuthorize("hasRole('ADMIN')")
   public ResponseEntity<CreateEmployeeResponseDto> createEmployee(@RequestBody CreateEmployeeDto employeeDto){
       String password = employeeService.createEmployee(employeeDto);

       CreateEmployeeResponseDto responseDto = CreateEmployeeResponseDto.builder()
               .mobileNo(employeeDto.getPhoneNumber())
               .password(password).build();

       return  ResponseEntity.ok(responseDto);
   }

   @PostMapping("/createArea")
   @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> createArea(@RequestBody  CreateAreaRequestDto createAreaRequestDto){
       Long id = areaService.createArea(createAreaRequestDto);

       return ResponseEntity.ok("area with name "+createAreaRequestDto.getName()+" created with id "+id);


   }

    @PutMapping("/{id}/exit")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> markEmployeeExit(
            @PathVariable Long id,
            @RequestBody ExitEmployeeRequestDto request
    ) {
        employeeService.markEmployeeExit(id, request.getExitDate());
        return ResponseEntity.ok("Employee exit date updated successfully");
    }



}
