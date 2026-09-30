package com.veterinaryclinic.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.time.LocalDate;

@Data
public class MedicationPrescriptionRequest {
    @NotNull(message = "ID медичного запису є обов'язковим")
    private Long medicalRecordId;
    private Long previousPrescriptionId;

    @NotBlank(message = "Назва препарату є обов'язковою")
    private String medicationName;
    private String dosage;
    private String frequency;
    private String administrationTimes;
    private String duration;

    @NotNull(message = "Дата початку лікування є обов'язковою")
    private LocalDate startDate;
    private LocalDate endDate;
    private String instructions;
}
