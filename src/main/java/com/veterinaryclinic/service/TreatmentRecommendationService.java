package com.veterinaryclinic.service;

import com.veterinaryclinic.dto.TreatmentRecommendationRequest;
import com.veterinaryclinic.entity.*;
import com.veterinaryclinic.exception.AccessDeniedException;
import com.veterinaryclinic.repository.MedicalRecordRepository;
import com.veterinaryclinic.repository.TreatmentRecommendationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Service 
@RequiredArgsConstructor 
public class TreatmentRecommendationService {
    private final TreatmentRecommendationRepository recommendationRepository;
    private final MedicalRecordRepository medicalRecordRepository;

    public TreatmentRecommendation createRecommendation(TreatmentRecommendationRequest request, User doctor) {
        if (doctor.getRole() != Role.DOCTOR) {
            throw new AccessDeniedException("Тільки лікар може створювати рекомендації");
        }

        MedicalRecord medicalRecord = medicalRecordRepository.findById(request.getMedicalRecordId()).orElseThrow(() -> new IllegalArgumentException("Медичний запис не знайдено"));

        if (!medicalRecord.getDoctor().getId().equals(doctor.getId())) {
            throw new AccessDeniedException("Ви не можете змінювати цей медичний запис");
        }

        TreatmentRecommendation recommendation = TreatmentRecommendation.builder()
                                                                        .medicalRecord(medicalRecord)
                                                                        .type(request.getType())
                                                                        .recommendation(request.getRecommendation())
                                                                        .schedule(request.getSchedule())
                                                                        .createdAt(LocalDateTime.now())
                                                                        .build();

        return recommendationRepository.save(recommendation);
    }

    public List<TreatmentRecommendation> getRecommendations(Long medicalRecordId, User doctor) {
        if (doctor.getRole() != Role.DOCTOR) {
            throw new AccessDeniedException("Тільки лікар може переглядати ці рекомендації");
        }

        MedicalRecord medicalRecord = medicalRecordRepository.findById(medicalRecordId).orElseThrow(() -> new IllegalArgumentException("Медичний запис не знайдено"));

        if (!medicalRecord.getDoctor().getId().equals(doctor.getId())) {
            throw new AccessDeniedException("Немає доступу до цього медичного запису");
        }

        return recommendationRepository.findByMedicalRecordOrderByCreatedAtAsc(medicalRecord);
    }
}