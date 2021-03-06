package com.silviucanton.repositories.filePersistence;

import com.silviucanton.domain.entities.Assignment;
import com.silviucanton.domain.entities.Grade;
import com.silviucanton.domain.entities.GradeId;
import com.silviucanton.domain.entities.Student;
import com.silviucanton.domain.validators.AssignmentValidator;
import com.silviucanton.domain.validators.GradeValidator;
import com.silviucanton.domain.validators.StudentValidator;
import com.silviucanton.exceptions.InvalidGradeException;
import com.silviucanton.repositories.memoryPersistence.AssignmentInMemoryRepository;
import com.silviucanton.repositories.memoryPersistence.InMemoryRepository;
import com.silviucanton.repositories.memoryPersistence.StudentInMemoryRepository;
import com.silviucanton.services.config.ApplicationContext;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class GradeFileRepositoryTest {

    private GradeFileRepository gradeFileRepository;
    private Path path;
    private Grade gr1, gr2;
    private Student st1;
    private Assignment as1, as2;
    private InMemoryRepository<Student, String> studentRepo;
    private InMemoryRepository<Assignment, Integer> assignmentRepo;

    @BeforeEach
    void setUp() {
        studentRepo = new StudentInMemoryRepository(new StudentValidator());
        assignmentRepo = new AssignmentInMemoryRepository(new AssignmentValidator());
        gradeFileRepository = new GradeFileRepository(new GradeValidator(), ApplicationContext.getProperties().getProperty("data.test.catalog.grades"), studentRepo, assignmentRepo);
        path = Paths.get(ApplicationContext.getProperties().getProperty("data.test.catalog.grades"));
        st1 = new Student("asir2446", "Silviu", "Anton", 221, "asir2446@scs.ubbcluj.ro", "Camelia Serban");
        Student st2 = new Student("abcd1235", "St2", "St2L", 221, "st2@scs.ubbcluj.ro", "Camelia Serban");
        as1 = new Assignment(1, "desc1", 6);
        as2 = new Assignment(2, "desc2", 6);
        gradeFileRepository.getStudentRepo().save(st1);
        gradeFileRepository.getStudentRepo().save(st2);
        gradeFileRepository.getAssignmentRepo().save(as1);
        gradeFileRepository.getAssignmentRepo().save(as2);
        gr1 = new Grade(st1, as1, 8.6f, "Prof1");
        gr2 = new Grade(st2, as2, 7.3f, "Prof2");
        gradeFileRepository.save(gr1);
    }

    @AfterEach
    void tearDown() {
        try {
            BufferedWriter bw = Files.newBufferedWriter(path, StandardOpenOption.TRUNCATE_EXISTING);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @Test
    void save() {
        ArrayList<Grade> grades = new ArrayList<>();
        gradeFileRepository.findAll().forEach(grades::add);
        assertEquals(1, grades.size());
        gradeFileRepository.save(gr2);
        grades = new ArrayList<>();
        gradeFileRepository.findAll().forEach(grades::add);
        assertEquals(2, grades.size());
    }

    @Test
    void saveBadId() {
        st1.setId("");
        Grade badGrade = new Grade(st1, as1, 6.5f, "ProfTest");
        assertThrows(InvalidGradeException.class, () -> gradeFileRepository.save(badGrade));
    }

    @Test
    void saveBadValue1() {
        Grade badGrade = new Grade(st1, as1, -5.3f, "ProfTest");
        assertThrows(InvalidGradeException.class, () -> gradeFileRepository.save(badGrade));
    }

    @Test
    void saveBadValue2() {
        Grade badGrade = new Grade(st1, as1, 15.3f, "ProfTest");
        assertThrows(InvalidGradeException.class, () -> gradeFileRepository.save(badGrade));
    }

    @Test
    void saveBadDate() {
        Grade badGrade = new Grade(st1, as1, 6.5f, "ProfTest");
        badGrade.setDate(null);
        assertThrows(InvalidGradeException.class, () -> gradeFileRepository.save(badGrade));
    }

    @Test
    void saveBadProfessor() {
        Grade badGrade = new Grade(st1, as1, 6.5f, "");
        assertThrows(InvalidGradeException.class, () -> gradeFileRepository.save(badGrade));
    }

    @Test
    void saveNullGrade() {
        assertThrows(IllegalArgumentException.class, () -> gradeFileRepository.save(null));
    }

    @Test
    void delete() {
        assertEquals(gr1, gradeFileRepository.delete(new GradeId("asir2446", 1)));
        assertNull(gradeFileRepository.delete(new GradeId("abcd1238", 3)));
    }

    @Test
    void update() {
        gradeFileRepository.update(new Grade(st1, as1, 6.5f, "newProf"));
        assertEquals(new Grade(st1, as1, 6.5f, "newProf"), gradeFileRepository.findOne(new GradeId("asir2446", 1)));
        assertNull(gradeFileRepository.update(new Grade(st1, as2, 6.5f, "newProf")));
    }

    @Test
    void findOne() {
        gradeFileRepository.save(gr2);
        assertEquals(gr1, gradeFileRepository.findOne(new GradeId("asir2446", 1)));
        assertEquals(gr2, gradeFileRepository.findOne(new GradeId("abcd1235", 2)));
        assertNull(gradeFileRepository.findOne(new GradeId("abcd1235", 1)));
    }

    @Test
    void findAll() {
        gradeFileRepository.save(gr2);
        ArrayList<Grade> grades = new ArrayList<>();
        gradeFileRepository.findAll().forEach(grades::add);
        assertEquals(2, grades.size());
    }

    @Test
    void parseEntity() {
        assertEquals(gr1, gradeFileRepository.parseEntity("asir2446/1/30.10.2019/8.6/Prof1"));
    }

    @Test
    void getStudentRepo() {
        assertEquals(studentRepo, gradeFileRepository.getStudentRepo());
    }

    @Test
    void setStudentRepo() {
        InMemoryRepository<Student, String> newRepo = new StudentInMemoryRepository(new StudentValidator());
        gradeFileRepository.setStudentRepo(newRepo);
        assertEquals(newRepo, gradeFileRepository.getStudentRepo());
    }

    @Test
    void getAssignmentRepo() {
        assertEquals(assignmentRepo, gradeFileRepository.getAssignmentRepo());
    }

    @Test
    void setAssignmentRepo() {
        InMemoryRepository<Assignment, Integer> newRepo = new AssignmentInMemoryRepository(new AssignmentValidator());
        gradeFileRepository.setAssignmentRepo(newRepo);
        assertEquals(newRepo, gradeFileRepository.getAssignmentRepo());
    }
}