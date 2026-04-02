package com.example.kds_attendance_service_backend.repository;

import com.example.kds_attendance_service_backend.model.Employee;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface EmployeeRepository extends JpaRepository<Employee,Long> {

    Optional<Employee> findByPhoneNumber(String phoneNumber);
    Page<Employee> findByAreaId(Long areaId, Pageable pageable);

    @Query("""
    SELECT e FROM Employee e
    WHERE e.area.id = :areaId
    AND e.status = 'ACTIVE'
    AND e.joiningDate <= :date
    AND (e.exitDate IS NULL OR e.exitDate >= :date)
""")
    Page<Employee> findActiveEmployeesForDate(
            @Param("areaId") Long areaId,
            @Param("date") LocalDate date,
            Pageable pageable
    );




}
