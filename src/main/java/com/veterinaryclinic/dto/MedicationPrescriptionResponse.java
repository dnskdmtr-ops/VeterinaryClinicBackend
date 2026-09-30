package com.veterinaryclinic.dto;

import com.veterinaryclinic.entity.MedicationPrescriptionStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor 
public class MedicationPrescriptionResponse {
    private Long id;
    private Long medicalRecordId;
    private Long previousPrescriptionId;
    private String medicationName;
    private String dosage;
    private String frequency;
    private String administrationTimes;
    private String duration;
    private LocalDate startDate;
    private LocalDate endDate;
    private String instructions;
    private MedicationPrescriptionStatus status;
    private LocalDateTime createdAt;
}
