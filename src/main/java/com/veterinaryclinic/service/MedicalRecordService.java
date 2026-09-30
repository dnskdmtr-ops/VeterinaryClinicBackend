package com.veterinaryclinic.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.veterinaryclinic.dto.MedicalHistoryRecordResponse;
import com.veterinaryclinic.dto.MedicalHistoryResponse;
import com.veterinaryclinic.dto.MedicalObservationResponse;
import com.veterinaryclinic.dto.MedicalRecordRequest;
import com.veterinaryclinic.dto.MedicationPrescriptionResponse;
import com.veterinaryclinic.dto.TreatmentRecommendationResponse;
import com.veterinaryclinic.entity.Appointment;
import com.veterinaryclinic.entity.AppointmentStatus;
import com.veterinaryclinic.entity.Clinic;
import com.veterinaryclinic.entity.DoctorClinicStatus;
import com.veterinaryclinic.entity.MedicalRecord;
import com.veterinaryclinic.entity.Pet;
import com.veterinaryclinic.entity.Role;
import com.veterinaryclinic.entity.User;
import com.veterinaryclinic.repository.AppointmentRepository;
import com.veterinaryclinic.repository.DoctorClinicRepository;
import com.veterinaryclinic.repository.MedicalObservationRepository;
import com.veterinaryclinic.repository.MedicalRecordRepository;
import com.veterinaryclinic.repository.MedicationPrescriptionRepository;
import com.veterinaryclinic.repository.PetRepository;
import com.veterinaryclinic.repository.TreatmentRecommendationRepository;

import lombok.RequiredArgsConstructor;

@Service 
@RequiredArgsConstructor 
public class MedicalRecordService {
    private final MedicalRecordRepository medicalRecordRepository;
    private final AppointmentRepository appointmentRepository;
    private final PetRepository petRepository;
    private final DoctorClinicRepository doctorClinicRepository;
    private final MedicalObservationRepository medicalObservationRepository;
    private final MedicationPrescriptionRepository medicationPrescriptionRepository;
    private final TreatmentRecommendationRepository treatmentRecommendationRepository;

    public MedicalRecord createMedicalRecord(MedicalRecordRequest request, User doctor) {
        if (doctor.getRole() != Role.DOCTOR) {
            throw new IllegalArgumentException("Тільки лікар може створити медичний запис");
        }

        Appointment appointment = appointmentRepository.findById(request.getAppointmentId()).orElseThrow(() -> new IllegalArgumentException("Прийом не знайдено"));

        if (!appointment.getDoctor().getId().equals(doctor.getId())) {
            throw new IllegalArgumentException("Цей прийом не належить даному лікарю");
        }

        if (appointment.getStatus() != AppointmentStatus.COMPLETED) {
            throw new IllegalArgumentException("Медичний запис можна створити тільки після завершення прийому");
        }

        if (medicalRecordRepository.findByAppointment(appointment).isPresent()) {
            throw new IllegalArgumentException("Для цього прийому медичний запис вже існує");
        }

        MedicalRecord previousRecord = medicalRecordRepository.findTopByPetOrderByCreatedAtDesc(appointment.getPet()).orElse(null);
        MedicalRecord medicalRecord = MedicalRecord.builder().appointment(appointment)
                                                             .pet(appointment.getPet())
                                                             .clinic(appointment.getClinic())
                                                             .previousRecord(previousRecord)
                                                             .doctor(appointment.getDoctor())
                                                             .complaints(request.getComplaints())
                                                             .symptoms(request.getSymptoms())
                                                             .examinationResults(request.getExaminationResults())
                                                             .diagnosis(request.getDiagnosis())
                                                             .examination(request.getExaminations())
                                                             .treatment(request.getTreatment())
                                                             .recommendations(request.getRecommendations())
                                                             .notes(request.getNotes())
                                                             .createdAt(LocalDateTime.now())
                                                             .build();
                                                            
        return  medicalRecordRepository.save(medicalRecord);
    }

    public List<MedicalRecord> getMyRecords(User doctor) {
        if (doctor.getRole() != Role.DOCTOR) {
            throw new IllegalArgumentException("Тільки лікар може переглядати свої медичні записи");
        }

        return medicalRecordRepository.findByDoctor(doctor);
    }

