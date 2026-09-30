package com.veterinaryclinic.controller;

import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.veterinaryclinic.dto.MedicalHistoryResponse;
import com.veterinaryclinic.dto.MedicalRecordRequest;
import com.veterinaryclinic.dto.MedicalRecordResponse;
import com.veterinaryclinic.entity.MedicalRecord;
import com.veterinaryclinic.entity.User;
import com.veterinaryclinic.service.MedicalRecordService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController 
@RequestMapping("/api/medical-records")
@RequiredArgsConstructor 
public class MedicalRecordController {
    private final MedicalRecordService medicalRecordService;

     private MedicalRecordResponse toResponse(MedicalRecord record) {
        return new MedicalRecordResponse(record.getId(),
                                         record.getAppointment().getId(),
                                         record.getPet().getId(),
                                         record.getPet().getName(),
                                         record.getClinic().getId(),
                                         record.getClinic().getName(),
                                         record.getDoctor().getId(),
                                         record.getDoctor().getFirstName(),
                                         record.getDoctor().getLastName(),
                                         record.getComplaints(),
                                         record.getSymptoms(),
                                         record.getExaminationResults(),
                                         record.getDiagnosis(),
                                         record.getExamination(),
                                         record.getTreatment(),
                                         record.getRecommendations(),
                                         record.getNotes(),
                                         record.getCreatedAt()
        );
    }
    
    @PostMapping
    public MedicalRecordResponse createMedicalRecord(@Valid @RequestBody MedicalRecordRequest request, Authentication authentication) {
        User doctor = (User) authentication.getPrincipal();
        MedicalRecord record = medicalRecordService.createMedicalRecord(request, doctor);

        return toResponse(record);
    }

    @GetMapping("/doctor")
    public List<MedicalRecordResponse> getMyRecords(Authentication authentication) {
        User doctor = (User) authentication.getPrincipal();

        return medicalRecordService.getMyRecords(doctor).stream()
                                                        .map(this::toResponse)
                                                        .toList();
    }

    @GetMapping("/pet/{petId}")
    public List<MedicalRecordResponse> getPetMedicalHistory(@PathVariable Long petId, Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        List<MedicalRecord> records = medicalRecordService.getPetMedicalHistory(petId, user);
        List<MedicalRecordResponse> response = records.stream()
                                                      .map(this::toResponse)
                                                      .toList();

        return response;
    }

    @GetMapping("/pet/{petId}/full")
    public List<MedicalHistoryResponse> getFullMedicationHistory(@PathVariable Long petId, Authentication authentication) {
        User user = (User) authentication.getPrincipal();

        return medicalRecordService.getFullMedicalHistory(petId, user);
    }
}
