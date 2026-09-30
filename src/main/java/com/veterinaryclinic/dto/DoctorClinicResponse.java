package com.veterinaryclinic.dto;

import com.veterinaryclinic.entity.DoctorClinicStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor 
public class DoctorClinicResponse {
    private Long id;
    private Long doctorId;
    private String doctorFirstName;
    private String doctorLastName;
    private String doctorEmail;
    private Long clinicId;
    private DoctorClinicStatus status;
    private LocalDateTime createdAt;
}
