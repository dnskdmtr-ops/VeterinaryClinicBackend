package com.veterinaryclinic.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.veterinaryclinic.dto.LoginRequest;
import com.veterinaryclinic.dto.RegisterRequest;
import com.veterinaryclinic.entity.Role;
import com.veterinaryclinic.entity.User;
import com.veterinaryclinic.repository.UserRepository;
import com.veterinaryclinic.security.JwtService;

import lombok.RequiredArgsConstructor;

@Service 
@RequiredArgsConstructor 
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public User register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new  IllegalArgumentException("Користувач з таким email вже існує");
        }

        User user = User.builder().firstName(request.getFirstName())
                                  .lastName(request.getLastName())
                                  .email(request.getEmail())
                                  .password(passwordEncoder.encode(request.getPassword()))
                                  .phone(request.getPhone())
                                  .role(Role.PATIENT)
                                  .build();

        return  userRepository.save(user);
    }

    public User login(LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                                  .orElseThrow(() -> new IllegalArgumentException("Невірний email або пароль"));
        
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new IllegalArgumentException("Невірний email або пароль");
        }

        return user;
    }

    public String generateToken(User user) {
        return jwtService.generateToken(user);
    }
}
