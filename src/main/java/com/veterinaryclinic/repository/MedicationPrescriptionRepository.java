package com.veterinaryclinic.repository;

import com.veterinaryclinic.entity.MedicalRecord;
import com.veterinaryclinic.entity.MedicationPrescription;
import com.veterinaryclinic.entity.MedicationPrescriptionStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface MedicationPrescriptionRepository extends JpaRepository<MedicationPrescription, Long> {
    List<MedicationPrescription> findByMedicalRecord(MedicalRecord medicalRecord);
    List<MedicationPrescription> findByMedicalRecordPetId(Long petId);
    List<MedicationPrescription> findByMedicalRecordPetIdAndStatus(Long petId, MedicationPrescriptionStatus status);
}