package com.veterinaryclinic.repository;

import com.veterinaryclinic.entity.MedicalObservation;
import com.veterinaryclinic.entity.MedicalRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface  MedicalObservationRepository extends JpaRepository<MedicalObservation, Long> {
    List<MedicalObservation> findByMedicalRecordOrderByCreatedAtAsc(MedicalRecord medicalRecord);
}