package domain.validators;

import domain.Student;
import exceptions.InvalidStudentException;
import exceptions.ValidationException;

/**
 * Class for validating student entities
 */
public class StudentValidator implements Validator<Student> {
    /**
     * validates a student
     * @param student - Student to be validated
     * @throws ValidationException if student is not valid
     */
    @Override
    public void validate(Student student) throws ValidationException {
        String error = "";
        if(!student.getId().matches("[a-z]{4}[1-9][0-9]{3}")){
            error += "Student id not valid. ";
        }

        if(student.getCoordinator().isEmpty()){
            error += "Coordinator cannot be null. ";
        }

        if(!student.getEmail().matches(".+@.+\\..+")){
            error += "Invalid student email. ";
        }

        if(student.getFirstName().isEmpty()){
            error += "First name cannot be null. ";
        }

        if(student.getLastName().isEmpty()){
            error += "Last name cannot be null. ";
        }
        if(!error.isEmpty()){
            throw new InvalidStudentException(error);
        }
    }
}
