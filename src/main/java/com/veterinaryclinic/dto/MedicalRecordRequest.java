package com.veterinaryclinic.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class MedicalRecordRequest {
    @NotNull(message = "ID прийом є обов'язковим")
    private Long appointmentId;
    private String complaints;
    private String symptoms;
    private String examinationResults;
    private String diagnosis;
    private String examinations;
    private String treatment;
    private String recommendations;
    private String notes;
}
