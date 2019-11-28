package com.silviucanton.repositories.xmlPersistence;

import com.silviucanton.domain.entities.Assignment;
import com.silviucanton.domain.validators.AssignmentValidator;
import com.silviucanton.exceptions.InvalidAssignmentException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import com.silviucanton.services.config.ApplicationContext;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class AssignmentXMLRepositoryTest {

    private AssignmentXMLRepository assignmentXMLRepository;
    private Path path;
    private Assignment a1, a2;

    @BeforeEach
    void setUp() {
        assignmentXMLRepository = new AssignmentXMLRepository(new AssignmentValidator(), ApplicationContext.getProperties().getProperty("data.test.catalog.xml.assignments"));
        path = Paths.get(ApplicationContext.getProperties().getProperty("data.test.catalog.xml.assignments"));
        a1 = new Assignment(1, "desc1", 6);
        a2 = new Assignment(2, "desc2", 7);
        assignmentXMLRepository.save(a1);
    }

    @AfterEach
    void tearDown() {
        try {
            Files.write(path, "<assignments>\n\n</assignments>\n".getBytes());
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @Test
    void save() {
        ArrayList<Assignment> assignments = new ArrayList<>();
        assignmentXMLRepository.findAll().forEach(assignments::add);
        assertEquals(1, assignments.size());
        assignmentXMLRepository.save(a2);
        assignments = new ArrayList<>();
        assignmentXMLRepository.findAll().forEach(assignments::add);
        assertEquals(2, assignments.size());
    }

    @Test
    void saveBadId() {
        Assignment badAssignment = new Assignment(-1, "desc1", 6);
        assertThrows(InvalidAssignmentException.class, () -> assignmentXMLRepository.save(badAssignment));
    }

    @Test
    void saveBadDescription() {
        Assignment badAssignment = new Assignment(1, "", 6);
        assertThrows(InvalidAssignmentException.class, () -> assignmentXMLRepository.save(badAssignment));
    }

    @Test
    void saveBadDeadline1() {
        Assignment badAssignment = new Assignment(2, "desc1", 0);
        assertThrows(InvalidAssignmentException.class, () -> assignmentXMLRepository.save(badAssignment));
    }

    @Test
    void saveBadDeadline2() {
        Assignment badAssignment = new Assignment(3, "desc1", 20);
        assertThrows(InvalidAssignmentException.class, () -> assignmentXMLRepository.save(badAssignment));
    }

    @Test
    void saveNullAssignment() {
        assertThrows(IllegalArgumentException.class, () -> assignmentXMLRepository.save(null));
    }

    @Test
    void delete() {
        assertEquals(a1, assignmentXMLRepository.delete(1));
        assertNull(assignmentXMLRepository.delete(5));
    }

    @Test
    void update() {
        assignmentXMLRepository.update(new Assignment(1, "descNew", 6));
        assertEquals(new Assignment(1, "descNew", 6), assignmentXMLRepository.findOne(1));
        assertNull(assignmentXMLRepository.update(new Assignment(5, "dsaf", 6)));
    }

    @Test
    void findOne() {
        assignmentXMLRepository.save(a2);
        assertEquals(a1, assignmentXMLRepository.findOne(1));
        assertEquals(a2, assignmentXMLRepository.findOne(2));
        assertNull(assignmentXMLRepository.findOne(3));
    }

    @Test
    void findAll() {
        assignmentXMLRepository.save(a2);
        ArrayList<Assignment> assignments = new ArrayList<>();
        assignmentXMLRepository.findAll().forEach(assignments::add);
        assertEquals(2, assignments.size());
    }
}