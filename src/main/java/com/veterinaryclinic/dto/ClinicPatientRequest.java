package com.veterinaryclinic.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data 
public class ClinicPatientRequest {
    @NotNull(message = "ID тварини є обов'язковим")
    private Long petId;
}
