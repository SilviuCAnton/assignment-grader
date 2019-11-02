package repositories;

import domain.Assignment;
import domain.validators.AssignmentValidator;
import utils.Constants;

/**
 * Repository for assignment storage - file data persistence
 */
public class AssignmentFileRepository extends AbstractFileRepository<Integer, Assignment> {
    public AssignmentFileRepository(AssignmentValidator validator, String fileName) {
        super(validator, fileName);
    }

    @Override
    Assignment parseEntity(String lineToParse) {
        String[] args = lineToParse.split("/");
        Assignment assignment = new Assignment(Integer.parseInt(args[0]),args[1],Integer.parseInt(args[3]), Constants.yearStructure);
        assignment.setStartWeek(Integer.parseInt(args[2]));
        return assignment;
    }
}
