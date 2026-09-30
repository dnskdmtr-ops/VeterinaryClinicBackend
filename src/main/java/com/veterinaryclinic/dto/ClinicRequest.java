package com.veterinaryclinic.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Email;
import lombok.Data;

@Data 
public class ClinicRequest {
    @NotBlank(message = "Назва клініки є обов'язковою")
    private String name;

    @NotBlank(message = "Адреса є обов'язковою")
    private String address;
    private String clinicNumber;
    private String phone;

    @Email(message = "Некоректний email")
    private String email;
}
