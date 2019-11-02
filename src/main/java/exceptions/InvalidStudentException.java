package exceptions;

public class InvalidStudentException extends ValidationException {
    public InvalidStudentException(String errorMessage) {
        super(errorMessage);
    }
}
