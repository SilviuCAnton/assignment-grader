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
     * @return the result of the storing operation - Assignment
     * @throws ValidationException if the assignment is not valid
     * @throws IllegalArgumentException if the assignment is null
     */
    public Assignment addAssignment(int id, String description, int deadlineWeek) throws ValidationException, IllegalArgumentException {
        return assignmentFileRepository.save(new Assignment(id, description, deadlineWeek, ApplicationContext.getYearStructure()));
    }

    /**
     * updates an assignment in the repository
     * @param id - id of the assignment - int
     * @param description - description of the assignment - String
     * @param deadlineWeek - deadline week of the assignment - int
     * @return the result of the updating operation - Assignment
     * @throws ValidationException if the assignment is not valid
     * @throws IllegalArgumentException if the assignment is null
     */
    public Assignment updateAssignment(int id, String description, int deadlineWeek) throws ValidationException, IllegalArgumentException {
        return assignmentFileRepository.update(new Assignment(id, description, deadlineWeek, ApplicationContext.getYearStructure()));
    }

    /**
     * finds an assignment in the repository
     * @param id - the id of the assignment - int
     * @return the result of the search operation - Assignment
     */
    public Assignment findAssignment(int id) {
        return assignmentFileRepository.findOne(id);
    }

    /**
     * returns all assignments
     * @return list of all assignments - iterable of assignments
     */
    public Iterable<Assignment> findAllAssignments() {
        return assignmentFileRepository.findAll();
    }

    /**
     * deletes an assignment from the repository
     * @param id - id of the assignment - int
     * @return the result of the deletion operation - Assignment
     */
    public Assignment deleteAssignment(int id) {
        return assignmentFileRepository.delete(id);
    }
}
