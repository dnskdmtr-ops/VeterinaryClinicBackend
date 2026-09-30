package com.veterinaryclinic.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data 
public class LoginRequest {
    @Email(message = "Некоректний email")
    @NotBlank(message = "Email є обов'язковим")
    private  String email;

    @NotBlank(message = "Пароль є обов'язковим")
    private String password;
}
