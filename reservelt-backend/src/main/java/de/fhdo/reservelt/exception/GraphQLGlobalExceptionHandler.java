package de.fhdo.reservelt.exception;

import graphql.GraphQLError;
import jakarta.validation.ConstraintViolationException;
import org.springframework.graphql.data.method.annotation.GraphQlExceptionHandler;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;

@ControllerAdvice
public class GraphQLGlobalExceptionHandler {

    @GraphQlExceptionHandler({IllegalArgumentException.class})
    public GraphQLError handleIllegalArgumentException(IllegalArgumentException ex) {
        return new GenericGraphQLError("Invalid input: " + ex.getMessage());
    }

    @GraphQlExceptionHandler({BadCredentialsException.class})
    public GraphQLError handleIllegalArgumentException(BadCredentialsException ex) {
        return new GenericGraphQLError("Invalid input: " + ex.getMessage());
    }

    @GraphQlExceptionHandler({MethodArgumentNotValidException.class})
    public GraphQLError handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        StringBuilder errorMessages = new StringBuilder("Validation failed: ");

        for (FieldError fieldError : ex.getBindingResult().getFieldErrors()) {
            errorMessages.append(fieldError.getDefaultMessage()).append("; ");
        }

        if (errorMessages.length() > 0) {
            errorMessages.setLength(errorMessages.length() - 2);
        }

        return new GenericGraphQLError(errorMessages.toString());
    }

    @GraphQlExceptionHandler({ConstraintViolationException.class})
    public GraphQLError handleConstraintViolationException(ConstraintViolationException ex) {
        return new GenericGraphQLError("Invalid input: " + ex.getMessage());
    }
}


