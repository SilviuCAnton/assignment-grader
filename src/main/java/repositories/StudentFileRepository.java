package repositories;

import domain.Student;
import domain.validators.Validator;

/**
 * Repository for student storage - file data persistence
 */
public class StudentFileRepository extends AbstractFileRepository<String, Student> {
    public StudentFileRepository(Validator<Student> validator, String fileName) {
        super(validator, fileName);
    }

    @Override
    Student parseEntity(String lineToParse) {
        String[] args = lineToParse.split("/");
        return new Student(args[0],args[1],args[2],Integer.parseInt(args[3]), args[4],args[5]);
    }
}
