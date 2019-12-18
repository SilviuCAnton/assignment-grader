package com.silviucanton.repositories;

import com.silviucanton.domain.entities.Assignment;
import com.silviucanton.domain.entities.Grade;
import com.silviucanton.domain.entities.GradeId;
import com.silviucanton.domain.entities.Student;

/**
 * Grade repo interface for allowing generic getter and setter for the student and assignment repos
 */
public interface GradeRepository extends CrudNoSpringRepo<Grade, GradeId> {
    Repository<Student, String> getStudentRepo();

    Repository<Assignment, Integer> getAssignmentRepo();
}
