package com.silviucanton.repositories.filePersistence;

import com.silviucanton.domain.entities.Assignment;
import com.silviucanton.domain.entities.Grade;
import com.silviucanton.domain.entities.GradeId;
import com.silviucanton.domain.entities.Student;
import com.silviucanton.domain.validators.Validator;
import com.silviucanton.repositories.CrudNoSpringRepo;
import com.silviucanton.repositories.GradeRepository;
import com.silviucanton.utils.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

/**
 * Repository for grades - file data persistence
 */
@Repository
public class GradeFileRepository extends AbstractFileRepository<Grade, GradeId> implements GradeRepository {
    private CrudNoSpringRepo<Student, String> studentRepo;
    private CrudNoSpringRepo<Assignment, Integer> assignmentRepo;

    @Autowired
    public GradeFileRepository(Validator<Grade> validator, @Value("${data.catalog.grades}") String fileName, @Qualifier("studentFileRepository") CrudNoSpringRepo<Student, String> studentRepo, @Qualifier("assignmentFileRepository") CrudNoSpringRepo<Assignment, Integer> assignmentRepo) {
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
    public CrudNoSpringRepo<Student, String> getStudentRepo() {
        return studentRepo;
    }

    /**
     * sets the student repository
     *
     * @param studentRepo - Student Crud Repository
     */
    public void setStudentRepo(CrudNoSpringRepo<Student, String> studentRepo) {
        this.studentRepo = studentRepo;
    }

    /**
     * returns the assignment repository
     *
     * @return assignmentRepo - Assignment Crud Repository
     */
    @Override
    public CrudNoSpringRepo<Assignment, Integer> getAssignmentRepo() {
        return assignmentRepo;
    }

    /**
     * sets the assignment repository
     *
     * @param assignmentRepo - Assignment Crud Repository
     */
    public void setAssignmentRepo(CrudNoSpringRepo<Assignment, Integer> assignmentRepo) {
        this.assignmentRepo = assignmentRepo;
    }
}