    public List<MedicalRecord> getPetMedicalHistory(Long petId, User user) {
        Pet pet = petRepository.findById(petId).orElseThrow(() -> new IllegalArgumentException("Тварину не знайдено"));
        List<MedicalRecord> records = medicalRecordRepository.findByPetOrderByCreatedAtAsc(pet);

        if (user.getRole() == Role.PATIENT) {
            if (!pet.getOwner().getId().equals(user.getId())) {
                throw new IllegalArgumentException("Немає доступу до медичної історії цієї тварини");
            }

            return records;
        }

        if (user.getRole() == Role.DOCTOR) {
            return records.stream()
                          .filter(record -> doctorHasAccessToClinic(user, record.getClinic())).toList();
        }

        if (user.getRole() == Role.CLINIC_ADMIN) {
            if (user.getClinic() == null) {
                throw new IllegalArgumentException("Адміністратор не прив'язаний до клініки");
            }

            return records.stream()
                          .filter(record -> record.getClinic().getId().equals(user.getClinic().getId())).toList();
        }

        throw new IllegalArgumentException("Немає доступу до медичної історії");
    }

    private boolean doctorHasAccessToClinic(User doctor, Clinic clinic) {
        return doctorClinicRepository.findByDoctorAndClinic(doctor, clinic)
                                     .map(doctorClinic -> doctorClinic.getStatus() == DoctorClinicStatus.ACTIVE)
                                     .orElse(false);
    }

    public List<MedicalHistoryResponse> getFullMedicalHistory(Long petId, User user) {
        List<MedicalRecord> records = getPetMedicalHistory(petId, user);

        return records.stream()
                      .map(record -> {
                        MedicalHistoryRecordResponse medicalRecord = new MedicalHistoryRecordResponse(
                            record.getId(),

                            record.getAppointment().getId(),
                            record.getAppointment().getAppointmentDateTime(),

                            record.getPet().getId(),
                            record.getPet().getName(),

                            record.getClinic().getId(),
                            record.getClinic().getName(),

                            record.getDoctor().getId(),
                            record.getDoctor().getFirstName(),
                            record.getDoctor().getLastName(),

                            record.getComplaints(),
                            record.getSymptoms(),
                            record.getExaminationResults(),
                            record.getDiagnosis(),
                            record.getExamination(),
                            record.getTreatment(),
                            record.getRecommendations(),
                            record.getNotes(),

                            record.getCreatedAt()
                        );
                       
                        List<MedicalObservationResponse> observations = medicalObservationRepository.findByMedicalRecordOrderByCreatedAtAsc(record)
                                                                                                    .stream()
                                                                                                    .map(observation -> new MedicalObservationResponse(
                                                                                                           observation.getId(), 
                                                                                                           record.getId(), 
                                                                                                           observation.getCondition(), 
                                                                                                           observation.getSymptoms(), 
                                                                                                           observation.getObservation(), 
                                                                                                           observation.getNotes(), 
                                                                                                           observation.getCreatedAt()
                                                                                                        )
                                                                                                    ).toList();

                        List<MedicationPrescriptionResponse> medications = medicationPrescriptionRepository.findByMedicalRecord(record)
                                                                                                           .stream()
                                                                                                           .map(prescription -> new MedicationPrescriptionResponse(
                                                                                                                  prescription.getId(),
                                                                                                                  record.getId(),
                                                                                                                  prescription.getPreviousPrescription() != null
                                                                                                                                                         ? prescription.getPreviousPrescription().getId() 
                                                                                                                                                         : null,

                                                                                                                  prescription.getMedicationName(),
                                                                                                                  prescription.getDosage(),
                                                                                                                  prescription.getFrequency(),
                                                                                                                  prescription.getAdministrationTimes(),
                                                                                                                  prescription.getDuration(),
                                                                                                                  prescription.getStartDate(),
                                                                                                                  prescription.getEndDate(),
                                                                                                                  prescription.getInstructions(),
                                                                                                                  prescription.getStatus(),
                                                                                                                  prescription.getCreatedAt()
                                                                                                               )    
                                                                                                            ).toList();

                        List<TreatmentRecommendationResponse> recommendations = treatmentRecommendationRepository.findByMedicalRecordOrderByCreatedAtAsc(record)
                                                                                                                 .stream()
                                                                                                                 .map(recommendation -> new TreatmentRecommendationResponse(
                                                                                                                        recommendation.getId(),
                                                                                                                        record.getId(),
                                                                                                                        recommendation.getType(),
                                                                                                                        recommendation.getRecommendation(),
                                                                                                                        recommendation.getSchedule(),
                                                                                                                        recommendation.getCreatedAt()
                                                                                                                    )
                                                                                                                ).toList();

                        return new MedicalHistoryResponse(medicalRecord, observations, medications, recommendations);
                     }).toList();
    }
 }