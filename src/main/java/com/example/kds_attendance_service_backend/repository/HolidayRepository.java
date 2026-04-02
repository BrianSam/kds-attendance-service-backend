package com.example.kds_attendance_service_backend.repository;

import com.example.kds_attendance_service_backend.model.Holiday;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;


public interface HolidayRepository extends JpaRepository<Holiday,Long> {
    boolean existsByDate(LocalDate date);

    boolean existsByDateAndAreaId(LocalDate date, Long areaId);

    @Query("""
        SELECT CASE WHEN COUNT(h) > 0 THEN true ELSE false END
        FROM Holiday h
        WHERE h.date = :date
        AND (h.area.id = :areaId OR h.area IS NULL)
    """)
    boolean existsHoliday(@Param("date") LocalDate date,
                          @Param("areaId") Long areaId);
}
