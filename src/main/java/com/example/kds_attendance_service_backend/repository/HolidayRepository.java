package com.example.kds_attendance_service_backend.repository;

import com.example.kds_attendance_service_backend.model.Holiday;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDate;


public interface HolidayRepository extends JpaRepository<Holiday,Long> {
    boolean existsByDate(LocalDate date);

    boolean existsByDateAndAreaId(LocalDate date, Long areaId);
}
