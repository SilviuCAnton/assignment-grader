package com.silviucanton.domain.validators;

import com.silviucanton.domain.entities.Grade;
import com.silviucanton.exceptions.InvalidGradeException;
import com.silviucanton.exceptions.ValidationException;
import org.springframework.stereotype.Component;

/**
 * Class for validating grade entities
 */
@Component
public class GradeValidator implements Validator<Grade> {
    /**
     * validates a grade
     *
     * @param grade - Grade
     * @throws ValidationException if the grade is not valid
     */
    @Override
    public void validate(Grade grade) throws ValidationException {
        String error = "";
        if (grade.getId().getFirst().isEmpty()) {
            error += "Grade id not valid. ";
        }

        if (grade.getValue() < 1 || grade.getValue() > 10) {
            error += "Grade must be between 1 and 10. ";
        }

        if (grade.getProfessor().isEmpty()) {
            error += "Professor cannot be null. ";
        }

        if (grade.getDate() == null) {
            error += "Date cannot be null. ";
        }

        if (!error.isEmpty()) {
            throw new InvalidGradeException(error);
        }
    }
}
