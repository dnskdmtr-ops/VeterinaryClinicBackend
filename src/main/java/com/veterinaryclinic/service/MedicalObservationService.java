package com.veterinaryclinic.service;

import com.veterinaryclinic.dto.MedicalObservationRequest;
import com.veterinaryclinic.entity.MedicalObservation;
import com.veterinaryclinic.entity.MedicalRecord;
import com.veterinaryclinic.entity.Role;
import com.veterinaryclinic.entity.User;
import com.veterinaryclinic.repository.MedicalObservationRepository;
import com.veterinaryclinic.repository.MedicalRecordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor 
public class MedicalObservationService {
    private final MedicalObservationRepository medicalObservationRepository;
    private final MedicalRecordRepository medicalRecordRepository;

    public MedicalObservation createObservation(MedicalObservationRequest request, User doctor) {
        if (doctor.getRole() != Role.DOCTOR) {
            throw new IllegalArgumentException("Тільки лікар може додавати медичні спостереження");
        }

        MedicalRecord medicalRecord = medicalRecordRepository.findById(request.getMedicalRecordId()).orElseThrow(() -> new IllegalArgumentException("Медичний запис не знайдено"));

        if (!medicalRecord.getDoctor().getId().equals(doctor.getId())) {
            throw new IllegalArgumentException("Цей медичний запис не належить даному лікарю");
        }

        MedicalObservation observation = MedicalObservation.builder()
                                                           .medicalRecord(medicalRecord)
                                                           .condition(request.getCondition())
                                                           .symptoms(request.getSymptoms())
                                                           .observation(request.getObservation())
                                                           .notes(request.getNotes())
                                                           .createdAt(LocalDateTime.now())
                                                           .build();

        return medicalObservationRepository.save(observation);
    }

    public List<MedicalObservation> getObservations(Long medicalRecordId, User doctor) {
        if (doctor.getRole() != Role.DOCTOR) {
            throw new IllegalArgumentException("Тільки лікар може переглядати медичні спостереження");
        }

        MedicalRecord medicalRecord = medicalRecordRepository.findById(medicalRecordId).orElseThrow(() -> new IllegalArgumentException("Медичний запис не знайдено"));

        if (!medicalRecord.getDoctor().getId().equals(doctor.getId())) {
            throw new IllegalArgumentException("Цей медичний запис не належить даному лікарю");
        }

        return medicalObservationRepository.findByMedicalRecordOrderByCreatedAtAsc(medicalRecord);
    }
}
