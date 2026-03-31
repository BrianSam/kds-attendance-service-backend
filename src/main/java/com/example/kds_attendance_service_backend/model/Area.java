package com.example.kds_attendance_service_backend.model;

import jakarta.persistence.Column;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalTime;

@Entity
@Table(name = "areas")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Area extends BaseEntity {

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Double latitude;

    @Column(nullable = false)
    private Double longitude;


    @Column(name = "late_time", nullable = false)
    private LocalTime lateTime;

    @Column(name = "radius", nullable = false)
    private Double radius;


}