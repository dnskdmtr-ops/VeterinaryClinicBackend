package com.veterinaryclinic.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.veterinaryclinic.dto.AppointmentRequest;
import com.veterinaryclinic.dto.AppointmentResponse;
import com.veterinaryclinic.dto.RescheduleAppointmentRequest;
import com.veterinaryclinic.entity.Appointment;
import com.veterinaryclinic.entity.User;
import com.veterinaryclinic.service.AppointmentService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/appointments")
@RequiredArgsConstructor
public class AppointmentController {
    private final AppointmentService appointmentService;

    private AppointmentResponse toResponse(Appointment appointment) {
        return new AppointmentResponse(
            appointment.getId(), 
            appointment.getPet().getId(), 
            appointment.getPet().getName(), 
            appointment.getClinic().getId(), 
            appointment.getClinic().getName(), 
            appointment.getDoctor().getId(), 
            appointment.getDoctor().getFirstName(),
            appointment.getDoctor().getLastName(), 
            appointment.getAppointmentDateTime(), 
            appointment.getReason(), 
            appointment.getStatus(), 
            appointment.getNotes(), 
            appointment.getCreatedAt()
        );
    }

    @PostMapping
    public ResponseEntity<AppointmentResponse> createAppointment(@Valid @RequestBody AppointmentRequest request, Authentication authentication) {
        User patient = (User) authentication.getPrincipal();
        Appointment appointment = appointmentService.createAppointment(request, patient);

        return ResponseEntity.status(HttpStatus.CREATED).body(toResponse(appointment));
    }

    @GetMapping
    public ResponseEntity<java.util.List<AppointmentResponse>> getMyAppointments(Authentication authentication) {
        User patient = (User) authentication.getPrincipal();
        var appointments = appointmentService.getMyAppointments(patient);

        var response = appointments.stream()
                                   .map(this::toResponse)
                                   .toList();

        return ResponseEntity.ok(response);
    }

    @GetMapping("/doctor")
    public ResponseEntity<java.util.List<AppointmentResponse>> getDoctorAppointments(Authentication authentication) {
        User doctor = (User) authentication.getPrincipal();
        var appointments = appointmentService.getDoctorAppointments(doctor);

        var response = appointments.stream()
                                   .map(this::toResponse)
                                   .toList();

        return ResponseEntity.ok(response);
    }

    @PutMapping("/{appointmentId}/confirm")
    public ResponseEntity<AppointmentResponse> confirmAppointment(@PathVariable Long appointmentId, Authentication authentication) {
        User doctor = (User) authentication.getPrincipal();
        Appointment appointment = appointmentService.confirmAppointment(appointmentId, doctor);

        return ResponseEntity.ok(toResponse(appointment));
    }

    @PutMapping("/{appointmentId}/complete")
    public ResponseEntity<AppointmentResponse> completeAppointment(@PathVariable Long appointmentId, Authentication authentication) {
        User doctor = (User) authentication.getPrincipal();
        Appointment appointment = appointmentService.completeAppointment(appointmentId, doctor);

        return ResponseEntity.ok(toResponse(appointment));
    }

    @PutMapping("/{appointmentId}/cancel")
    public ResponseEntity<AppointmentResponse> cancelAppointment(@PathVariable Long appointmentId, Authentication authentication) {
        User doctor = (User) authentication.getPrincipal();
        Appointment appointment = appointmentService.cancelAppointment(appointmentId, doctor);

        return ResponseEntity.ok(toResponse(appointment));
    }

    @PutMapping("/{appointmentId}/reschedule")
    public AppointmentResponse rescheduleAppointment(@PathVariable Long appointmentId, @Valid @RequestBody RescheduleAppointmentRequest request, Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        Appointment appointment = appointmentService.rescheduleAppointment(appointmentId, request, user);
        
        return toResponse(appointment);
    }
}