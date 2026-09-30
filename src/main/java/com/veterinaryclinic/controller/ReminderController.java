package com.veterinaryclinic.controller;

import com.veterinaryclinic.dto.ReminderResponse;
import com.veterinaryclinic.entity.MedicationPrescription;
import com.veterinaryclinic.entity.Reminder;
import com.veterinaryclinic.entity.User;
import com.veterinaryclinic.service.ReminderService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.List;


@RestController
@RequestMapping("/api/reminders")
@RequiredArgsConstructor
public class ReminderController {
    private final ReminderService reminderService;

    private ReminderResponse toResponse(Reminder reminder) {
        MedicationPrescription prescription = reminder.getPrescription();

        return new ReminderResponse(
            reminder.getId(),
            reminder.getType(),
            reminder.getStatus(),
            reminder.getReminderDateTime(), 
            prescription != null
                         ? prescription.getId() : null,
            prescription != null
                         ? prescription.getMedicationName() : null,
            prescription != null
                         ? prescription.getDosage() : null,
            reminder.getMessage(),
            reminder.getCreatedAt(),
            reminder.getCompletedAt()
        );
    }

    @GetMapping
    public List<ReminderResponse> getMyReminders(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        List<Reminder> reminders = reminderService.getMyReminders(user);

        List<ReminderResponse> response = reminders.stream()
                                                   .map(this::toResponse)
                                                   .toList();

        return response;
    }

    @PutMapping("/{id}/read")
    public ReminderResponse completeReminder(@PathVariable Long id, Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        Reminder reminder = reminderService.completeReminder(id, user);

        return toResponse(reminder);
    }
}
