package com.veterinaryclinic.entity;

import java.time.LocalDateTime;
import jakarta.persistence.*;
import lombok.*;

@Entity 
@Table(name = "doctor_clinics", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"doctor_id", "clinic_id"})
})
@Getter 
@Setter 
@NoArgsConstructor 
@AllArgsConstructor 
@Builder 
public class DoctorClinic {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne (fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "doctor_id", nullable = false)
    private User doctor;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "clinic_id", nullable = false)
    private Clinic clinic;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private DoctorClinicStatus status;

    @Column(nullable = false)
    private LocalDateTime createdAt;
}