package com.example.kds_attendance_service_backend.repository;

import com.example.kds_attendance_service_backend.model.Employee;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

public interface EmployeeRepository extends JpaRepository<Employee,Long> {

    Optional<Employee> findByPhoneNumber(String phoneNumber);
    Page<Employee> findByAreaId(Long areaId, Pageable pageable);




}
