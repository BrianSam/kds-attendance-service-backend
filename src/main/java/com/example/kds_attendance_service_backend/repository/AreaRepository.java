package com.example.kds_attendance_service_backend.repository;

import com.example.kds_attendance_service_backend.model.Area;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
public interface AreaRepository extends JpaRepository<Area,Long> {

    Optional<Area> findById(Long id);

    Optional<Area> findByName(String name);

    @Query("SELECT DISTINCT a FROM Area a")
    List<Area> findDistinctAreas();



}
