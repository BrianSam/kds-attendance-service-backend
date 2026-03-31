package com.example.kds_attendance_service_backend.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "attendance",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"employee_id", "date"})
        })
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Attendance extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee employee;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "area_id", nullable = false)
    private Area area;

    @Column(name = "marked_at", nullable = false)
    private LocalDateTime markedAt;

    @Enumerated(EnumType.STRING)
    private AttendanceStatus status;

    private LocalDate date;

    @Column(name = "photo_url")
    private String photoUrl;

    private Double latitude;

    private Double longitude;
}