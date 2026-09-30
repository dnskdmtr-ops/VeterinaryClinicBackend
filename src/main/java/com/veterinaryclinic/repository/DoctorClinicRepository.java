package com.veterinaryclinic.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.veterinaryclinic.entity.Clinic;
import com.veterinaryclinic.entity.DoctorClinic;
import com.veterinaryclinic.entity.DoctorClinicStatus;
import com.veterinaryclinic.entity.User;

public interface DoctorClinicRepository  extends JpaRepository<DoctorClinic, Long> {
    List<DoctorClinic> findByDoctor(User doctor);
    List<DoctorClinic> findByClinic(Clinic clinic);
    Optional<DoctorClinic> findByDoctorAndClinic(User doctor, Clinic clinic);
    List<DoctorClinic> findByClinicAndStatus(Clinic clinic, DoctorClinicStatus status);
}
