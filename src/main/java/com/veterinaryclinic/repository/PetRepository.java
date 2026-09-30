package com.veterinaryclinic.repository;

import com.veterinaryclinic.entity.Pet;
import com.veterinaryclinic.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface PetRepository extends JpaRepository<Pet, Long> {
    List<Pet> findByOwner(User owner);
}