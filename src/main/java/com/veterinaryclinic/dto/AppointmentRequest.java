package com.veterinaryclinic.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Future;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class AppointmentRequest {
    @NotNull(message = "ID тварини є обов'язковим")
    private Long petId;

    @NotNull(message = "ID клініки є обов'язковим")
    private Long clinicId;

    @NotNull(message = "ID лікаря є обов'язковим")
    private Long doctorId;

    @NotNull(message = "Дата та час прийому є обов'язковими")
    @Future(message = "Дата прийому повинна бути в майбутньому")
    private LocalDateTime appointmentDateTime;
    private String reason;
    private String notes;
}
