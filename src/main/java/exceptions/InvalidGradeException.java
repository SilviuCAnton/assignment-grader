package exceptions;

public class InvalidGradeException extends ValidationException {
    public InvalidGradeException(String errorMessage) {
        super(errorMessage);
    }
}
