package de.fhdo.reservelt.dto;

import de.fhdo.reservelt.annotation.PasswordValidation;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ChangeUserPasswordDto {

    @NotBlank
    private String currentPassword;

    @NotBlank
    @PasswordValidation
    private String newPassword;

    @NotBlank
    private String newPasswordConfirmation;
}
