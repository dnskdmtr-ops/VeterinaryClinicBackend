package com.veterinaryclinic.service;

import com.veterinaryclinic.dto.PetRequest;
import com.veterinaryclinic.entity.Pet;
import com.veterinaryclinic.entity.User;
import com.veterinaryclinic.repository.PetRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service 
@RequiredArgsConstructor 
public class PetService {
    private final PetRepository petRepository;

    public Pet createPet(PetRequest request, User owner) {
        Pet pet = Pet.builder().name(request.getName())
                               .species(request.getSpecies())
                               .breed(request.getBreed())
                               .gender(request.getGender())
                               .age(request.getAge())
                               .owner(owner)
                               .build();

        return petRepository.save(pet);
    }

    public List<Pet> getMyPets(User owner) {
        return petRepository.findByOwner(owner);
    }
}
