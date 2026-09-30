package com.veterinaryclinic.repository;

import com.veterinaryclinic.entity.Clinic;
import com.veterinaryclinic.entity.ClinicPatient;
import com.veterinaryclinic.entity.ClinicPatientStatus;
import com.veterinaryclinic.entity.Pet;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ClinicPatientRepository extends JpaRepository<ClinicPatient, Long>{
    Optional<ClinicPatient> findByPetAndClinic(Pet pet, Clinic clinic);
    List<ClinicPatient> findByClinicAndStatus(Clinic clinic, ClinicPatientStatus status);
}
