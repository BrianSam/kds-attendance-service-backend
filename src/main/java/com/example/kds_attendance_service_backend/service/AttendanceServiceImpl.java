package com.example.kds_attendance_service_backend.service;

import com.example.kds_attendance_service_backend.dto.AttendanceRequestDto;
import com.example.kds_attendance_service_backend.dto.DailyAttendanceResponseDto;
import com.example.kds_attendance_service_backend.dto.MonthlyAttendanceResponseDto;
import com.example.kds_attendance_service_backend.exception.BadRequestException;
import com.example.kds_attendance_service_backend.exception.ResourceNotFoundException;
import com.example.kds_attendance_service_backend.model.*;
import com.example.kds_attendance_service_backend.repository.AttendanceRepository;
import com.example.kds_attendance_service_backend.repository.EmployeeRepository;
import com.example.kds_attendance_service_backend.repository.HolidayRepository;
import com.example.kds_attendance_service_backend.security.CustomUserDetails;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.YearMonth;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class AttendanceServiceImpl implements AttendanceService{

    private final AttendanceRepository attendanceRepository;
    private final EmployeeRepository employeeRepository;
    private final HolidayRepository holidayRepository;
    @Override
    @Transactional
    public void markAttendance(AttendanceRequestDto request) {
        Employee authEmployee = getCurrentEmployee();

        Employee employee = employeeRepository.findById(authEmployee.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found"));
        LocalDate today = LocalDate.now();
        Area area = employee.getArea();
        if (area == null) {

            throw new BadRequestException("Employee is not assigned to any area");
        }
        boolean isHoliday =
                holidayRepository.existsByDate(today) ||
                        holidayRepository.existsByDateAndAreaId(today, area.getId());

        if (isHoliday) {
            throw new BadRequestException("Attendance cannot be marked on holiday");
        }
        if (today.isBefore(employee.getJoiningDate())) {
            throw new BadRequestException("Employee has not joined yet");
        }

        if (employee.getExitDate() != null && today.isAfter(employee.getExitDate())) {
            throw new BadRequestException("Employee has already exited");
        }

        log.info("Employee {} is marking attendance", employee.getId());

        // Step 2: Check duplicate
        if (attendanceRepository.existsByEmployeeAndDate(employee, today)) {
            log.warn("Employee {} already marked attendance for {}", employee.getId(), today);
            throw new BadRequestException("Attendance already marked for today");
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

    @Override
    @Transactional(readOnly = true)
    public Page<DailyAttendanceResponseDto> getDailyReport(Long areaId, LocalDate date, int page, int size) {
        log.info("Fetching daily attendance report for areaId={}, date={}, page={}, size={}",
                areaId, date, page, size);
        Pageable pageable = PageRequest.of(page,size);

        Page<Employee>pagedEmployees = employeeRepository.findActiveEmployeesForDate(areaId,date,pageable);
        List<Employee>employeesList = pagedEmployees.getContent();
        log.debug("Fetched {} employees for areaId={}", employeesList.size(), areaId);

        boolean isHoliday = holidayRepository.existsHoliday(date, areaId);

        if(isHoliday){
            log.info("Date {} is a holiday for areaId={}", date, areaId);
            List<DailyAttendanceResponseDto> holidayList = employeesList.stream()
                    .map(emp->new DailyAttendanceResponseDto(emp.getName(),"HOLIDAY",null)).toList();
            return new PageImpl<>(holidayList,pageable,pagedEmployees.getTotalElements());
        }

            List<Long>employeeIds = employeesList.stream().map(emp->emp.getId()).toList();
            List<Attendance>attendances = attendanceRepository.findByEmployeeIdInAndDate(employeeIds,date);
        log.debug("Fetched {} attendance records for date={}", attendances.size(), date);

            Map<Long,Attendance>attendanceMap = new HashMap<>();

            for(Attendance a:attendances){
                Long empId = a.getEmployee().getId();
                attendanceMap.put(empId,a);
            }
            List<DailyAttendanceResponseDto>responseDtoList = new ArrayList<>();

            for(Employee emp :employeesList){
                Long empId = emp.getId();
                String name = emp.getName();

                if(attendanceMap.containsKey(empId)){
                    String status = attendanceMap.get(empId).getStatus().name();
                    String photoUrl = attendanceMap.get(empId).getPhotoUrl();
                    DailyAttendanceResponseDto responseDto = new DailyAttendanceResponseDto(
                            name,
                            status,
                            photoUrl
                    );
                    responseDtoList.add(responseDto);


                }
                else{
                    DailyAttendanceResponseDto responseDto = new DailyAttendanceResponseDto(
                            name,
                            "ABSENT",
                            null
                    );
                    responseDtoList.add(responseDto);
                }
            }
        log.info("Successfully generated report for areaId={}, date={}", areaId, date);

            return new PageImpl<>(responseDtoList,pageable,pagedEmployees.getTotalElements());





    }

    @Override
    @Transactional(readOnly = true)
    public Page<MonthlyAttendanceResponseDto> getMonthlyReport(
            Long areaId,
            int year,
            int month,
            int page,
            int size
    ) {

        Pageable pageable = PageRequest.of(page, size);

        // 1. Validate future month
        YearMonth requestedMonth = YearMonth.of(year, month);
        YearMonth currentMonth = YearMonth.from(LocalDate.now());

        if (requestedMonth.isAfter(currentMonth)) {
            throw new BadRequestException("Cannot fetch report for future month");
        }

        // 2. Fetch employees
        Page<Employee> employeePage =
                employeeRepository.findByAreaId(areaId, pageable);

        List<Employee> employees = employeePage.getContent();

        // 3. Date range
        LocalDate startDate = requestedMonth.atDay(1);
        LocalDate endDate = requestedMonth.atEndOfMonth();

        // Cap for current month
        if (requestedMonth.equals(currentMonth)) {
            endDate = LocalDate.now();
        }

        // 4. Fetch holidays
        List<Holiday> holidays =
                holidayRepository.findHolidaysForMonth(startDate, endDate, areaId);

        Set<LocalDate> holidayDates = holidays.stream()
                .map(Holiday::getDate)
                .collect(Collectors.toSet());

        // 5. Extract employee IDs
        List<Long> employeeIds = employees.stream()
                .map(Employee::getId)
                .toList();

        // 6. Fetch attendance
        List<Attendance> attendances =
                attendanceRepository.findByEmployeeIdInAndDateBetween(
                        employeeIds, startDate, endDate
                );

        // 7. Group attendance
        Map<Long, List<Attendance>> attendanceMap =
                attendances.stream()
                        .collect(Collectors.groupingBy(
                                a -> a.getEmployee().getId()
                        ));

        // 8. Build response
        List<MonthlyAttendanceResponseDto> response = new ArrayList<>();

        for (Employee emp : employees) {

            LocalDate effectiveStartDate = startDate;
            LocalDate effectiveEndDate = endDate;

            // Adjust for joining date
            if (emp.getJoiningDate().isAfter(startDate)) {
                effectiveStartDate = emp.getJoiningDate();
            }

            // Adjust for exit date
            if (emp.getExitDate() != null &&
                    emp.getExitDate().isBefore(endDate)) {
                effectiveEndDate = emp.getExitDate();
            }

            // Skip invalid range
            if (effectiveStartDate.isAfter(effectiveEndDate)) {
                response.add(new MonthlyAttendanceResponseDto(
                        emp.getName(), 0, 0, 0, 0, 0
                ));
                continue;
            }

            // Total days
            int totalDays =
                    (int) ChronoUnit.DAYS.between(effectiveStartDate, effectiveEndDate) + 1;

            LocalDate finalStartDate = effectiveStartDate;
            LocalDate finalEndDate = effectiveEndDate;
            // Holiday count (filtered)
            long employeeHolidayDays = holidayDates.stream()
                    .filter(d -> !d.isBefore(finalStartDate) &&
                            !d.isAfter(finalEndDate))
                    .count();

            int workingDays = totalDays - (int) employeeHolidayDays;

            List<Attendance> empAttendance =
                    attendanceMap.getOrDefault(emp.getId(), Collections.emptyList());

            Set<LocalDate> presentDates = new HashSet<>();
            int lateDays = 0;

            for (Attendance a : empAttendance) {

                LocalDate date = a.getDate();

                // Ignore outside effective range
                if (date.isBefore(effectiveStartDate) ||
                        date.isAfter(effectiveEndDate)) {
                    continue;
                }

                // Ignore holidays
                if (holidayDates.contains(date)) {
                    continue;
                }

                presentDates.add(date);

                if (a.getStatus() == AttendanceStatus.LATE) {
                    lateDays++;
                }
            }

            int presentDays = presentDates.size();
            int absentDays = workingDays - presentDays;

            response.add(new MonthlyAttendanceResponseDto(
                    emp.getName(),
                    presentDays,
                    absentDays,
                    lateDays,
                    (int) employeeHolidayDays,
                    workingDays
            ));
        }

        return new PageImpl<>(response, pageable, employeePage.getTotalElements());
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
