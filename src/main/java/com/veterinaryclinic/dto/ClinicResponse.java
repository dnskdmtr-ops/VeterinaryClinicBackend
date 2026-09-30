package com.veterinaryclinic.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor 
public class ClinicResponse {
    private Long id;
    private String name;
    private String address;
    private String clinicNumber;
    private String phone;
    private String email;
}
