package com.fullcycle.admin.catalogo.domain.exceptions;

public abstract class NoStacktraceException extends RuntimeException {
    protected NoStacktraceException(String message) {
        super(message, null, true, false);
    }
}
