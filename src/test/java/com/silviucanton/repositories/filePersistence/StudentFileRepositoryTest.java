package com.silviucanton.repositories.filePersistence;

import com.silviucanton.domain.entities.Student;
import com.silviucanton.domain.validators.StudentValidator;
import com.silviucanton.exceptions.InvalidStudentException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import com.silviucanton.services.config.ApplicationContext;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class StudentFileRepositoryTest {

    private StudentFileRepository studentFileRepository;
    private Path path;
    private Student st1, st2;

    @BeforeEach
    void setUp() {
        studentFileRepository = new StudentFileRepository(new StudentValidator(), ApplicationContext.getProperties().getProperty("data.test.catalog.students"));
        path = Paths.get(ApplicationContext.getProperties().getProperty("data.test.catalog.students"));
        st1 = new Student("abcd1234", "St1F", "St1L", 221, "st1@gmail.com", "Cord1");
        st2 = new Student("abcd1235", "St2F", "St2L", 221, "st2@gmail.com", "Cord2");
        studentFileRepository.save(st1);
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
        ArrayList<Student> students = new ArrayList<>();
        studentFileRepository.findAll().forEach(students::add);
        assertEquals(1, students.size());
        studentFileRepository.save(st2);
        students = new ArrayList<>();
        studentFileRepository.findAll().forEach(students::add);
        assertEquals(2, students.size());
    }

    @Test
    void saveBadId() {
        Student badStudent = new Student("qa4", "TestF", "TestL", 221, "test@gmail.com", "TestCord");
        assertThrows(InvalidStudentException.class, () -> studentFileRepository.save(badStudent));
    }

    @Test
    void saveBadFName() {
        Student badStudent = new Student("qazx1234", "", "TestL", 221, "test@gmail.com", "TestCord");
        assertThrows(InvalidStudentException.class, () -> studentFileRepository.save(badStudent));
    }

    @Test
    void saveBadLName() {
        Student badStudent = new Student("qazx1234", "TestF", "", 221, "test@gmail.com", "TestCord");
        assertThrows(InvalidStudentException.class, () -> studentFileRepository.save(badStudent));
    }

    @Test
    void saveBadGroup() {
        Student badStudent = new Student("qazx1234", "TestF", "TestL", 4, "test@gmail.com", "TestCord");
        assertThrows(InvalidStudentException.class, () -> studentFileRepository.save(badStudent));
    }

    @Test
    void saveBadEmail() {
        Student badStudent = new Student("qazx1234", "TestF", "TestL", 221, "test", "TestCord");
        assertThrows(InvalidStudentException.class, () -> studentFileRepository.save(badStudent));
    }

    @Test
    void saveBadCoord() {
        Student badStudent = new Student("qazx1234", "TestF", "TestL", 221, "test@gmail.com", "");
        assertThrows(InvalidStudentException.class, () -> studentFileRepository.save(badStudent));
    }

    @Test
    void saveNullStudent() {
        assertThrows(IllegalArgumentException.class, () -> studentFileRepository.save(null));
    }

    @Test
    void delete() {
        assertEquals(st1, studentFileRepository.delete("abcd1234"));
        assertNull(studentFileRepository.delete("qazx1234"));
    }

    @Test
    void update() {
        studentFileRepository.update(new Student("abcd1234", "1", "1", 222, "1@1.com", "1"));
        assertEquals(new Student("abcd1234", "1", "1", 222, "1@1.com", "1"), studentFileRepository.findOne("abcd1234"));
        assertNull(studentFileRepository.update(new Student("qazx1234", "dsaf", "dsafadsf", 235, "1@1.1", "fdsaf")));
    }

    @Test
    void findOne() {
        studentFileRepository.save(st2);
        assertEquals(st1, studentFileRepository.findOne("abcd1234"));
        assertEquals(st2, studentFileRepository.findOne("abcd1235"));
        assertNull(studentFileRepository.findOne("qazx1234"));
    }

    @Test
    void findAll() {
        studentFileRepository.save(st2);
        ArrayList<Student> students = new ArrayList<>();
        studentFileRepository.findAll().forEach(students::add);
        assertEquals(2, students.size());
    }

    @Test
    void parseEntity() {
        assertEquals(st1, studentFileRepository.parseEntity("abcd1234/St1F/St1L/221/st1@gmail.com/Cord1"));
    }
}