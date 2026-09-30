package com.veterinaryclinic.repository;

import com.veterinaryclinic.entity.Appointment;
import com.veterinaryclinic.entity.AppointmentStatus;
import com.veterinaryclinic.entity.Clinic;
import com.veterinaryclinic.entity.Pet;
import com.veterinaryclinic.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDateTime;
import java.util.List;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
    List<Appointment> findByPet(Pet pet);
    List<Appointment> findByClinic(Clinic clinic);
    List<Appointment> findByDoctor(User doctor);
    List<Appointment> findByPetOwner(User owner);
    List<Appointment> findByClinicAndStatus(Clinic Clinic, AppointmentStatus status);
    List<Appointment> findByDoctorAndStatus(User doctor, AppointmentStatus status);

    boolean existsByDoctorAndAppointmentDateTime(User doctor, LocalDateTime appointmentDateTime);
    boolean existsByDoctorAndAppointmentDateTimeAndIdNot(User doctor, LocalDateTime appointmentDateTime, Long id);


}
