package com.veterinaryclinic.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.veterinaryclinic.entity.Clinic;

public interface ClinicRepository extends JpaRepository<Clinic, Long> {}
