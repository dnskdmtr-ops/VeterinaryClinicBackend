package com.veterinaryclinic.service;

import com.veterinaryclinic.dto.ClinicRequest;
import com.veterinaryclinic.entity.Clinic;
import com.veterinaryclinic.entity.Role;
import com.veterinaryclinic.entity.User;
import com.veterinaryclinic.repository.ClinicRepository;
import com.veterinaryclinic.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor 
public class ClinicService {
    private final ClinicRepository clinicRepository;
    private final UserRepository userRepository;

    public Clinic createClinic(ClinicRequest request, User admin) {
        if (admin.getRole() != Role.CLINIC_ADMIN) {
            throw new IllegalArgumentException("Тільки адміністратор клініки може створити клініку");
        }

        if (admin.getClinic() != null) {
            throw new IllegalArgumentException("Адміністратор вже має клініку");
        }

        Clinic clinic = Clinic.builder().name(request.getName())
                                        .address(request.getAddress())
                                        .clinicNumber(request.getClinicNumber())
                                        .phone(request.getPhone())
                                        .email(request.getEmail())
                                        .build();

        Clinic savedClinic = clinicRepository.save(clinic);
        admin.setClinic(savedClinic);
        userRepository.save(admin);

        return savedClinic;
    }
}
