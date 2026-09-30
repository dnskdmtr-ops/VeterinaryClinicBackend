package com.veterinaryclinic.service;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import com.veterinaryclinic.dto.ClinicPatientRequest;
import com.veterinaryclinic.entity.ClinicPatient;
import com.veterinaryclinic.entity.ClinicPatientStatus;
import com.veterinaryclinic.entity.Pet;
import com.veterinaryclinic.entity.Role;
import com.veterinaryclinic.entity.User;
import com.veterinaryclinic.repository.ClinicPatientRepository;
import com.veterinaryclinic.repository.PetRepository;

import lombok.RequiredArgsConstructor;

@Service 
@RequiredArgsConstructor 
public class ClinicPatientService {
    private final ClinicPatientRepository clinicPatientRepository;
    private final PetRepository petRepository;

    public ClinicPatient addPatient(ClinicPatientRequest request, User admin) {
        if (admin.getRole() != Role.CLINIC_ADMIN) {
            throw new IllegalArgumentException("Тільки адміністратор клініки може додавати пацієнтів");
        }

        if (admin.getClinic() == null) {
            throw new IllegalArgumentException("Адміністратор не має клініки");
        }

        Pet pet = petRepository.findById(request.getPetId()).orElseThrow(() -> new IllegalArgumentException("Тварину не знайдено"));

        if (clinicPatientRepository.findByPetAndClinic(pet, admin.getClinic()).isPresent()) {
            throw new IllegalArgumentException("Ця тварина вже має медичну картку в цій клініці");
        }

        ClinicPatient clinicPatient = ClinicPatient.builder()
                                                   .pet(pet)
                                                   .clinic(admin.getClinic())
                                                   .status(ClinicPatientStatus.ACTIVE)
                                                   .createdAt(LocalDateTime.now())
                                                   .build();

        return clinicPatientRepository.save(clinicPatient);
    }
    
    public java.util.List<ClinicPatient> getActivePatients(User admin) {
        if (admin.getRole() != Role.CLINIC_ADMIN) {
            throw new IllegalArgumentException("Тільки адміністратор клініки може переглядати пацієнтів");
        }

        if (admin.getClinic() == null) {
            throw new IllegalArgumentException("Адміністратор не має клініки");
        }

        return clinicPatientRepository.findByClinicAndStatus(admin.getClinic(), ClinicPatientStatus.ACTIVE);
    }

    public ClinicPatient archivePatient(Long patientId, String reason, User admin) {
        if (admin.getRole() != Role.CLINIC_ADMIN) {
            throw new IllegalArgumentException("Тільки адміністратор клініки може архівувати пацієнтів");
        }

        if (admin.getClinic() == null) {
            throw new IllegalArgumentException("Адміністратор не має клініки");
        }

        ClinicPatient clinicPatient = clinicPatientRepository.findById(patientId).orElseThrow(() -> new IllegalArgumentException("Пацієнта не знайдено"));

        if (!clinicPatient.getClinic().getId().equals(admin.getClinic().getId())) {
            throw new IllegalArgumentException("Пацієнт не належить цій клініці");
        }

        if (clinicPatient.getStatus() == ClinicPatientStatus.ARCHIVED) {
            throw new IllegalArgumentException("Пацієнт вже знаходиться в архіві");
        }

        clinicPatient.setStatus(ClinicPatientStatus.ARCHIVED);
        clinicPatient.setArchivedAt(LocalDateTime.now());
        clinicPatient.setArchiveReason(reason);

        return clinicPatientRepository.save(clinicPatient);
    }

    public java.util.List<ClinicPatient> getArchivedPatients(User admin) {
        if (admin.getRole() != Role.CLINIC_ADMIN) {
            throw new IllegalArgumentException("Тільки адміністратор клініки може переглядати архів");
        }

        if (admin.getClinic() == null) {
            throw new IllegalArgumentException("Адміністратор не має клініки");
        }

        return clinicPatientRepository.findByClinicAndStatus(admin.getClinic(), ClinicPatientStatus.ARCHIVED);
    }

    public ClinicPatient restorePatient(Long patientId, User admin) {
        if (admin.getRole() != Role.CLINIC_ADMIN) {
            throw new IllegalArgumentException("Тільки адміністратор клініки може відновлювати пацієнтів");
        }

        if (admin.getClinic() == null) {
            throw new IllegalArgumentException("Адміністратор не має клініки");
        }

        ClinicPatient clinicPatient = clinicPatientRepository.findById(patientId).orElseThrow(() -> new IllegalArgumentException("Пацієнта не знайдено"));

        if (!clinicPatient.getClinic().getId().equals(admin.getClinic().getId())) {
            throw new IllegalArgumentException("Пацієнт не належить цій клініці");
        }

        if (clinicPatient.getStatus() == ClinicPatientStatus.ACTIVE) {
            throw new IllegalArgumentException("Пацієнт вже активний");
        }

        clinicPatient.setStatus(ClinicPatientStatus.ACTIVE);
        clinicPatient.setArchivedAt(null);
        clinicPatient.setArchiveReason(null);

        return clinicPatientRepository.save(clinicPatient);
    }
}
