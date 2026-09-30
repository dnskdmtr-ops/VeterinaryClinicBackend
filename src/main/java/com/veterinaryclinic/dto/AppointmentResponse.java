package com.veterinaryclinic.dto;

import java.time.LocalDateTime;

import com.veterinaryclinic.entity.AppointmentStatus;

import lombok.AllArgsConstructor;
import lombok.Data;


@Data
@AllArgsConstructor 
public class AppointmentResponse {
    private Long id;

    private Long petId;
    private String petName;

    private Long  clinicId;
    private String clinicName;

    private Long doctorId;
    private String doctorFirstName;
    private String doctorLastName;

    private LocalDateTime appointmentDateTime;
    private String reason;
    private AppointmentStatus status;
    private String notes;
    private LocalDateTime createdAt;
}
