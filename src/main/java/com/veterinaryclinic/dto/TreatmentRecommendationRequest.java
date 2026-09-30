package com.veterinaryclinic.dto;

import com.veterinaryclinic.entity.TreatmentRecommendationType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data 
public class TreatmentRecommendationRequest {
    @NotNull(message = "ID медичного запису є обов'язковим")
    private Long medicalRecordId;

    @NotNull(message = "Тип медичного запису є обов'язковим")
    private TreatmentRecommendationType type;

    @NotBlank(message = "Текст рекомендації є обов'язковим")
    private String recommendation;
    private String schedule;
}
