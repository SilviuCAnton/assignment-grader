package com.silviucanton.exceptions;

public class InvalidGradeException extends ValidationException {
    public InvalidGradeException(String errorMessage) {
        super(errorMessage);
    }
}
