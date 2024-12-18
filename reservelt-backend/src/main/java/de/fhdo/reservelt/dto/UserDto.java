package de.fhdo.reservelt.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;
import java.util.UUID;

@Data
public class UserDto {

    private UUID id;

    @NotBlank
    private String firstName;

    @NotBlank
    private String lastName;

    @NotNull
    private LocalDate birthDate;

    private CountryDto country;

    private CityDto city;

    @NotBlank
    private String email;

    @NotBlank
    @JsonIgnore
    private String password;

    @NotBlank
    @JsonIgnore
    private String passwordConfirmation;
}
