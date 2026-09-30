package com.veterinaryclinic.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;

@Data
public class PetRequest {
    @NotBlank (message = "Ім'я тварини є обов'язковим")
    private String name;

    @NotBlank(message = "Вид тварини є обов'язковим")
    private String species;
    private String breed;
    private String gender;

    @PositiveOrZero(message = "Вік не може бути від'ємним")
    private Integer age;
}
