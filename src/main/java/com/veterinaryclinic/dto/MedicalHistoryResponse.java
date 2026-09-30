package com.veterinaryclinic.dto;

import java.util.List;

public record MedicalHistoryResponse(
    MedicalHistoryRecordResponse medicalResponse,
    List<MedicalObservationResponse> observations,
    List<MedicationPrescriptionResponse> medications,
    List<TreatmentRecommendationResponse> recommendations
) {}
