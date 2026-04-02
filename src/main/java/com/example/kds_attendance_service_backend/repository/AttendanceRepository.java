package com.example.kds_attendance_service_backend.repository;

import com.example.kds_attendance_service_backend.model.Attendance;
import com.example.kds_attendance_service_backend.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
public interface AttendanceRepository extends JpaRepository<Attendance,Long> {
    Optional<Attendance> findByEmployeeIdAndDate(Employee employee, LocalDate date);

    List<Attendance> findByEmployeeAndDateBetween(
            Employee employee,
            LocalDate startDate,
            LocalDate endDate
    );
    List<Attendance> findByDate(LocalDate date);

    List<Attendance> findByEmployeeIdInAndDate(List<Long> employeeIds, LocalDate date);

    boolean existsByEmployeeAndDate(Employee employee, LocalDate date);
}
