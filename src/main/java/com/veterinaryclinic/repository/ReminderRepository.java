package com.veterinaryclinic.repository;

import com.veterinaryclinic.entity.Reminder;
import com.veterinaryclinic.entity.ReminderStatus;
import com.veterinaryclinic.entity.User;
import com.veterinaryclinic.entity.MedicationPrescription;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface  ReminderRepository extends JpaRepository<Reminder, Long>{
    List<Reminder> findByUserOrderByReminderDateTimeAsc(User user);
    List<Reminder> findByUserAndStatusOrderByReminderDateTimeAsc(User user, ReminderStatus status);
    List<Reminder> findByPrescription(MedicationPrescription prescription);
}
