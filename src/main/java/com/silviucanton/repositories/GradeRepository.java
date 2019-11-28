package com.silviucanton.repositories;

import com.silviucanton.domain.entities.Assignment;
import com.silviucanton.domain.entities.Grade;
import com.silviucanton.domain.entities.Student;
import com.silviucanton.utils.Pair;

/**
 * Grade repo interface for allowing generic getter and setter for the student and assignment repos
 */
public interface GradeRepository extends CrudRepository<Pair<String, Integer>, Grade> {
    CrudRepository<String, Student> getStudentRepo();

    CrudRepository<Integer, Assignment> getAssignmentRepo();
}
