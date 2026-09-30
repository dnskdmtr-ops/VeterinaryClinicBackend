package com.veterinaryclinic.dto;

import com.veterinaryclinic.entity.ClinicPatientStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor 
public class ClinicPatientResponse {
    private Long id;
    private Long petId;
    private String petName;
    private String species;
    private String breed;
    private Long clinicId;

    private ClinicPatientStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime archivedAt;

    private String archiveReason;
}
