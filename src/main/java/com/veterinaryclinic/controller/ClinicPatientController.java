package com.veterinaryclinic.controller;

import com.veterinaryclinic.dto.ClinicPatientRequest;
import com.veterinaryclinic.dto.ClinicPatientResponse;
import com.veterinaryclinic.entity.ClinicPatient;
import com.veterinaryclinic.entity.User;
import com.veterinaryclinic.service.ClinicPatientService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;





@RestController
@RequestMapping("/api/clinic-admin/patients")
@RequiredArgsConstructor 
public class ClinicPatientController {
    private final ClinicPatientService clinicPatientService;

    private ClinicPatientResponse toResponse(ClinicPatient clinicPatient) {
        return new ClinicPatientResponse(
            clinicPatient.getId(), 
            clinicPatient.getPet().getId(), 
            clinicPatient.getPet().getName(), 
            clinicPatient.getPet().getSpecies(), 
            clinicPatient.getPet().getBreed(), 
            clinicPatient.getClinic().getId(), 
            clinicPatient.getStatus(), 
            clinicPatient.getCreatedAt(),
            clinicPatient.getArchivedAt(),
            clinicPatient.getArchiveReason()
        );
    }

    @PostMapping
    public ResponseEntity<ClinicPatientResponse> addPatient(@Valid @RequestBody ClinicPatientRequest request, Authentication authentication) {
        User admin = (User) authentication.getPrincipal();
        ClinicPatient clinicPatient = clinicPatientService.addPatient(request, admin);

        return ResponseEntity.status(HttpStatus.CREATED).body(toResponse(clinicPatient));
    }

    @GetMapping
    public ResponseEntity<java.util.List<ClinicPatientResponse>> getActivePatients(Authentication authentication) {
        User admin = (User) authentication.getPrincipal();
        var patients = clinicPatientService.getActivePatients(admin);

        var response = patients.stream()
                                .map(this::toResponse)
                                .toList();


        return ResponseEntity.ok(response);
    }

    @PutMapping("/{patientId}/archive")
    public ResponseEntity<ClinicPatientResponse> archivePatient(@PathVariable Long patientId, @RequestParam(required = false) String reason, Authentication authentication) {
        User admin = (User) authentication.getPrincipal();
        ClinicPatient clinicPatient = clinicPatientService.archivePatient(patientId, reason, admin);

        return ResponseEntity.ok(toResponse(clinicPatient));
    }

    @GetMapping("/archive")
    public ResponseEntity<java.util.List<ClinicPatientResponse>> getArchivedPatients(Authentication authentication) {
        User admin = (User) authentication.getPrincipal();
        var patients = clinicPatientService.getArchivedPatients(admin);

        var response = patients.stream()
                                .map(this::toResponse)
                                .toList();


        return ResponseEntity.ok(response);
    }

    @PutMapping("/{patientId}/restore")
    public ResponseEntity<ClinicPatientResponse> restorePatient(@PathVariable Long patientId, Authentication authentication) {
        User admin = (User) authentication.getPrincipal();
        ClinicPatient clinicPatient = clinicPatientService.restorePatient(patientId, admin);

        return ResponseEntity.ok(toResponse(clinicPatient));
    }
}
