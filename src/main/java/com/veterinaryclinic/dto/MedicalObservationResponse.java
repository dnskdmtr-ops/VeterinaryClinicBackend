package com.veterinaryclinic.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class MedicalObservationResponse {
    private Long id;
    private Long medicalRecordId;
    private String condition;
    private String symptoms;
    private String observation;
    private String notes;
    private LocalDateTime createdAt;
}
