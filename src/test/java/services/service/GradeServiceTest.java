package services.service;

import domain.entities.Assignment;
import domain.entities.Grade;
import domain.entities.Student;
import domain.validators.AssignmentValidator;
import domain.validators.GradeValidator;
import domain.validators.StudentValidator;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import repositories.memoryPersistence.GradeInMemoryRepository;
import repositories.memoryPersistence.InMemoryRepository;
import utils.Constants;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GradeServiceTest {

    private GradeService gradeService;

    @BeforeEach
    void setUp() {
        InMemoryRepository<String, Student> studentRepo = new InMemoryRepository<>(new StudentValidator());
        studentRepo.save(new Student("aaaa1234", "St1", "L1", 221, "s1@a.com", "cord1"));
        studentRepo.save(new Student("aaaa1235", "St2", "L2", 221, "s2@a.com", "cord2"));
        studentRepo.save(new Student("aaaa1236", "St3", "L3", 221, "s3@a.com", "cord3"));
        studentRepo.save(new Student("aaaa1237", "St4", "L4", 223, "s4@a.com", "cord4"));

        InMemoryRepository<Integer, Assignment> assignmentRepo = new InMemoryRepository<>(new AssignmentValidator());
        assignmentRepo.save(new Assignment(1, "desc1", 7));
        assignmentRepo.save(new Assignment(2, "desc1", 7));
        assignmentRepo.save(new Assignment(3, "desc1", 7));
        Assignment assignment = new Assignment(4, "desc1", 7);
        assignment.setStartWeek(3);
        assignmentRepo.save(assignment);

        GradeInMemoryRepository gradeRepo = new GradeInMemoryRepository(new GradeValidator(), studentRepo, assignmentRepo);
        gradeRepo.save(new Grade(studentRepo.findOne("aaaa1234"), assignmentRepo.findOne(1), 7f, "Silviu"));
        gradeRepo.save(new Grade(studentRepo.findOne("aaaa1235"), assignmentRepo.findOne(2), 7.2f, "Silviu"));
        gradeRepo.save(new Grade(studentRepo.findOne("aaaa1235"), assignmentRepo.findOne(1), 8f, "Silviu"));
        gradeRepo.save(new Grade(studentRepo.findOne("aaaa1236"), assignmentRepo.findOne(3), 9.3f, "Dana"));
        Grade grade = new Grade(studentRepo.findOne("aaaa1237"), assignmentRepo.findOne(3), 9f, "Test");
        grade.setDate(LocalDate.parse("10.10.2019", Constants.DATE_TIME_FORMATTER));
        gradeRepo.save(grade);

        gradeService = new GradeService(gradeRepo);
    }

    @AfterEach
    void tearDown() {
        gradeService = null;
    }

    @Test
    void filterStudentsBySubmission() {
        List<Student> students = new ArrayList<>();
        gradeService.filterStudentsBySubmission(1).forEach(students::add);
        assertEquals(2, students.size());
    }

    @Test
    void filterStudentsBySubmissionNoResult() {
        List<Student> students = new ArrayList<>();
        gradeService.filterStudentsBySubmission(4).forEach(students::add);
        assertEquals(0, students.size());
    }

    @Test
    void filterStudentsBySubmissionAndProfessor() {
        List<Student> students = new ArrayList<>();
        gradeService.filterStudentsBySubmissionAndProfessor(1, "Silviu").forEach(students::add);
        assertEquals(2, students.size());
    }

    @Test
    void filterStudentsBySubmissionAndProfessorNoResult() {
        List<Student> students = new ArrayList<>();
        gradeService.filterStudentsBySubmissionAndProfessor(2, "Dana").forEach(students::add);
        assertEquals(0, students.size());
    }

    @Test
    void filterGradesByAssignmentAndWeek() {
        List<Grade> grades = new ArrayList<>();
        gradeService.filterGradesByAssignmentAndWeek(1,5).forEach(grades::add);
        assertEquals(2, grades.size());
    }

    @Test
    void filterGradesByAssignmentAndWeekNoResult() {
        List<Grade> grades = new ArrayList<>();
        gradeService.filterGradesByAssignmentAndWeek(1,8).forEach(grades::add);
        assertEquals(0, grades.size());
    }
}