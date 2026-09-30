package com.veterinaryclinic.controller;

import com.veterinaryclinic.dto.MedicalObservationRequest;
import com.veterinaryclinic.dto.MedicalObservationResponse;
import com.veterinaryclinic.entity.MedicalObservation;
import com.veterinaryclinic.entity.User;
import com.veterinaryclinic.service.MedicalObservationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.List;



@RestController 
@RequestMapping("/api/medical-observations")
@RequiredArgsConstructor 
public class MedicalObservationController {
    private final MedicalObservationService medicalObservationService;

    private MedicalObservationResponse toResponse(MedicalObservation observation) {
        return new MedicalObservationResponse(
            observation.getId(), 
            observation.getMedicalRecord().getId(), 
            observation.getCondition(), 
            observation.getSymptoms(), 
            observation.getObservation(), 
            observation.getNotes(), 
            observation.getCreatedAt()
        );
    }

    @PostMapping
    public MedicalObservationResponse createObservation(@Valid @RequestBody MedicalObservationRequest request, Authentication authentication) {
        User doctor = (User) authentication.getPrincipal();
        MedicalObservation observation = medicalObservationService.createObservation(request, doctor);
        
        return toResponse(observation);
    }

    @GetMapping("/record/{medicalRecordId}")
    public List<MedicalObservationResponse> getObservations(@PathVariable Long medicalRecordId, Authentication authentication) {
        User doctor = (User) authentication.getPrincipal();
        List<MedicalObservation> observation = medicalObservationService.getObservations(medicalRecordId, doctor);
        List<MedicalObservationResponse> response = observation.stream()
                                                               .map(this::toResponse)
                                                               .toList();
       
        return response;
    }
    
    
}
