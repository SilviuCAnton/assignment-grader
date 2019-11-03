package repositories;

import domain.Grade;
import domain.validators.Validator;
import utils.Constants;
import utils.Pair;

import java.time.LocalDate;

public class GradeFileRepository extends AbstractFileRepository<Pair<String,Integer>, Grade> {
    GradeFileRepository(Validator<Grade> validator, String fileName) {
        super(validator, fileName);
    }

    /**
     * parses a grade from a file string
     * @param lineToParse - the grade to be parsed - String
     * @return grade - Grade
     */
    @Override
    Grade parseEntity(String lineToParse) {
        String[] args = lineToParse.split("/");
        return new Grade(args[0],Integer.parseInt(args[1]), LocalDate.parse(args[2], Constants.DATE_TIME_FORMATTER), Float.parseFloat(args[3]), args[4]);
    }
}
