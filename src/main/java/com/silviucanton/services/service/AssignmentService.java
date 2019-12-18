package com.silviucanton.services.service;

import com.silviucanton.domain.entities.Assignment;
import com.silviucanton.domain.validators.Validator;
import com.silviucanton.exceptions.InvalidAssignmentException;
import com.silviucanton.exceptions.ValidationException;
import com.silviucanton.repositories.CrudRepository;
import com.silviucanton.services.config.ApplicationContext;
import com.silviucanton.utils.observer.Observable;
import com.silviucanton.utils.observer.Observer;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.PageRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * service for assignment operations
 */
@org.springframework.stereotype.Service
public class AssignmentService implements Service, Observable<AssignmentService> {
    private CrudRepository<Assignment, Integer> assignmentRepository;
    private Validator<Assignment> validator;
    private List<Observer<AssignmentService>> observers = new ArrayList<>();

    public AssignmentService(@Qualifier("assignmentDatabaseRepository") CrudRepository<Assignment, Integer> assignmentRepository, Validator<Assignment> validator) {
        this.assignmentRepository = assignmentRepository;
        this.validator = validator;
    }

    /**
     * stores an assignment in the repository
     *
     * @param assignment - Assignment
     * @throws ValidationException      if the assignment is not valid
     * @throws IllegalArgumentException if the assignment is null
     */
    public Assignment addAssignment(Assignment assignment) throws ValidationException, IllegalArgumentException {
        validator.validate(assignment);
        Assignment res = assignmentRepository.save(assignment);
        notifyObservers();
        return res;
    }

    /**
     * updates an assignment in the repository
     *
     * @param assignment - Assignment
     * @return the result of the updating operation - Assignment
     * @throws ValidationException      if the assignment is not valid
     * @throws IllegalArgumentException if the assignment is null
     */
    public Assignment updateAssignment(Assignment assignment) throws ValidationException, IllegalArgumentException {
        if (assignment.getDeadlineWeek() < ApplicationContext.getYearStructure().getCurrentWeek(ApplicationContext.getCurrentLocalDate()))
            throw new InvalidAssignmentException("Deadline date cannot precede current date!");
        validator.validate(assignment);
        Assignment res = assignmentRepository.save(assignment);
        notifyObservers();
        return res;
    }

    /**
     * finds an assignment in the repository
     *
     * @param id - the id of the assignment - int
     * @return the result of the search operation - Assignment
     */
    public Optional<Assignment> findAssignment(int id) {
        return assignmentRepository.findById(id);
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
     */
    public void deleteAssignment(int id) {
        assignmentRepository.deleteById(id);
        notifyObservers();
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

    public List<Assignment> findAllAssignmentsByPage(int pageIndex, int rowsPerPage) {
        List<Assignment> assignmentList = new ArrayList<>();
        assignmentRepository.findAll(PageRequest.of(pageIndex, rowsPerPage)).forEach(assignmentList::add);
        return assignmentList;
    }
}
