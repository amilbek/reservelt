package de.fhdo.reservelt.dto;

import de.fhdo.reservelt.annotation.PasswordValidation;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
public class UserRegisterDto {

    @NotBlank
    private String firstName;

    @NotBlank
    private String lastName;

    @NotNull
    private LocalDate birthDate;

    @NotNull
    private Long country;

    @NotNull
    private Long city;

    @NotBlank
    @Email
    private String email;

    @NotBlank
    @PasswordValidation
    private String password;

    @NotBlank
    private String passwordConfirmation;
}
