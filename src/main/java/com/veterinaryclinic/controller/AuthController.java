package com.veterinaryclinic.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.veterinaryclinic.dto.LoginRequest;
import com.veterinaryclinic.dto.RegisterRequest;
import com.veterinaryclinic.dto.UserResponse;
import com.veterinaryclinic.entity.User;
import com.veterinaryclinic.service.AuthService;
import com.veterinaryclinic.dto.AuthResponse;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;



@RestController 
@RequestMapping("/api/auth")
@RequiredArgsConstructor 
public class AuthController {
    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<UserResponse> register(@Valid @RequestBody RegisterRequest request) {
        User user = authService.register(request);
        UserResponse response = new UserResponse(
            user.getId(), 
            user.getFirstName(), 
            user.getLastName(), 
            user.getEmail(), 
            user.getPhone(), 
            user.getRole()
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        User user = authService.login(request);
        String token = authService.generateToken(user);
        
        AuthResponse response = new AuthResponse(
            user.getId(), 
            user.getFirstName(), 
            user.getLastName(), 
            user.getEmail(), 
            user.getPhone(), 
            user.getRole(),
            token
        );
        
        return ResponseEntity.ok(response);
    }
    
    
}
