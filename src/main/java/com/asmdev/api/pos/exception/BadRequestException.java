package com.asmdev.api.pos.exception;

public class BadRequestException extends Exception{

    private Object subErrors;

    public BadRequestException(String message) {
        super(message);
    }

    public BadRequestException(String message, Throwable cause) {
        super(message, cause);
    }

    public BadRequestException(String message, Object subErrors) {
        super(message);
        this.subErrors = subErrors;
    }

    public Object getSubErrors() {
        return subErrors;
    }

}
