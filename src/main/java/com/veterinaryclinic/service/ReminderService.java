package com.veterinaryclinic.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.veterinaryclinic.entity.MedicationPrescription;
import com.veterinaryclinic.entity.MedicationPrescriptionStatus;
import com.veterinaryclinic.entity.Reminder;
import com.veterinaryclinic.entity.ReminderStatus;
import com.veterinaryclinic.entity.ReminderType;
import com.veterinaryclinic.entity.Role;
import com.veterinaryclinic.entity.User;
import com.veterinaryclinic.exception.AccessDeniedException;
import com.veterinaryclinic.repository.ReminderRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReminderService {
    private final ReminderRepository reminderRepository;

    public List<Reminder> createMedicationReminders(MedicationPrescription prescription) {
        if (prescription.getStatus() != MedicationPrescriptionStatus.ACTIVE) {
            throw new IllegalArgumentException("Нагадування можна створити тільки для активного призначення");
        }

        if (prescription.getAdministrationTimes() == null || prescription.getAdministrationTimes().isBlank()) {
            throw new IllegalArgumentException("Час прийому препарату не вказано");
        }

        List<Reminder> reminders = new ArrayList<>();

        LocalDate startDate = prescription.getStartDate();
        LocalDate endDate = prescription.getEndDate();

        if (endDate == null) {
            endDate = startDate;
        }

        String[] times = prescription.getAdministrationTimes().split(",");
        for (LocalDate date = startDate;
             !date.isAfter(endDate);
             date = date.plusDays(1)) {
                for (String time : times) {
                    LocalTime administrationTime = LocalTime.parse(time.trim(), DateTimeFormatter.ofPattern("H:mm"));
                    LocalDateTime remiinderDateTime = LocalDateTime.of(date, administrationTime);
                    Reminder reminder = Reminder.builder()
                                                .user(prescription.getMedicalRecord()
                                                                  .getPet()
                                                                  .getOwner()
                                                )

                                                .type(ReminderType.MEDICATION)
                                                .status(ReminderStatus.PENDING)
                                                .reminderDateTime(remiinderDateTime)
                                                .prescription(prescription)
                                                .message(prescription.getMedicationName() + "-" + prescription.getDosage())
                                                .createdAt(LocalDateTime.now())
                                                .build();

                    reminders.add(reminder);
                }
        }

        return reminderRepository.saveAll(reminders);
    }

    public List<Reminder> getMyReminders(User user) {
        if (user.getRole() != Role.PATIENT) {
            throw new AccessDeniedException("Тільки пацієнт може переглядати свої нагадування");
        }

        return reminderRepository.findByUserOrderByReminderDateTimeAsc(user);
    }

    public Reminder completeReminder(Long reminderId, User user) {
        Reminder reminder = reminderRepository.findById(reminderId).orElseThrow(() -> new IllegalArgumentException("Нагадування не знайдено"));

        if (!reminder.getUser().getId().equals(user.getId())) {
            throw new AccessDeniedException("Немає доступу до цього нагадування");
        }

        if (reminder.getStatus() == ReminderStatus.CANCELLED) {
            throw new IllegalArgumentException("Скасоване нагадування не можна виконати");
        }

        if (reminder.getStatus() == ReminderStatus.COMPLETED) {
            return reminder;
        }

        reminder.setStatus(ReminderStatus.COMPLETED);
        reminder.setCompletedAt(LocalDateTime.now());

        return reminderRepository.save(reminder);
    }

    public void cancelPendingReminders(MedicationPrescription prescription) {
        List<Reminder> reminders = reminderRepository.findByPrescription(prescription);
        
        for (Reminder reminder : reminders) {
            if (reminder.getStatus() == ReminderStatus.PENDING) {
                reminder.setStatus(ReminderStatus.CANCELLED);
            }
        }

        reminderRepository.saveAll(reminders);
    }
}
