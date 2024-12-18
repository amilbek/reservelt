package de.fhdo.reservelt.annotation;

import de.fhdo.reservelt.validator.PasswordValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = PasswordValidator.class)
@Target({ ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
public @interface PasswordValidation {

    String message() default "Password must be strong (at least 6 characters, one uppercase, one lowercase, one digit, and one special character)";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
