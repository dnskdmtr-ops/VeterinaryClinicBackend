package com.veterinaryclinic.controller;

import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.veterinaryclinic.dto.MedicationPrescriptionRequest;
import com.veterinaryclinic.dto.MedicationPrescriptionResponse;
import com.veterinaryclinic.entity.MedicationPrescription;
import com.veterinaryclinic.entity.User;
import com.veterinaryclinic.service.MedicationPrescriptionService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;


@RestController 
@RequestMapping("/api/medications")
@RequiredArgsConstructor 
public class MedicationPrescriptionController {
    private final MedicationPrescriptionService prescriptionService;

    private MedicationPrescriptionResponse toResponse(MedicationPrescription prescription) {
        Long previousId = prescription.getPreviousPrescription() != null
                                                                 ? prescription.getPreviousPrescription().getId()
                                                                 : null;
        return new MedicationPrescriptionResponse(
            prescription.getId(), 
            prescription.getMedicalRecord().getId(), 
            previousId, 
            prescription.getMedicationName(), 
            prescription.getDosage(), 
            prescription.getFrequency(), 
            prescription.getAdministrationTimes(), 
            prescription.getDuration(), 
            prescription.getStartDate(), 
            prescription.getEndDate(), 
            prescription.getInstructions(), 
            prescription.getStatus(), 
            prescription.getCreatedAt()
        );
    }

    @PostMapping
    public MedicationPrescriptionResponse createPrescription(@Valid @RequestBody MedicationPrescriptionRequest request, Authentication authentication) {
        User doctor = (User) authentication.getPrincipal();
        MedicationPrescription prescription = prescriptionService.createPrescription(request, doctor);

        return toResponse(prescription);
    }

    @GetMapping("/pet/{petId}")
    public List<MedicationPrescriptionResponse> getPetPrescriptions(@PathVariable Long petId, Authentication authentication) {
        User doctor = (User) authentication.getPrincipal();
        List<MedicationPrescription> prescriptions = prescriptionService.getPetPrescriptions(petId, doctor);
        List<MedicationPrescriptionResponse> response = prescriptions.stream()
                                                                     .map(this::toResponse)
                                                                     .toList();

        return response;
    }

    @PutMapping("/{prescriptionId}/complete")
    public MedicationPrescriptionResponse completePrescription(@PathVariable Long prescriptionId, Authentication authentication) {
        User doctor = (User) authentication.getPrincipal();
        MedicationPrescription prescription = prescriptionService.completePrescription(prescriptionId, doctor);
        
        return toResponse(prescription);
    }

    @PutMapping("/{prescriptionId}/cancel")
    public MedicationPrescriptionResponse cancelPrescription(@PathVariable Long prescriptionId, Authentication authentication) {
        User doctor = (User) authentication.getPrincipal();
        MedicationPrescription prescription = prescriptionService.cancelPrescription(prescriptionId, doctor);
        
        return toResponse(prescription);
    }
}