package com.example.kds_attendance_service_backend.service;

import com.example.kds_attendance_service_backend.dto.AttendanceRequestDto;
import com.example.kds_attendance_service_backend.exception.BadRequestException;
import com.example.kds_attendance_service_backend.exception.ResourceNotFoundException;
import com.example.kds_attendance_service_backend.model.Area;
import com.example.kds_attendance_service_backend.model.Attendance;
import com.example.kds_attendance_service_backend.model.AttendanceStatus;
import com.example.kds_attendance_service_backend.model.Employee;
import com.example.kds_attendance_service_backend.repository.AttendanceRepository;
import com.example.kds_attendance_service_backend.repository.EmployeeRepository;
import com.example.kds_attendance_service_backend.security.CustomUserDetails;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Service
@Slf4j
@RequiredArgsConstructor
public class AttendanceServiceImpl implements AttendanceService{

    private final AttendanceRepository attendanceRepository;
    private final EmployeeRepository employeeRepository;
    @Override
    @Transactional
    public void markAttendance(AttendanceRequestDto request) {
        Employee authEmployee = getCurrentEmployee();

        Employee employee = employeeRepository.findById(authEmployee.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found"));
        LocalDate today = LocalDate.now();
        log.info("Employee {} is marking attendance", employee.getId());

        // Step 2: Check duplicate
        if (attendanceRepository.existsByEmployeeAndDate(employee, today)) {
            log.warn("Employee {} already marked attendance for {}", employee.getId(), today);
            throw new BadRequestException("Attendance already marked for today");
        }
        Area area = employee.getArea();
        if (area == null) {
            log.warn("Employee {} already marked attendance for {}", employee.getId(), today);
            throw new BadRequestException("Employee is not assigned to any area");
        }

        boolean isInside = isWithinRadius(
                request.getLatitude(),
                request.getLongitude(),
                area.getLatitude(),
                area.getLongitude(),
                area.getRadius(),
                employee
        );

        if (!isInside) {
            throw new BadRequestException("You are outside allowed location please com closer to area !! ");
        }
        LocalTime currentTime = LocalTime.now();

        AttendanceStatus status =
                currentTime.isAfter(area.getLateTime())
                        ? AttendanceStatus.LATE
                        : AttendanceStatus.ON_TIME;

        Attendance attendance = Attendance.builder()
                .employee(employee)
                .area(area)
                .date(today)
                .markedAt(LocalDateTime.now())
                .status(status)
                .latitude(request.getLatitude())
                .longitude(request.getLongitude())
                .photoUrl(request.getPhotoUrl())
                .build();

        attendanceRepository.save(attendance);
        log.info("attendance for employee id {} for date {} with status {} marked successfully",employee.getId(),today,attendance.getStatus());
    }

    private Employee getCurrentEmployee() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        CustomUserDetails userDetails =
                (CustomUserDetails) authentication.getPrincipal();

        return userDetails.getEmployee();
    }

    private boolean isWithinRadius(
            double userLat,
            double userLon,
            double areaLat,
            double areaLon,
            double allowedRadius,
            Employee employee
    ) {

        final int EARTH_RADIUS = 6371000; // meters

        double dLat = Math.toRadians(areaLat - userLat);
        double dLon = Math.toRadians(areaLon - userLon);

        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(userLat)) * Math.cos(Math.toRadians(areaLat))
                * Math.sin(dLon / 2) * Math.sin(dLon / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        double distance = EARTH_RADIUS * c;
        log.info("User: {} distance from area:{} is  {} meters",employee.getName(),employee.getArea().getName() ,distance);
        return distance <= allowedRadius;
    }
}
