package com.veterinaryclinic.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import com.veterinaryclinic.dto.DoctorClinicResponse;
import com.veterinaryclinic.entity.DoctorClinic;
import com.veterinaryclinic.entity.User;
import com.veterinaryclinic.service.DoctorClinicService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/clinic-admin/doctors")
@RequiredArgsConstructor 
public class DoctorClinicController {
    private final DoctorClinicService doctorClinicService;

    private DoctorClinicResponse toResponse(DoctorClinic doctorClinic) {
        return new DoctorClinicResponse(
            doctorClinic.getId(),
            doctorClinic.getDoctor().getId(), 
            doctorClinic.getDoctor().getFirstName(), 
            doctorClinic.getDoctor().getLastName(), 
            doctorClinic.getDoctor().getEmail(), 
            doctorClinic.getClinic().getId(), 
            doctorClinic.getStatus(), 
            doctorClinic.getCreatedAt()
        );
    }

    @PostMapping("/{doctorId}")
    public ResponseEntity<DoctorClinicResponse> addDoctor(@PathVariable Long doctorId, Authentication authentication) {
        User admin = (User) authentication.getPrincipal();
        DoctorClinic doctorClinic = doctorClinicService.addDoctor(doctorId, admin);
    
        return ResponseEntity.status(HttpStatus.CREATED).body(toResponse(doctorClinic));
    }

    @GetMapping
    public ResponseEntity<java.util.List<DoctorClinicResponse>> getDoctors(Authentication authentication) {
        User admin = (User) authentication.getPrincipal();
        var doctorClinics = doctorClinicService.getDoctors(admin);
        
        var response = doctorClinics.stream()
                                    .map(this::toResponse)
                                    .toList();

        return ResponseEntity.ok(response);
    }
    
    @PutMapping("/{doctorClinicId}/activate")
    public ResponseEntity<DoctorClinicResponse> activateDoctor(@PathVariable Long doctorClinicId, Authentication authentication) {
        User admin = (User) authentication.getPrincipal();
        DoctorClinic doctorClinic = doctorClinicService.activateDoctor(doctorClinicId, admin);

        return ResponseEntity.ok(toResponse(doctorClinic));
    }

    @PutMapping("/{doctorClinicId}/remove")
    public ResponseEntity<DoctorClinicResponse> removeDoctor(@PathVariable Long doctorClinicId, Authentication authentication) {
        User admin = (User) authentication.getPrincipal();
        DoctorClinic doctorClinic = doctorClinicService.removeDoctor(doctorClinicId, admin);

        return ResponseEntity.ok(toResponse(doctorClinic));
    }

    @PutMapping("/{doctorClinicId}/restore")
    public ResponseEntity<DoctorClinicResponse> restoreDoctor(@PathVariable Long doctorClinicId, Authentication authentication) {
        User admin = (User) authentication.getPrincipal();
        DoctorClinic doctorClinic = doctorClinicService.restoreDoctor(doctorClinicId, admin);

        return ResponseEntity.ok(toResponse(doctorClinic));
    }
}