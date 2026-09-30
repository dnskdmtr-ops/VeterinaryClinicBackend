package com.veterinaryclinic.repository;

import com.veterinaryclinic.entity.Clinic;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClinicRepository extends JpaRepository<Clinic, Long> {

}
