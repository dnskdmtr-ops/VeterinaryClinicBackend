package com.veterinaryclinic.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.veterinaryclinic.security.JwtAuthenticationFilter;

import lombok.RequiredArgsConstructor;

@Configuration 
@RequiredArgsConstructor 
public class SecurityConfig {
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean 
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean 
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable())
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth.requestMatchers("/api/auth/**").permitAll()
                                               .requestMatchers("/api/patient/**").hasRole("PATIENT")
                                               .requestMatchers("/api/pets/**").hasRole("PATIENT")
                                               .requestMatchers("/api/reminders/").hasRole("PATIENT")
                                               .requestMatchers("/api/doctor/**").hasRole("DOCTOR")
                                               .requestMatchers("/api/medical-observations/**").hasRole("DOCTOR")
                                               .requestMatchers("/api/medications/**").hasRole("DOCTOR")
                                               .requestMatchers("/api/clinic-admin/**").hasRole("CLINIC_ADMIN")
                                               .requestMatchers("/api/medical-records/**").hasAnyRole("PATIENT", "DOCTOR", "CLINIC_ADMIN")
                                               .requestMatchers("/api/appointments/**").hasAnyRole("PATIENT", "DOCTOR", "CLINIC_ADMIN")
                                               .anyRequest().authenticated())
                                               
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        
        return http.build();
    }
}
