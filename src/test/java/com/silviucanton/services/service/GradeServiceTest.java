//package com.silviucanton.services.service;
//
//import com.silviucanton.domain.entities.Assignment;
//import com.silviucanton.domain.entities.Grade;
//import com.silviucanton.domain.entities.Student;
//import com.silviucanton.domain.validators.AssignmentValidator;
//import com.silviucanton.domain.validators.GradeValidator;
//import com.silviucanton.domain.validators.StudentValidator;
//import com.silviucanton.repositories.memoryPersistence.AssignmentInMemoryRepository;
//import com.silviucanton.repositories.memoryPersistence.StudentInMemoryRepository;
//import org.junit.jupiter.api.AfterEach;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import com.silviucanton.repositories.memoryPersistence.GradeInMemoryRepository;
//import com.silviucanton.repositories.memoryPersistence.InMemoryRepository;
//import com.silviucanton.utils.Constants;
//
//import java.time.LocalDate;
//import java.util.ArrayList;
//import java.util.List;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//class GradeServiceTest {
//
//    private GradeService gradeService;
//
//    @BeforeEach
//    void setUp() {
//        StudentInMemoryRepository studentRepo = new StudentInMemoryRepository(new StudentValidator());
//        studentRepo.save(new Student("aaaa1234", "St1", "L1", 221, "s1@a.com", "cord1"));
//        studentRepo.save(new Student("aaaa1235", "St2", "L2", 221, "s2@a.com", "cord2"));
//        studentRepo.save(new Student("aaaa1236", "St3", "L3", 221, "s3@a.com", "cord3"));
//        studentRepo.save(new Student("aaaa1237", "St4", "L4", 223, "s4@a.com", "cord4"));
//
//        AssignmentInMemoryRepository assignmentRepo = new AssignmentInMemoryRepository(new AssignmentValidator());
//        assignmentRepo.save(new Assignment(1, "desc1", 7));
//        assignmentRepo.save(new Assignment(2, "desc1", 7));
//        assignmentRepo.save(new Assignment(3, "desc1", 7));
//        Assignment assignment = new Assignment(4, "desc1", 7);
//        assignment.setStartWeek(3);
//        assignmentRepo.save(assignment);
//
//        GradeInMemoryRepository gradeRepo = new GradeInMemoryRepository(new GradeValidator(), studentRepo, assignmentRepo);
//        gradeRepo.save(new Grade(studentRepo.findOne("aaaa1234"), assignmentRepo.findOne(1), 7f, "Silviu"));
//        gradeRepo.save(new Grade(studentRepo.findOne("aaaa1235"), assignmentRepo.findOne(2), 7.2f, "Silviu"));
//        gradeRepo.save(new Grade(studentRepo.findOne("aaaa1235"), assignmentRepo.findOne(1), 8f, "Silviu"));
//        gradeRepo.save(new Grade(studentRepo.findOne("aaaa1236"), assignmentRepo.findOne(3), 9.3f, "Dana"));
//        Grade grade = new Grade(studentRepo.findOne("aaaa1237"), assignmentRepo.findOne(3), 9f, "Test");
//        grade.setDate(LocalDate.parse("10.10.2019", Constants.DATE_TIME_FORMATTER));
//        gradeRepo.save(grade);
//
//        gradeService = new GradeService(gradeRepo, studentRepo, assignmentRepo, new GradeValidator());
//    }
//
//    @AfterEach
//    void tearDown() {
//        gradeService = null;
//    }
//
//    @Test
//    void filterStudentsBySubmission() {
//        assertEquals(2, gradeService.filterStudentsBySubmission(1).size());
//    }
//
//    @Test
//    void filterStudentsBySubmissionNoResult() {
//        assertEquals(0, gradeService.filterStudentsBySubmission(4).size());
//    }
//
//    @Test
//    void filterStudentsBySubmissionAndProfessor() {
//        assertEquals(2, gradeService.filterStudentsBySubmissionAndProfessor(1, "Silviu").size());
//    }
//
//    @Test
//    void filterStudentsBySubmissionAndProfessorNoResult() {
//        assertEquals(0, gradeService.filterStudentsBySubmissionAndProfessor(2, "Dana").size());
//    }
//
//    @Test
//    void filterGradesByAssignmentAndWeek() {
//        assertEquals(2, gradeService.filterGradesByAssignmentAndWeek(1,5).size());
//    }
//
//    @Test
//    void filterGradesByAssignmentAndWeekNoResult() {
//        assertEquals(0, gradeService.filterGradesByAssignmentAndWeek(1,8).size());
//    }
//}