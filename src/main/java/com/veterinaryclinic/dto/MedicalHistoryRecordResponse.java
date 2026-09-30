package com.veterinaryclinic.dto;

import java.time.LocalDateTime;

public record MedicalHistoryRecordResponse(
    Long id,
    Long appointmentId,
    LocalDateTime appointmentDateTime,

    Long petId,
    String petName,

    Long clinicId,
    String clinicName,

    Long doctorId,
    String doctorFirstName,
    String doctorLastName,

    String complaints,
    String symptoms,
    String examinationResults,
    String diagnosis,
    String examinations,
    String treatment,
    String recommendations,
    String notes,

    LocalDateTime createdAt
) {}
