package com.veterinaryclinic.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.veterinaryclinic.entity.Appointment;
import com.veterinaryclinic.entity.MedicalRecord;
import com.veterinaryclinic.entity.Pet;
import com.veterinaryclinic.entity.User;

public interface MedicalRecordRepository extends JpaRepository<MedicalRecord, Long> {
    Optional<MedicalRecord> findByAppointment(Appointment appointment);
    Optional<MedicalRecord> findTopByPetOrderByCreatedAtDesc(Pet pet);
    List<MedicalRecord> findByPetOrderByCreatedAtAsc(Pet pet);
    List<MedicalRecord> findByDoctor(User doctor);
}