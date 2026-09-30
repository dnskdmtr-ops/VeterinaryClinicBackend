package com.veterinaryclinic.controller;

import com.veterinaryclinic.dto.ClinicRequest;
import com.veterinaryclinic.dto.ClinicResponse;
import com.veterinaryclinic.dto.UserResponse;
import com.veterinaryclinic.entity.Clinic;
import com.veterinaryclinic.entity.User;
import com.veterinaryclinic.service.ClinicService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController 
@RequestMapping("/api/clinic-admin")
@RequiredArgsConstructor 
public class ClinicAdminController {
    private final ClinicService clinicService;

    @GetMapping("/profile")
    public UserResponse profile(Authentication authentication) {
        User user = (User) authentication.getPrincipal();

        return new UserResponse(
            user.getId(),
            user.getFirstName(),
            user.getLastName(),
            user.getEmail(),
            user.getPhone(),
            user.getRole()
        );
    }

    @PostMapping("/clinic")
    public ResponseEntity<ClinicResponse> createClinic(
        @Valid @RequestBody 
        ClinicRequest request, Authentication authentication) {
            User admin = (User) authentication.getPrincipal();
            Clinic clinic = clinicService.createClinic(request, admin);
            ClinicResponse response = new ClinicResponse(
                clinic.getId(),
                clinic.getName(),
                clinic.getAddress(),
                clinic.getClinicNumber(),
                clinic.getPhone(),
                clinic.getEmail()
            );

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}