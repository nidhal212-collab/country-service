package com.example.countryservice.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CountryDTO {

    private Integer idCountry;

    @NotBlank(message = "Le nom du pays est obligatoire")
    @Size(max = 100, message = "Le nom du pays ne doit pas dépasser 100 caractères")
    private String name;

    @NotBlank(message = "La capitale est obligatoire")
    @Size(max = 100, message = "La capitale ne doit pas dépasser 100 caractères")
    private String capital;
}
