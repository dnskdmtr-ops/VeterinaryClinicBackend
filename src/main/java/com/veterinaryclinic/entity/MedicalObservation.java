package com.veterinaryclinic.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity 
@Table(name = "medical_observations")
@Getter 
@Setter 
@NoArgsConstructor 
@AllArgsConstructor 
@Builder 
public class MedicalObservation {
    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "medical_record_id", nullable = false)
    private MedicalRecord medicalRecord;

    @Column(length = 2000)
    private String condition;

    @Column(length = 3000)
    private String symptoms;

    @Column(length = 3000)
    private String observation;

    @Column(length = 3000)
    private String notes;

    @Column(nullable = false)
    private LocalDateTime createdAt;
}
