package repositories.filePersistence;

import domain.entities.Assignment;
import domain.validators.AssignmentValidator;

/**
 * Repository for assignment storage - file data persistence
 */
public class AssignmentFileRepository extends AbstractFileRepository<Integer, Assignment> {
    public AssignmentFileRepository(AssignmentValidator validator, String fileName) {
        super(validator, fileName, true);
    }

    /**
     * parses an assignment from a file string
     *
     * @param lineToParse - the assignment to be parsed - String
     * @return assignment - Assignment
     */
    @Override
    Assignment parseEntity(String lineToParse) {
        String[] args = lineToParse.split("/");
        Assignment assignment = new Assignment(Integer.parseInt(args[0]), args[1], Integer.parseInt(args[3]));
        assignment.setStartWeek(Integer.parseInt(args[2]));
        return assignment;
    }
}
