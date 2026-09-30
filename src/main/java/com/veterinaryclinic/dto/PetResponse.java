package com.veterinaryclinic.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data 
@AllArgsConstructor 
public class PetResponse {
    private Long id;
    private String name;
    private String species;
    private String breed;
    private String gender;
    private Integer age;
}
