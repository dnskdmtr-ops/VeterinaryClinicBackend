package com.veterinaryclinic.controller;

import com.veterinaryclinic.dto.PetRequest;
import com.veterinaryclinic.dto.PetResponse;
import com.veterinaryclinic.entity.Pet;
import com.veterinaryclinic.entity.User;
import com.veterinaryclinic.service.PetService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController 
@RequestMapping ("/api/pets")
@RequiredArgsConstructor
public class PetController {
    private final PetService petService;

    private PetResponse toResponse(Pet pet) {
        return new PetResponse(
            pet.getId(), 
            pet.getName(), 
            pet.getSpecies(), 
            pet.getBreed(), 
            pet.getGender(), 
            pet.getAge()
        );
    }

    @PostMapping
    public ResponseEntity<PetResponse> createPet(@Valid @RequestBody PetRequest request, Authentication authentication) {
        User owner = (User) authentication.getPrincipal();
        Pet pet = petService.createPet(request, owner);

        return ResponseEntity.status(HttpStatus.CREATED).body(toResponse(pet));
    }
    
    @GetMapping
    public ResponseEntity<List<PetResponse>> getMyPets(Authentication authentication) {
        User owner = (User) authentication.getPrincipal();
        List<PetResponse> response = petService.getMyPets(owner).stream()
                                                                .map(this::toResponse)
                                                                .toList();

        return ResponseEntity.ok(response);
    }
}