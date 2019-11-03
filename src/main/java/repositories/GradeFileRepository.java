package repositories;

import domain.Grade;
import domain.validators.Validator;
import utils.Constants;
import utils.Pair;

import java.time.LocalDate;

public class GradeFileRepository extends AbstractFileRepository<Pair<String,Integer>, Grade> {
    public GradeFileRepository(Validator<Grade> validator, String fileName) {
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
        Grade grade = new Grade(args[0],Integer.parseInt(args[1]), Float.parseFloat(args[3]), args[4]);
        grade.setDate(LocalDate.parse(args[2], Constants.DATE_TIME_FORMATTER));
        return grade;
    }
}
