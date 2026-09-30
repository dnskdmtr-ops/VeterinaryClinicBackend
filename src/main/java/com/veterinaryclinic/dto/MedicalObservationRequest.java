package com.veterinaryclinic.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class MedicalObservationRequest {
    @NotNull(message = "ID медичного запису є обов'язковим")
    private Long medicalRecordId;
    private String condition;
    private String symptoms;
    private String observation;
    private String notes;
}
