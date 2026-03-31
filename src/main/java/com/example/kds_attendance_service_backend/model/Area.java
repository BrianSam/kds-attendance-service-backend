package com.example.kds_attendance_service_backend.model;

import jakarta.persistence.Column;
import jakarta.persistence.*;
import lombok.*;

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

    @Column(name = "qr_code", nullable = false, unique = true)
    private String qrCode;
}