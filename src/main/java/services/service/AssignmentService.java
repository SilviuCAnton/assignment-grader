package services.service;

import domain.Assignment;
import domain.validators.AssignmentValidator;
import exceptions.ValidationException;
import repositories.AssignmentFileRepository;
import services.config.ApplicationContext;
import utils.Constants;

/**
 *
 */
public class AssignmentService {
    private AssignmentFileRepository assignmentFileRepository;

    public AssignmentService(String fileName) {
        this.assignmentFileRepository = new AssignmentFileRepository(new AssignmentValidator(), fileName);
    }

    /**
     * stores an assignment in the repository
     * @param id - id of the assignment - int
     * @param description - description of the assignment - String
     * @param deadlineWeek - deadline week of the assignment - int
     * @return the result of the storing operation - String
     * @throws ValidationException if the assignment is not valid
     * @throws IllegalArgumentException if the assignment is null
     */
    public String addAssignment(int id, String description, int deadlineWeek) throws ValidationException, IllegalArgumentException {
        String res;
        Assignment assignment = assignmentFileRepository.save(new Assignment(id, description, deadlineWeek, ApplicationContext.getYearStructure()));
        if (assignment != null) {
            res = "Assignment already exists: " + assignment.toString();
        } else {
            res = "The assignment has been stored.";
        }
        return res;
    }

    /**
     * updates an assignment in the repository
     * @param id - id of the assignment - int
     * @param description - description of the assignment - String
     * @param deadlineWeek - deadline week of the assignment - int
     * @return the result of the updating operation - String
     * @throws ValidationException if the assignment is not valid
     * @throws IllegalArgumentException if the assignment is null
     */
    public String updateAssignment(int id, String description, int deadlineWeek) throws ValidationException, IllegalArgumentException {
        String res;
        Assignment assignment = assignmentFileRepository.update(new Assignment(id, description, deadlineWeek, ApplicationContext.getYearStructure()));
        if (assignment != null) {
            res = "The assignment " + assignment.getDescription() + " has been updated.";
        } else {
            res = "The assignment with the given id does not exist.";
        }
        return res;
    }

    /**
     * finds an assignment in the repository
     * @param id - the id of the assignment - int
     * @return the result of the search operation - String
     */
    public String findAssignment(int id) {
        String res;
        Assignment assignment = assignmentFileRepository.findOne(id);
        if (assignment != null) {
            res = assignment.toString();
        } else {
            res = "The assignment with the given id does not exist.";
        }
        return res;
    }

    /**
     * returns all assignments
     * @return list of all assignments - String
     */
    public String findAllAssignments() {
        StringBuilder sb = new StringBuilder();
        assignmentFileRepository.findAll().forEach(x-> sb.append(x.toString()).append('\n'));
        return sb.toString();
    }

    /**
     * deletes an assignment from the repository
     * @param id - id of the assignment - int
     * @return the result of the deletion operation - String
     */
    public String deleteAssignment(int id) {
        String res;
        Assignment assignment = assignmentFileRepository.delete(id);
        if (assignment != null) {
            res = assignment.getDescription() + " has been removed.";
        } else {
            res = "The assignment with the given id does not exist.";
        }
        return res;
    }
}
