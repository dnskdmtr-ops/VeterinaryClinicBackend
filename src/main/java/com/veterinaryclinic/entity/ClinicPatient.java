package com.veterinaryclinic.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity 
@Table(name = "clinic_patients", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"pet_id", "clinic_id"})
})
@Getter 
@Setter 
@NoArgsConstructor 
@AllArgsConstructor 
@Builder 
public class ClinicPatient {
    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "pet_id", nullable = false)
    private Pet pet;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "clinic_id", nullable = false)
    private Clinic clinic;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private ClinicPatientStatus status;
    private LocalDateTime archivedAt;

    @Column(length = 255)
    private String archiveReason;

    @Column(nullable = false)
    private LocalDateTime createdAt;
}
