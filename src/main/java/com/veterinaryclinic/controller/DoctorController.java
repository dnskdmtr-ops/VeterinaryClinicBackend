package com.veterinaryclinic.controller;

import com.veterinaryclinic.dto.UserResponse;
import com.veterinaryclinic.entity.User;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;


@RestController 
@RequestMapping("/api/doctor")
public class DoctorController {
    @GetMapping("/profile")
    public UserResponse profile(Authentication authentication) {
        User user = (User) authentication.getPrincipal();

        return new UserResponse(
            user.getId(),
            user.getFirstName(), 
            user.getLastName(), 
            user.getEmail(), 
            user.getPhone(), 
            user.getRole()
        );
    }
}
