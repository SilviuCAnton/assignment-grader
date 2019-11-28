package com.silviucanton.exceptions;

public abstract class ValidationException extends RuntimeException {
    ValidationException(String errorMessage) {
        super(errorMessage);
    }
}
