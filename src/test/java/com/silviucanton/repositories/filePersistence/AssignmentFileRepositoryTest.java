package com.silviucanton.repositories.filePersistence;

import com.silviucanton.domain.entities.Assignment;
import com.silviucanton.domain.validators.AssignmentValidator;
import com.silviucanton.exceptions.InvalidAssignmentException;
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

class AssignmentFileRepositoryTest {

    private AssignmentFileRepository assignmentFileRepository;
    private Path path;
    private Assignment a1, a2;

    @BeforeEach
    void setUp() {
        assignmentFileRepository = new AssignmentFileRepository(new AssignmentValidator(), ApplicationContext.getProperties().getProperty("data.test.catalog.assignments"));
        path = Paths.get(ApplicationContext.getProperties().getProperty("data.test.catalog.assignments"));
        a1 = new Assignment(1, "desc1", 6);
        a2 = new Assignment(2, "desc2", 7);
        assignmentFileRepository.save(a1);
    }

    @AfterEach
    void tearDown() {
        try {
            BufferedWriter bf = Files.newBufferedWriter(path, StandardOpenOption.TRUNCATE_EXISTING);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @Test
    void save() {
        ArrayList<Assignment> assignments = new ArrayList<>();
        assignmentFileRepository.findAll().forEach(assignments::add);
        assertEquals(1, assignments.size());
        assignmentFileRepository.save(a2);
        assignments = new ArrayList<>();
        assignmentFileRepository.findAll().forEach(assignments::add);
        assertEquals(2, assignments.size());
    }

    @Test
    void saveBadId() {
        Assignment badAssignment = new Assignment(-1, "desc1", 6);
        assertThrows(InvalidAssignmentException.class, () -> assignmentFileRepository.save(badAssignment));
    }

    @Test
    void saveBadDescription() {
        Assignment badAssignment = new Assignment(1, "", 6);
        assertThrows(InvalidAssignmentException.class, () -> assignmentFileRepository.save(badAssignment));
    }

    @Test
    void saveBadDeadline1() {
        Assignment badAssignment = new Assignment(2, "desc1", 0);
        assertThrows(InvalidAssignmentException.class, () -> assignmentFileRepository.save(badAssignment));
    }

    @Test
    void saveBadDeadline2() {
        Assignment badAssignment = new Assignment(3, "desc1", 20);
        assertThrows(InvalidAssignmentException.class, () -> assignmentFileRepository.save(badAssignment));
    }

    @Test
    void saveNullAssignment() {
        assertThrows(IllegalArgumentException.class, () -> assignmentFileRepository.save(null));
    }

    @Test
    void delete() {
        assertEquals(a1, assignmentFileRepository.delete(1));
        assertNull(assignmentFileRepository.delete(5));
    }

    @Test
    void update() {
        assignmentFileRepository.update(new Assignment(1, "descNew", 6));
        assertEquals(new Assignment(1, "descNew", 6), assignmentFileRepository.findOne(1));
        assertNull(assignmentFileRepository.update(new Assignment(5, "dsaf", 6)));
    }

    @Test
    void findOne() {
        assignmentFileRepository.save(a2);
        assertEquals(a1, assignmentFileRepository.findOne(1));
        assertEquals(a2, assignmentFileRepository.findOne(2));
        assertNull(assignmentFileRepository.findOne(3));
    }

    @Test
    void findAll() {
        assignmentFileRepository.save(a2);
        ArrayList<Assignment> assignments = new ArrayList<>();
        assignmentFileRepository.findAll().forEach(assignments::add);
        assertEquals(2, assignments.size());
    }

    @Test
    void parseEntity() {
        assertEquals(a1, assignmentFileRepository.parseEntity("1/desc1/5/6"));
    }
}