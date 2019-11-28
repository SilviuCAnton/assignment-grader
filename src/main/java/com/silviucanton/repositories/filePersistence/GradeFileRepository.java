package com.silviucanton.repositories.filePersistence;

import com.silviucanton.domain.entities.Assignment;
import com.silviucanton.domain.entities.Grade;
import com.silviucanton.domain.entities.Student;
import com.silviucanton.domain.validators.Validator;
import com.silviucanton.repositories.CrudRepository;
import com.silviucanton.repositories.GradeRepository;
import com.silviucanton.utils.Constants;
import com.silviucanton.utils.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

/**
 * Repository for grades - file data persistence
 */
@Component
public class GradeFileRepository extends AbstractFileRepository<Pair<String, Integer>, Grade> implements GradeRepository {
    private CrudRepository<String, Student> studentRepo;
    private CrudRepository<Integer, Assignment> assignmentRepo;

    @Autowired
    public GradeFileRepository(Validator<Grade> validator, @Value("${data.catalog.grades}") String fileName, @Qualifier("studentFileRepository") CrudRepository<String, Student> studentRepo, @Qualifier("assignmentFileRepository") CrudRepository<Integer, Assignment> assignmentRepo) {
        super(validator, fileName, false);
        this.studentRepo = studentRepo;
        this.assignmentRepo = assignmentRepo;
        super.loadDataFromFile();
    }

    /**
     * parses a grade from a file string
     *
     * @param lineToParse - the grade to be parsed - String
     * @return grade - Grade
     */
    @Override
    Grade parseEntity(String lineToParse) {
        String[] args = lineToParse.split("/");
        Student student = studentRepo.findOne(args[0]);
        Assignment assignment = assignmentRepo.findOne(Integer.parseInt(args[1]));
        Grade grade = new Grade(student, assignment, Float.parseFloat(args[3]), args[4]);
        grade.setDate(LocalDate.parse(args[2], Constants.DATE_TIME_FORMATTER));
        return grade;
    }

    /**
     * returns the student repository
     *
     * @return studentRepo - Student Crud Repository
     */
    @Override
    public CrudRepository<String, Student> getStudentRepo() {
        return studentRepo;
    }

    /**
     * sets the student repository
     *
     * @param studentRepo - Student Crud Repository
     */
    public void setStudentRepo(CrudRepository<String, Student> studentRepo) {
        this.studentRepo = studentRepo;
    }

    /**
     * returns the assignment repository
     *
     * @return assignmentRepo - Assignment Crud Repository
     */
    @Override
    public CrudRepository<Integer, Assignment> getAssignmentRepo() {
        return assignmentRepo;
    }

    /**
     * sets the assignment repository
     *
     * @param assignmentRepo - Assignment Crud Repository
     */
    public void setAssignmentRepo(CrudRepository<Integer, Assignment> assignmentRepo) {
        this.assignmentRepo = assignmentRepo;
    }
}
