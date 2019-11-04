package services.service;

import domain.entities.Assignment;
import exceptions.InvalidAssignmentException;
import exceptions.ValidationException;
import repositories.CrudRepository;
import services.config.ApplicationContext;

/**
 *
 */
public class AssignmentService {
    private CrudRepository<Integer, Assignment> assignmentRepository;

    public AssignmentService(CrudRepository<Integer, Assignment> assignmentRepository) {
        this.assignmentRepository = assignmentRepository;
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
        return assignmentRepository.save(new Assignment(id, description, deadlineWeek));
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
    public Assignment updateAssignment(int id, String description,int startWeek, int deadlineWeek) throws ValidationException, IllegalArgumentException {
        if(deadlineWeek < ApplicationContext.getYearStructure().getCurrentWeek())
            throw new InvalidAssignmentException("Deadline date cannot precede current date!");
        Assignment assignment = new Assignment(id, description, deadlineWeek);
        assignment.setStartWeek(startWeek);
        return assignmentRepository.update(assignment);
    }

    /**
     * finds an assignment in the repository
     * @param id - the id of the assignment - int
     * @return the result of the search operation - Assignment
     */
    public Assignment findAssignment(int id) {
        return assignmentRepository.findOne(id);
    }

    /**
     * returns all assignments
     * @return list of all assignments - iterable of assignments
     */
    public Iterable<Assignment> findAllAssignments() {
        return assignmentRepository.findAll();
    }

    /**
     * deletes an assignment from the repository
     * @param id - id of the assignment - int
     * @return the result of the deletion operation - Assignment
     */
    public Assignment deleteAssignment(int id) {
        return assignmentRepository.delete(id);
    }
}
