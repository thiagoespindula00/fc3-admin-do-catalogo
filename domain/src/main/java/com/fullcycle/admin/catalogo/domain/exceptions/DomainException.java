package com.fullcycle.admin.catalogo.domain.exceptions;


import com.fullcycle.admin.catalogo.domain.validation.Error;

import java.util.ArrayList;
import java.util.List;

public class DomainException extends NoStacktraceException {

    private final List<Error> errors;

    private DomainException(List<Error> errors) {
        super("");
        this.errors = errors;
    }

    public static DomainException with(final List<Error> errors) {
        return new DomainException(errors);
    }

    public static DomainException with(final Error error) {
        return new DomainException(new ArrayList<>(List.of(error)));
    }

    public List<Error> getErrors() {
        return errors;
    }
}
