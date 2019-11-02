package exceptions;

public abstract class ValidationException extends RuntimeException {
    ValidationException(String errorMessage) {
        super(errorMessage);
    }
}
