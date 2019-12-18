package com.silviucanton.repositories.memoryPersistence;

import com.silviucanton.domain.entities.Assignment;
import com.silviucanton.domain.entities.Grade;
import com.silviucanton.domain.entities.GradeId;
import com.silviucanton.domain.entities.Student;
import com.silviucanton.domain.validators.Validator;
import com.silviucanton.repositories.GradeRepository;
import com.silviucanton.repositories.Repository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

/**
 * In memory repository for grades
 */
@org.springframework.stereotype.Repository
public class GradeInMemoryRepository extends InMemoryRepository<Grade, GradeId> implements GradeRepository {
    private Repository<Student, String> studentRepo;
    private Repository<Assignment, Integer> assignmentRepo;

    @Autowired
    public GradeInMemoryRepository(Validator<Grade> validator, @Qualifier("studentInMemoryRepository") Repository<Student, String> studentRepo, @Qualifier("assignmentInMemoryRepository") Repository<Assignment, Integer> assignmentRepo) {
        super(validator);
        this.studentRepo = studentRepo;
        this.assignmentRepo = assignmentRepo;
    }

    /**
     * returns the student repository
     *
     * @return studentRepo - Student Crud Repository
     */
    @Override
    public Repository<Student, String> getStudentRepo() {
        return studentRepo;
    }

    /**
     * sets the student repository
     *
     * @param studentRepo - Student Crud Repository
     */
    public void setStudentRepo(Repository<Student, String> studentRepo) {
        this.studentRepo = studentRepo;
    }

    /**
     * returns the assignment repository
     *
     * @return assignmentRepo - Assignment Crud Repository
     */
    @Override
    public Repository<Assignment, Integer> getAssignmentRepo() {
        return assignmentRepo;
    }

    /**
     * sets the assignment repository
     *
     * @param assignmentRepo - Assignment Crud Repository
     */
    public void setAssignmentRepo(Repository<Assignment, Integer> assignmentRepo) {
        this.assignmentRepo = assignmentRepo;
    }
}
