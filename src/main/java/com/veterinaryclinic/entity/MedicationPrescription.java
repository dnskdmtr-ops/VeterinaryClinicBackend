package com.veterinaryclinic.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "medication_prescriptions")
@Getter 
@Setter 
@NoArgsConstructor 
@AllArgsConstructor 
@Builder 
public class MedicationPrescription {
    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "medical_record_id", nullable = false)
    private MedicalRecord medicalRecord;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "previous_prescription_id")
    private MedicationPrescription previousPrescription;

    @Column(nullable = false, length = 200)
    private String medicationName;

    @Column(length = 200)
    private String dosage;

    @Column(length = 200)
    private String frequency;

    @Column(length = 500)
    private String administrationTimes;

    @Column(length = 200)
    private String duration;

    @Column(nullable = false)
    private LocalDate startDate;
    private LocalDate endDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private MedicationPrescriptionStatus status;

    @Column(length = 2000)
    private String instructions;

    @Column(nullable = false)
    private LocalDateTime createdAt;
}
