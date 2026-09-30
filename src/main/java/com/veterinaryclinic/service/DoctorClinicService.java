package com.veterinaryclinic.service;

import org.springframework.stereotype.Service;

import com.veterinaryclinic.entity.Clinic;
import com.veterinaryclinic.entity.DoctorClinic;
import com.veterinaryclinic.entity.DoctorClinicStatus;
import com.veterinaryclinic.entity.Role;
import com.veterinaryclinic.entity.User;
import com.veterinaryclinic.repository.DoctorClinicRepository;
import com.veterinaryclinic.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DoctorClinicService {
    private final DoctorClinicRepository doctorClinicRepository;
    private final UserRepository userRepository;

    public DoctorClinic addDoctor(Long doctorId, User admin) {
        if (admin.getRole() != Role.CLINIC_ADMIN) {
            throw new IllegalArgumentException("Тільки адміністратор клініки може додавати лікарів");
        }

        if (admin.getClinic() == null) {
            throw new IllegalArgumentException("Адміністратор не має клініки");
        }

        User doctor = userRepository.findById(doctorId).orElseThrow(() -> new IllegalArgumentException("Лікаря не знайдено"));

        if (doctor.getRole() != Role.DOCTOR) {
            throw new IllegalArgumentException("Вказаний користувач не є лікарем");
        }

        Clinic clinic = admin.getClinic();

        if (doctorClinicRepository.findByDoctorAndClinic(doctor, clinic).isPresent()) {
            throw new IllegalArgumentException("Цей лікар вже пов'язаний з клінікою");
        }

        DoctorClinic doctorClinic = DoctorClinic.builder()
                                                .doctor(doctor)
                                                .clinic(clinic)
                                                .status(DoctorClinicStatus.PENDING)
                                                .createdAt(java.time.LocalDateTime.now())
                                                .build();

        return doctorClinicRepository.save(doctorClinic);
    }

    public java.util.List<DoctorClinic> getDoctors(User admin) {
        if (admin.getRole() != Role.CLINIC_ADMIN) {
            throw new IllegalArgumentException("Тільки адміністратор клініки може переглядати лікарів");
        }

        if (admin.getClinic() == null) {
            throw new IllegalArgumentException("Адміністратор не має клініки");
        }

        return doctorClinicRepository.findByClinic(admin.getClinic());
    }

    public DoctorClinic activateDoctor(Long doctorClinicId, User admin) {
        if (admin.getRole() != Role.CLINIC_ADMIN) {
            throw new IllegalArgumentException("Тільки адміністратор клініки може підтверджувати лікарів");
        }

        if (admin.getClinic() == null) {
            throw new IllegalArgumentException("Адміністратор не має клініки");
        }

        DoctorClinic doctorClinic = doctorClinicRepository.findById(doctorClinicId).orElseThrow(() -> new IllegalArgumentException("Зв'язок лікаря з клінікою не знайдено"));

        if (!doctorClinic.getClinic().getId().equals(admin.getClinic().getId())) {
            throw new IllegalArgumentException("Цей лікар належить до іншої клініки");
        }

        if (doctorClinic.getStatus() != DoctorClinicStatus.PENDING) {
            throw new IllegalArgumentException("Лікар не має статусу PENDING");
        }

        doctorClinic.setStatus(DoctorClinicStatus.ACTIVE);

        return doctorClinicRepository.save(doctorClinic);
    }

    public DoctorClinic removeDoctor(Long doctorClinicId, User admin) {
        if (admin.getRole() != Role.CLINIC_ADMIN) {
            throw new IllegalArgumentException("Тільки адміністратор клініки може відключити лікарів");
        }

        if (admin.getClinic() == null) {
            throw new IllegalArgumentException("Адміністратор не має клініки");
        }

        DoctorClinic doctorClinic = doctorClinicRepository.findById(doctorClinicId).orElseThrow(() -> new IllegalArgumentException("Зв'язок лікаря з клінікою не знайдено"));

        if (!doctorClinic.getClinic().getId().equals(admin.getClinic().getId())) {
            throw new IllegalArgumentException("Цей лікар належить до іншої клініки");
        }

        if (doctorClinic.getStatus() != DoctorClinicStatus.ACTIVE) {
            throw new IllegalArgumentException("Можна відключити тільки активного лікаря");
        }

        doctorClinic.setStatus(DoctorClinicStatus.REMOVED);

        return doctorClinicRepository.save(doctorClinic);
    }

    public DoctorClinic restoreDoctor(Long doctorClinicId, User admin) {
        if (admin.getRole() != Role.CLINIC_ADMIN) {
            throw new IllegalArgumentException("Тільки адміністратор клініки може відновлювати лікарів");
        }

        if (admin.getClinic() == null) {
            throw new IllegalArgumentException("Адміністратор не має клініки");
        }

        DoctorClinic doctorClinic = doctorClinicRepository.findById(doctorClinicId).orElseThrow(() -> new IllegalArgumentException("Зв'язок лікаря з клінікою не знайдено"));

        if (!doctorClinic.getClinic().getId().equals(admin.getClinic().getId())) {
            throw new IllegalArgumentException("Цей лікар належить до іншої клініки");
        }

        if (doctorClinic.getStatus() != DoctorClinicStatus.REMOVED) {
            throw new IllegalArgumentException("Можна відновити лікаря зі статусом REMOVED");
        }

        doctorClinic.setStatus(DoctorClinicStatus.ACTIVE);

        return doctorClinicRepository.save(doctorClinic);
    }
}