package services.service;

import domain.entities.Student;
import domain.validators.StudentValidator;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import repositories.memoryPersistence.InMemoryRepository;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class StudentServiceTest {

    private StudentService studentService;

    @BeforeEach
    void setUp() {
        InMemoryRepository<String, Student> studentRepo = new InMemoryRepository<>(new StudentValidator());
        studentRepo.save(new Student("aaaa1234", "St1", "L1", 221, "s1@a.com", "cord1"));
        studentRepo.save(new Student("aaaa1235", "St2", "L2", 221, "s2@a.com", "cord2"));
        studentRepo.save(new Student("aaaa1236", "St3", "L3", 221, "s3@a.com", "cord3"));
        studentRepo.save(new Student("aaaa1237", "St4", "L4", 223, "s4@a.com", "cord4"));
        studentService = new StudentService(studentRepo);
    }

    @AfterEach
    void tearDown() {
        studentService = null;
    }

    @Test
    void filterStudentsByGroup() {
        int count = 0;
        List<Student> res = new ArrayList<>();
        studentService.filterStudentsByGroup(221).forEach(res::add);
        assertEquals(3, res.size());
        res = new ArrayList<>();
        studentService.filterStudentsByGroup(223).forEach(res::add);
        assertEquals(1, res.size());
    }
}