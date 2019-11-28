package com.silviucanton.services.service;

import com.silviucanton.domain.entities.Assignment;
import com.silviucanton.exceptions.InvalidAssignmentException;
import com.silviucanton.exceptions.ValidationException;
import com.silviucanton.repositories.CrudRepository;
import com.silviucanton.services.config.ApplicationContext;
import com.silviucanton.utils.observer.Observable;
import com.silviucanton.utils.observer.Observer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * service for assignment operations
 */
@Component
public class AssignmentService implements Service, Observable<AssignmentService> {
    private CrudRepository<Integer, Assignment> assignmentRepository;
    private List<Observer<AssignmentService>> observers = new ArrayList<>();

    @Autowired
    public AssignmentService(@Qualifier("assignmentDatabaseRepository") CrudRepository<Integer, Assignment> assignmentRepository) {
        this.assignmentRepository = assignmentRepository;
    }

    /**
     * stores an assignment in the repository
     *
     * @param id           - id of the assignment - int
     * @param description  - description of the assignment - String
     * @param deadlineWeek - deadline week of the assignment - int
     * @return the result of the storing operation - Assignment
     * @throws ValidationException      if the assignment is not valid
     * @throws IllegalArgumentException if the assignment is null
     */
    public Assignment addAssignment(int id, String description, int deadlineWeek) throws ValidationException, IllegalArgumentException {
        return assignmentRepository.save(new Assignment(id, description, deadlineWeek));
    }

    /**
     * updates an assignment in the repository
     *
     * @param id           - id of the assignment - int
     * @param description  - description of the assignment - String
     * @param deadlineWeek - deadline week of the assignment - int
     * @return the result of the updating operation - Assignment
     * @throws ValidationException      if the assignment is not valid
     * @throws IllegalArgumentException if the assignment is null
     */
    public Assignment updateAssignment(int id, String description, int startWeek, int deadlineWeek) throws ValidationException, IllegalArgumentException {
        if (deadlineWeek < ApplicationContext.getYearStructure().getCurrentWeek(ApplicationContext.getCurrentLocalDate()))
            throw new InvalidAssignmentException("Deadline date cannot precede current date!");
        Assignment assignment = new Assignment(id, description, deadlineWeek);
        assignment.setStartWeek(startWeek);
        return assignmentRepository.update(assignment);
    }

    /**
     * finds an assignment in the repository
     *
     * @param id - the id of the assignment - int
     * @return the result of the search operation - Assignment
     */
    public Assignment findAssignment(int id) {
        return assignmentRepository.findOne(id);
    }

    /**
     * returns all assignments
     *
     * @return list of all assignments - list of assignments
     */
    public List<Assignment> findAllAssignments() {
        List<Assignment> assignments = new ArrayList<>();
        assignmentRepository.findAll().forEach(assignments::add);
        return assignments;
    }

    /**
     * deletes an assignment from the repository
     *
     * @param id - id of the assignment - int
     * @return the result of the deletion operation - Assignment
     */
    public Assignment deleteAssignment(int id) {
        return assignmentRepository.delete(id);
    }

    @Override
    public void addObserver(Observer<AssignmentService> e) {
        observers.add(e);
    }

    @Override
    public void removeObserver(Observer<AssignmentService> e) {
        observers.remove(e);
    }

    @Override
    public void notifyObservers() {
        observers.forEach(x -> x.update(this));
    }
}
