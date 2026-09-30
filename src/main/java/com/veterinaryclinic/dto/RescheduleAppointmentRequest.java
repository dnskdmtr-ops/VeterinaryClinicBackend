package com.veterinaryclinic.dto;

import java.time.LocalDateTime;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data 
public class RescheduleAppointmentRequest {
    @NotNull(message = "Нова дата та час є обов'язковими")
    @Future(message = "Нова дата та час повинні бути в майбутньому")
    private LocalDateTime appointmentDateTime;
}
