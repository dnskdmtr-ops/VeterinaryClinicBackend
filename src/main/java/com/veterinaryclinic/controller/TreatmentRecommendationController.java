package com.veterinaryclinic.controller;

import com.veterinaryclinic.dto.TreatmentRecommendationRequest;
import com.veterinaryclinic.dto.TreatmentRecommendationResponse;
import com.veterinaryclinic.entity.TreatmentRecommendation;
import com.veterinaryclinic.entity.User;
import com.veterinaryclinic.service.TreatmentRecommendationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.List;


@RestController
@RequestMapping("/api/treatment-recommendations")
@RequiredArgsConstructor
public class TreatmentRecommendationController {
    private final TreatmentRecommendationService recommendationService;

    private TreatmentRecommendationResponse toResponse(TreatmentRecommendation recommendation) {
        return new TreatmentRecommendationResponse(
            recommendation.getId(),
            recommendation.getMedicalRecord().getId(),
            recommendation.getType(),
            recommendation.getRecommendation(),
            recommendation.getSchedule(),
            recommendation.getCreatedAt()
        );
    }

    @PostMapping
    public TreatmentRecommendationResponse create(@Valid @RequestBody TreatmentRecommendationRequest request, Authentication authentication) {
        User doctor = (User) authentication.getPrincipal();
        TreatmentRecommendation recommendation = recommendationService.createRecommendation(request, doctor);

        return toResponse(recommendation);
    }

    @GetMapping("/record/{medicalRecordId}")
    public List<TreatmentRecommendationResponse> getRecommendations(@PathVariable Long medicalRecordId, Authentication authentication) {
        User doctor = (User) authentication.getPrincipal();
        List<TreatmentRecommendation> recommendations = recommendationService.getRecommendations(medicalRecordId, doctor);
        List<TreatmentRecommendationResponse> response = recommendations.stream()
                                                                       .map(this::toResponse)
                                                                       .toList();

        return response;
    }
}
