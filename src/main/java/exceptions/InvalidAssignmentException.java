package exceptions;

public class InvalidAssignmentException extends ValidationException {
    public InvalidAssignmentException(String errorMessage) {
        super(errorMessage);
    }
}
