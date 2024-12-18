package de.fhdo.reservelt.exception;

import graphql.ErrorClassification;
import graphql.GraphQLError;
import graphql.language.SourceLocation;
import java.util.Collections;

import java.util.List;
import java.util.Map;

public record GenericGraphQLError(String message) implements GraphQLError {

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public List<SourceLocation> getLocations() {
        return Collections.emptyList();
    }

    @Override
    public ErrorClassification getErrorType() {
        return ErrorClassification.errorClassification("BAD_USER_INPUT");
    }

    @Override
    public Map<String, Object> getExtensions() {
        return Collections.emptyMap();
    }
}

