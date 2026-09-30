package com.veterinaryclinic.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.veterinaryclinic.dto.MedicationPrescriptionRequest;
import com.veterinaryclinic.entity.MedicalRecord;
import com.veterinaryclinic.entity.MedicationPrescription;
import com.veterinaryclinic.entity.MedicationPrescriptionStatus;
import com.veterinaryclinic.entity.Role;
import com.veterinaryclinic.entity.User;
import com.veterinaryclinic.exception.AccessDeniedException;
import com.veterinaryclinic.repository.MedicalRecordRepository;
import com.veterinaryclinic.repository.MedicationPrescriptionRepository;

import lombok.RequiredArgsConstructor;

@Service 
@RequiredArgsConstructor 
public class MedicationPrescriptionService {
    private final MedicationPrescriptionRepository prescriptionRepository;
    private final MedicalRecordRepository medicalRecordRepository;
    private final ReminderService reminderService;

    public MedicationPrescription createPrescription(MedicationPrescriptionRequest request, User doctor) {
        if (doctor.getRole() != Role.DOCTOR) {
            throw new IllegalArgumentException("Тільки лікар може створювати призначення");
        }

        MedicalRecord medicalRecord = medicalRecordRepository.findById(request.getMedicalRecordId()).orElseThrow(() -> new IllegalArgumentException("Медичний запис не знайдено"));

        if (!medicalRecord.getDoctor().getId().equals(doctor.getId())) {
            throw new IllegalArgumentException("Цей медичний запис не належить даному лікарю");
        }

        MedicationPrescription previousPrescription = null;

        if (request.getPreviousPrescriptionId() != null) {
            previousPrescription = prescriptionRepository.findById(request.getPreviousPrescriptionId()).orElseThrow(() -> new IllegalArgumentException("Попереднє призначення не знайдено"));

            if (!previousPrescription.getMedicalRecord()
                                     .getPet()
                                     .getId()
                                     .equals(medicalRecord.getPet().getId())) {
                throw new IllegalArgumentException("Попереднє призначення належить іншій тварині");
            }
        }

        MedicationPrescription prescription = prescriptionRepository.save(MedicationPrescription.builder()
                                                                    .medicalRecord(medicalRecord)
                                                                    .previousPrescription(previousPrescription)
                                                                    .medicationName(request.getMedicationName())
                                                                    .dosage(request.getDosage())
                                                                    .frequency(request.getFrequency())
                                                                    .administrationTimes(request.getAdministrationTimes())
                                                                    .duration(request.getDuration())
                                                                    .startDate(request.getStartDate())
                                                                    .endDate(request.getEndDate())
                                                                    .instructions(request.getInstructions())
                                                                    .status(MedicationPrescriptionStatus.ACTIVE)
                                                                    .createdAt(LocalDateTime.now())
                                                                    .build()
        );

        reminderService.createMedicationReminders(prescription);

        return prescriptionRepository.save(prescription);
    }

    public List<MedicationPrescription> getPetPrescriptions(Long petId, User doctor) {
        if (doctor.getRole() != Role.DOCTOR) {
            throw new IllegalArgumentException("Тільки лікар може переглядати призначення");
        }

        return prescriptionRepository.findByMedicalRecordPetId(petId);
    }

    public MedicationPrescription completePrescription(Long prescriptionId, User doctor) {
        if (doctor.getRole() != Role.DOCTOR) {
            throw new AccessDeniedException("Тільки лікар може змінювати призначення");
        }

        MedicationPrescription prescription = prescriptionRepository.findById(prescriptionId).orElseThrow(() -> new IllegalArgumentException("Призначення не знайдено"));

        if (!prescription.getMedicalRecord()
                         .getDoctor()
                         .getId()
                         .equals(doctor.getId())) {
            throw new AccessDeniedException("Ви не можете змінювати це призначення");
        }

        if (prescription.getStatus() == MedicationPrescriptionStatus.CANCELLED) {
            throw new IllegalArgumentException("Скасоване призначення не можна завершити");
        }

        if (prescription.getStatus() == MedicationPrescriptionStatus.COMPLETED) {
            return prescription;
        }

        prescription.setStatus(MedicationPrescriptionStatus.COMPLETED);
        reminderService.cancelPendingReminders(prescription);

        return prescriptionRepository.save(prescription);
    }

    public MedicationPrescription cancelPrescription(Long prescriptionId, User doctor) {
        if (doctor.getRole() != Role.DOCTOR) {
            throw new AccessDeniedException("Тільки лікар може змінювати призначення");
        }

        MedicationPrescription prescription = prescriptionRepository.findById(prescriptionId).orElseThrow(() -> new IllegalArgumentException("Призначення не знайдено"));

        if (!prescription.getMedicalRecord()
                         .getDoctor()
                         .getId()
                         .equals(doctor.getId())) {
            throw new AccessDeniedException("Ви не можете змінювати це призначення");
        }

        if (prescription.getStatus() == MedicationPrescriptionStatus.COMPLETED) {
            throw new IllegalArgumentException("Завершене призначення не можна скасувати");
        }

        if (prescription.getStatus() == MedicationPrescriptionStatus.CANCELLED) {
            return prescription;
        }

        prescription.setStatus(MedicationPrescriptionStatus.CANCELLED);
        reminderService.cancelPendingReminders(prescription);

        return prescriptionRepository.save(prescription);
    }
}
