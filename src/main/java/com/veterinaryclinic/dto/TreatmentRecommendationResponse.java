package com.veterinaryclinic.dto;

import com.veterinaryclinic.entity.TreatmentRecommendationType;
import java.time.LocalDateTime;

public record  TreatmentRecommendationResponse(
    Long id,
    Long medicalRecordId,
    TreatmentRecommendationType type,
    String recommendation,
    String schedule,
    LocalDateTime createdAt
) {}
