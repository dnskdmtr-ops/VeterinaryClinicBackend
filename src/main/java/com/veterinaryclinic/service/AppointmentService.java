package com.veterinaryclinic.service;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import com.veterinaryclinic.dto.AppointmentRequest;
import com.veterinaryclinic.dto.RescheduleAppointmentRequest;
import com.veterinaryclinic.entity.Appointment;
import com.veterinaryclinic.entity.AppointmentStatus;
import com.veterinaryclinic.entity.Clinic;
import com.veterinaryclinic.entity.DoctorClinic;
import com.veterinaryclinic.entity.DoctorClinicStatus;
import com.veterinaryclinic.entity.Pet;
import com.veterinaryclinic.entity.Role;
import com.veterinaryclinic.entity.User;
import com.veterinaryclinic.repository.AppointmentRepository;
import com.veterinaryclinic.repository.ClinicRepository;
import com.veterinaryclinic.repository.DoctorClinicRepository;
import com.veterinaryclinic.repository.PetRepository;
import com.veterinaryclinic.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AppointmentService {
    private final AppointmentRepository  appointmentRepository;
    private final PetRepository petRepository;
    private final ClinicRepository clinicRepository;
    private final DoctorClinicRepository doctorClinicRepository;
    private final UserRepository userRepository;

    public Appointment createAppointment(AppointmentRequest request, User patient) {
        if (patient.getRole() != Role.PATIENT) {
            throw new IllegalArgumentException("Тільки пацієнт може створити запис на прийом");
        }
        
        Pet pet = petRepository.findById(request.getPetId()).orElseThrow(() -> new IllegalArgumentException("Тварину не знайдено"));

        if (!pet.getOwner().getId().equals(patient.getId())) {
            throw new IllegalArgumentException("Ця тварина не належить поточному користувачу");
        }

        Clinic clinic = clinicRepository.findById(request.getClinicId()).orElseThrow(() -> new IllegalArgumentException("Клініку не знайдено"));
        User doctor = userRepository.findById(request.getDoctorId()).orElseThrow(() -> new IllegalArgumentException("Лікаря не знайдено"));

        if (doctor.getRole() != Role.DOCTOR) {
            throw new IllegalArgumentException("Вказаний користувач не є лікарем");
        }

        DoctorClinic doctorClinic = doctorClinicRepository.findByDoctorAndClinic(doctor, clinic).orElseThrow(() -> new IllegalArgumentException("Лікар не працює  в цій клініці"));

        if (doctorClinic.getStatus() != DoctorClinicStatus.ACTIVE) {
            throw new IllegalArgumentException("Лікар не має активного доступу до цієї клініки");
        }

        if (!request.getAppointmentDateTime().isAfter(java.time.LocalDateTime.now())) {
            throw new IllegalArgumentException("Дата та час прийому повинні бути в майбутньому");
        }

        if (appointmentRepository.existsByDoctorAndAppointmentDateTime(doctor, request.getAppointmentDateTime())) {
            throw new IllegalArgumentException("Лікар вже має запис на цей час");
        }

        Appointment appointment = Appointment.builder()
                                             .pet(pet)
                                             .clinic(clinic)
                                             .doctor(doctor)
                                             .appointmentDateTime(request.getAppointmentDateTime()).reason(request.getReason())
                                                                                                   .notes(request.getNotes())
                                                                                                   .status(AppointmentStatus.PENDING)
                                                                                                   .createdAt(java.time.LocalDateTime.now())
                                                                                                   .build();
                                                                                    
        return appointmentRepository.save(appointment);
    }

    public java.util.List<Appointment> getMyAppointments(User patient) {
        if (patient.getRole() != Role.PATIENT) {
            throw new IllegalArgumentException("Тільки пацієнт може переглядати свої записи");
        }

        return appointmentRepository.findByPetOwner(patient);
    }

    public java.util.List<Appointment> getDoctorAppointments(User doctor) {
        if (doctor.getRole() != Role.DOCTOR) {
            throw new IllegalArgumentException("Тільки лікар може переглядати свої записи");
        }

        return appointmentRepository.findByDoctor(doctor);
    }

    public Appointment confirmAppointment(Long appointmentId, User doctor) {
        if (doctor.getRole() != Role.DOCTOR) {
            throw new IllegalArgumentException("Тільки лікар може підтверджувати прийом");
        }

        Appointment appointment = appointmentRepository.findById(appointmentId).orElseThrow(() -> new IllegalArgumentException("Запис на прийом не знайдено"));

        if (!appointment.getDoctor().getId().equals(doctor.getId())) {
            throw new IllegalArgumentException("Цей прийом не належить даному лікарю");
        }

        if (appointment.getStatus() != AppointmentStatus.PENDING) {
            throw new IllegalArgumentException("Підтвердити можна тільки очікуваний запис");
        }

        appointment.setStatus(AppointmentStatus.CONFIRMED);

        return appointmentRepository.save(appointment);
    }

    public Appointment completeAppointment(Long appointmentId, User doctor) {
        if (doctor.getRole() != Role.DOCTOR) {
            throw new IllegalArgumentException("Тільки лікар може завершити прийом");
        }

        Appointment appointment = appointmentRepository.findById(appointmentId).orElseThrow(() -> new IllegalArgumentException("Запис на прийом не знайдено"));

        if (!appointment.getDoctor().getId().equals(doctor.getId())) {
            throw new IllegalArgumentException("Цей прийом не належить даному лікарю");
        }

        if (appointment.getStatus() != AppointmentStatus.CONFIRMED) {
            throw new IllegalArgumentException("Завершити можна тільки підтверджений прийом");
        }

        appointment.setStatus(AppointmentStatus.COMPLETED);

        return appointmentRepository.save(appointment);
    }

    public Appointment cancelAppointment(Long appointmentId, User doctor) {
        if (doctor.getRole() != Role.DOCTOR) {
            throw new IllegalArgumentException("Тільки лікар може скасувати прийом");
        }

        Appointment appointment = appointmentRepository.findById(appointmentId).orElseThrow(() -> new IllegalArgumentException("Запис на прийом не знайдено"));

        if (!appointment.getDoctor().getId().equals(doctor.getId())) {
            throw new IllegalArgumentException("Цей прийом не належить даному лікарю");
        }

        if (appointment.getStatus() == AppointmentStatus.COMPLETED) {
            throw new IllegalArgumentException("Завершений прийом не можна скасувати");
        }

        if (appointment.getStatus() == AppointmentStatus.CANCELLED) {
            throw new IllegalArgumentException("Прийом вже скасований");
        }

        appointment.setStatus(AppointmentStatus.CANCELLED);

        return appointmentRepository.save(appointment);
    }

    public Appointment rescheduleAppointment(Long appointmentId, RescheduleAppointmentRequest request, User user) {
        Appointment appointment = appointmentRepository.findById(appointmentId).orElseThrow(() -> new IllegalArgumentException("Запис на прийом не знайдено"));

        if (user.getRole() == Role.PATIENT) {
            if (!appointment.getPet().getOwner().getId().equals(user.getId())) {
                throw new IllegalArgumentException("Цей прийом не належить поточному користувачу");
            }
        } else if (user.getRole() == Role.DOCTOR) {
            if (!appointment.getDoctor().getId().equals(user.getId())) {
                throw new IllegalArgumentException("Цей прийом не належить даному лікарю");
            }
        } else {
            throw new IllegalArgumentException("Переносити прийом може тільки пацієнт або лікар");
        }

        if (appointment.getStatus() == AppointmentStatus.COMPLETED) {
            throw new IllegalArgumentException("Завершений прийом не можна переносити. Створіть новий прийом");
        }

        if (appointment.getStatus() == AppointmentStatus.CANCELLED) {
            throw new IllegalArgumentException("Скасований прийом не можна перенести. Створіть новий прийом");
        }

        LocalDateTime newDateTime = request.getAppointmentDateTime();

        if (!newDateTime.isAfter(LocalDateTime.now())) {
            throw new IllegalArgumentException("Нова дата та час повинні бути в майбутньому");
        }

        if (appointmentRepository.existsByDoctorAndAppointmentDateTimeAndIdNot(appointment.getDoctor(), newDateTime, appointment.getId())) {
            throw new IllegalArgumentException("Лікар вже має запис на цей час");
        }

        appointment.setAppointmentDateTime(newDateTime);

        return appointmentRepository.save(appointment);
    }
}