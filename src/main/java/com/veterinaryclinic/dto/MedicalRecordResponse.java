package com.veterinaryclinic.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor 
public class MedicalRecordResponse {
    private Long id;
    
    private Long appointmentId;

    private Long petId;
    private String petName;

    private Long clinicId;
    private String clinicName;

    private Long doctorId;
    private String doctorFirstName;
    private String doctorLastName;

    private String complaints;
    private String symptoms;
    private String examinationResults;
    private String diagnosis;
    private String examinations;
    private String treatment;
    private String recommendations;
    private String notes;
    private LocalDateTime createdAt;
}
