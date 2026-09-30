package com.veterinaryclinic.dto;

import com.veterinaryclinic.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data 
@AllArgsConstructor 
public class AuthResponse {
    private  Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private Role role;
    private String token;
}
