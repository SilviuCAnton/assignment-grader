package com.silviucanton.repositories.xmlPersistence;

import com.silviucanton.domain.entities.Student;
import com.silviucanton.domain.validators.StudentValidator;
import com.silviucanton.exceptions.InvalidStudentException;
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

class StudentXMLRepositoryTest {

    private StudentXMLRepository studentXMLRepository;
    private Path path;
    private Student st1, st2;

    @BeforeEach
    void setUp() {
        studentXMLRepository = new StudentXMLRepository(new StudentValidator(), ApplicationContext.getProperties().getProperty("data.test.catalog.xml.students"));
        path = Paths.get(ApplicationContext.getProperties().getProperty("data.test.catalog.xml.students"));
        st1 = new Student("abcd1234", "St1F", "St1L", 221, "st1@gmail.com", "Cord1");
        st2 = new Student("abcd1235", "St2F", "St2L", 221, "st2@gmail.com", "Cord2");
        studentXMLRepository.save(st1);
    }

    @AfterEach
    void tearDown() {
        try {
            Files.write(path, "<students>\n\n</students>\n".getBytes());
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @Test
    void save() {
        ArrayList<Student> students = new ArrayList<>();
        studentXMLRepository.findAll().forEach(students::add);
        assertEquals(1, students.size());
        studentXMLRepository.save(st2);
        students = new ArrayList<>();
        studentXMLRepository.findAll().forEach(students::add);
        assertEquals(2, students.size());
    }

    @Test
    void saveBadId() {
        Student badStudent = new Student("qa4", "TestF", "TestL", 221, "test@gmail.com", "TestCord");
        assertThrows(InvalidStudentException.class, () -> studentXMLRepository.save(badStudent));
    }

    @Test
    void saveBadFName() {
        Student badStudent = new Student("qazx1234", "", "TestL", 221, "test@gmail.com", "TestCord");
        assertThrows(InvalidStudentException.class, () -> studentXMLRepository.save(badStudent));
    }

    @Test
    void saveBadLName() {
        Student badStudent = new Student("qazx1234", "TestF", "", 221, "test@gmail.com", "TestCord");
        assertThrows(InvalidStudentException.class, () -> studentXMLRepository.save(badStudent));
    }

    @Test
    void saveBadGroup() {
        Student badStudent = new Student("qazx1234", "TestF", "TestL", 4, "test@gmail.com", "TestCord");
        assertThrows(InvalidStudentException.class, () -> studentXMLRepository.save(badStudent));
    }

    @Test
    void saveBadEmail() {
        Student badStudent = new Student("qazx1234", "TestF", "TestL", 221, "test", "TestCord");
        assertThrows(InvalidStudentException.class, () -> studentXMLRepository.save(badStudent));
    }

    @Test
    void saveBadCoord() {
        Student badStudent = new Student("qazx1234", "TestF", "TestL", 221, "test@gmail.com", "");
        assertThrows(InvalidStudentException.class, () -> studentXMLRepository.save(badStudent));
    }

    @Test
    void saveNullStudent() {
        assertThrows(IllegalArgumentException.class, () -> studentXMLRepository.save(null));
    }

    @Test
    void delete() {
        assertEquals(st1, studentXMLRepository.delete("abcd1234"));
        assertNull(studentXMLRepository.delete("qazx1234"));
    }

    @Test
    void update() {
        studentXMLRepository.update(new Student("abcd1234", "1", "1", 222, "1@1.com", "1"));
        assertEquals(new Student("abcd1234", "1", "1", 222, "1@1.com", "1"), studentXMLRepository.findOne("abcd1234"));
        assertNull(studentXMLRepository.update(new Student("qazx1234", "dsaf", "dsafadsf", 235, "1@1.1", "fdsaf")));
    }

    @Test
    void findOne() {
        studentXMLRepository.save(st2);
        assertEquals(st1, studentXMLRepository.findOne("abcd1234"));
        assertEquals(st2, studentXMLRepository.findOne("abcd1235"));
        assertNull(studentXMLRepository.findOne("qazx1234"));
    }

    @Test
    void findAll() {
        studentXMLRepository.save(st2);
        ArrayList<Student> students = new ArrayList<>();
        studentXMLRepository.findAll().forEach(students::add);
        assertEquals(2, students.size());
    }
}