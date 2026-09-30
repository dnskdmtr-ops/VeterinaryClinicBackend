package com.veterinaryclinic.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.veterinaryclinic.entity.MedicalRecord;
import com.veterinaryclinic.entity.TreatmentRecommendation;

public interface TreatmentRecommendationRepository extends JpaRepository<TreatmentRecommendation, Long> {
    List<TreatmentRecommendation> findByMedicalRecordOrderByCreatedAtAsc(MedicalRecord medicalRecord);
}
