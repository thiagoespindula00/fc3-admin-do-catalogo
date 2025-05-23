package com.fullcycle.admin.catalogo.domain.validation;

import java.util.List;

public interface ValidationHandler {
    ValidationHandler append(Error error);

    ValidationHandler append(ValidationHandler handler);

    ValidationHandler validate(Validation validation);

    List<Error> getErrors();

    default boolean hasErrors() {
        return getErrors() != null && !(getErrors().isEmpty());
    }

    default Error firstError() {
        if (hasErrors()) {
            return getErrors().get(0);
        }

        return null;
    }

    public interface Validation {
        void validate();
    }
}
