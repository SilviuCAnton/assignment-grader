package com.silviucanton.repositories.xmlPersistence;

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

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class GradeXMLRepositoryTest {

    private GradeXMLRepository gradeXMLRepository;
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
        gradeXMLRepository = new GradeXMLRepository(new GradeValidator(), ApplicationContext.getProperties().getProperty("data.test.catalog.xml.grades"), studentRepo, assignmentRepo);
        path = Paths.get(ApplicationContext.getProperties().getProperty("data.test.catalog.xml.grades"));
        st1 = new Student("asir2446", "Silviu", "Anton", 221, "asir2446@scs.ubbcluj.ro", "Camelia Serban");
        Student st2 = new Student("abcd1235", "St2", "St2L", 221, "st2@scs.ubbcluj.ro", "Camelia Serban");
        as1 = new Assignment(1, "desc1", 6);
        as2 = new Assignment(2, "desc2", 6);
        gradeXMLRepository.getStudentRepo().save(st1);
        gradeXMLRepository.getStudentRepo().save(st2);
        gradeXMLRepository.getAssignmentRepo().save(as1);
        gradeXMLRepository.getAssignmentRepo().save(as2);
        gr1 = new Grade(st1, as1, 8.6f, "Prof1");
        gr2 = new Grade(st2, as2, 7.3f, "Prof2");
        gradeXMLRepository.save(gr1);
    }

    @AfterEach
    void tearDown() {
        try {
            Files.write(path, "<grades>\n\n</grades>\n".getBytes());
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @Test
    void save() {
        ArrayList<Grade> grades = new ArrayList<>();
        gradeXMLRepository.findAll().forEach(grades::add);
        assertEquals(1, grades.size());
        gradeXMLRepository.save(gr2);
        grades = new ArrayList<>();
        gradeXMLRepository.findAll().forEach(grades::add);
        assertEquals(2, grades.size());
    }

    @Test
    void saveBadId() {
        st1.setId("");
        Grade badGrade = new Grade(st1, as1, 6.5f, "ProfTest");
        assertThrows(InvalidGradeException.class, () -> gradeXMLRepository.save(badGrade));
    }

    @Test
    void saveBadValue1() {
        Grade badGrade = new Grade(st1, as1, -5.3f, "ProfTest");
        assertThrows(InvalidGradeException.class, () -> gradeXMLRepository.save(badGrade));
    }

    @Test
    void saveBadValue2() {
        Grade badGrade = new Grade(st1, as1, 15.3f, "ProfTest");
        assertThrows(InvalidGradeException.class, () -> gradeXMLRepository.save(badGrade));
    }

    @Test
    void saveBadDate() {
        Grade badGrade = new Grade(st1, as1, 6.5f, "ProfTest");
        badGrade.setDate(null);
        assertThrows(InvalidGradeException.class, () -> gradeXMLRepository.save(badGrade));
    }

    @Test
    void saveBadProfessor() {
        Grade badGrade = new Grade(st1, as1, 6.5f, "");
        assertThrows(InvalidGradeException.class, () -> gradeXMLRepository.save(badGrade));
    }

    @Test
    void saveNullGrade() {
        assertThrows(IllegalArgumentException.class, () -> gradeXMLRepository.save(null));
    }

    @Test
    void delete() {
        assertEquals(gr1, gradeXMLRepository.delete(new GradeId("asir2446", 1)));
        assertNull(gradeXMLRepository.delete(new GradeId("abcd1238", 3)));
    }

    @Test
    void update() {
        gradeXMLRepository.update(new Grade(st1, as1, 6.5f, "newProf"));
        assertEquals(new Grade(st1, as1, 6.5f, "newProf"), gradeXMLRepository.findOne(new GradeId("asir2446", 1)));
        assertNull(gradeXMLRepository.update(new Grade(st1, as2, 6.5f, "newProf")));
    }

    @Test
    void findOne() {
        gradeXMLRepository.save(gr2);
        assertEquals(gr1, gradeXMLRepository.findOne(new GradeId("asir2446", 1)));
        assertEquals(gr2, gradeXMLRepository.findOne(new GradeId("abcd1235", 2)));
        assertNull(gradeXMLRepository.findOne(new GradeId("abcd1235", 1)));
    }

    @Test
    void findAll() {
        gradeXMLRepository.save(gr2);
        ArrayList<Grade> grades = new ArrayList<>();
        gradeXMLRepository.findAll().forEach(grades::add);
        assertEquals(2, grades.size());
    }

    @Test
    void getStudentRepo() {
        assertEquals(studentRepo, gradeXMLRepository.getStudentRepo());
    }

    @Test
    void getAssignmentRepo() {
        assertEquals(assignmentRepo, gradeXMLRepository.getAssignmentRepo());
    }

}