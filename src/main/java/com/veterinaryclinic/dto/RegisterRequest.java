package com.veterinaryclinic.dto;

import com.veterinaryclinic.entity.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data 
public class RegisterRequest {
    @NotBlank(message = "Ім'я є обов'язковим")
    private String firstName;

    @NotBlank(message = "Прізвище є обов'язковим")
    private String lastName;

    @Email(message = "Некоректний email")
    @NotBlank(message = "Email є обов'язковим")
    private String email;

    @NotBlank(message = "Пароль є обов'язковим")
    @Size(min = 6, message = "Пароль повинен мічтити щонайменше 6 символів")
    private String password;
    private String phone;
    private Role role;
}
