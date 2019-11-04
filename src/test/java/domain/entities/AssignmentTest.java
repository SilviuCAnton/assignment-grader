package domain.entities;

import domain.entities.Assignment;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class AssignmentTest {
    private Assignment a1;
    private Assignment a2;
    private Assignment a3;

    @BeforeEach
    void setUp() {
        a1 = new Assignment(1,"tema 1", 6);
        a2 = new Assignment(2,"tema 2", 5);
        a3 = new Assignment(1,"tema 1", 6);
    }

    @AfterEach
    void tearDown() {
        a1 = null;
        a2 = null;
        a3 = null;
    }

    @Test
    void getId() {
        assertEquals(1, a1.getId());
    }

    @Test
    void setId() {
        a1.setId(5);
        assertEquals(5, a1.getId());
    }

    @Test
    void getDescription() {
        assertEquals("tema 1", a1.getDescription());
    }

    @Test
    void setDescription() {
        a1.setDescription("desc");
        assertEquals("desc", a1.getDescription());
    }

    @Test
    void getStartWeek() {
        assertEquals(5, a1.getStartWeek());
    }

    @Test
    void setStartWeek() {
        a1.setStartWeek(4);
        assertEquals(4, a1.getStartWeek());
    }

    @Test
    void getDeadlineWeek() {
        assertEquals(6, a1.getDeadlineWeek());
    }

    @Test
    void setDeadlineWeek() {
        a1.setDeadlineWeek(6);
        assertEquals(6, a1.getDeadlineWeek());
    }

    @Test
    void testEquals() {
        assertEquals(a1, a3);
        assertNotEquals(a1, a2);
    }

    @Test
    void testHashCode() {
        assertEquals(a1.hashCode(), a3.hashCode());
        assertNotEquals(a1.hashCode(), a2.hashCode());
    }

    @Test
    void testToString() {
        assertEquals("id: 1|description: tema 1|startWeek: 5|deadlineWeek: 6", a1.toString());
    }

    @Test
    void testToFileString() {
        assertEquals("1/tema 1/5/6", a1.toFileString());
    }
}