package com.veterinaryclinic.dto;

import com.veterinaryclinic.entity.ReminderStatus;
import com.veterinaryclinic.entity.ReminderType;
import java.time.LocalDateTime;

public record ReminderResponse (
    Long id,
    ReminderType type,
    ReminderStatus status,
    LocalDateTime reminderDateTime,
    Long prescriptionId,
    String prescriptionName,
    String dosage,
    String message,
    LocalDateTime createdAt,
    LocalDateTime completedAt
) {
}
