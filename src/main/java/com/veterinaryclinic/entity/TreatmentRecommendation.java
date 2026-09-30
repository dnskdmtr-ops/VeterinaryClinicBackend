package com.veterinaryclinic.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity 
@Table(name = "treatment_recommendations")
@Getter 
@Setter 
@NoArgsConstructor 
@AllArgsConstructor 
@Builder 
public class TreatmentRecommendation {
    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "medical_record_id", nullable = false)
    private MedicalRecord medicalRecord;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private TreatmentRecommendationType type;

    @Column(nullable = false, length = 3000)
    private String recommendation;

    @Column(length = 500)
    private String schedule;

    @Column(nullable = false)
    private LocalDateTime createdAt;
}
