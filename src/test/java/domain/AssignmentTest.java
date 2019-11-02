package domain;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import utils.Constants;
import utils.Pair;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class AssignmentTest {

    private YearStructure yearStructure;

    @BeforeEach
    void setUp() {

        yearStructure = YearStructure.getInstance(new SemesterStructure(1,
                        LocalDate.parse("30.09.2019", Constants.DATE_TIME_FORMATTER),
                        14,
                        new Pair<>(LocalDate.parse("23.12.2019", Constants.DATE_TIME_FORMATTER), LocalDate.parse("05.01.2020", Constants.DATE_TIME_FORMATTER))),
                new SemesterStructure(2,
                        LocalDate.parse("24.02.2020", Constants.DATE_TIME_FORMATTER),
                        14,
                        new Pair<>(LocalDate.parse("20.04.2019", Constants.DATE_TIME_FORMATTER), LocalDate.parse("26.04.2020", Constants.DATE_TIME_FORMATTER))));

        Assignment a1 = new Assignment(1,"tema 1", 6, yearStructure);
        Assignment a2 = new Assignment(2,"tema 2", 5, yearStructure);
        Assignment a3 = new Assignment(1,"tema 1", 6, yearStructure);
    }

    @AfterEach
    void tearDown() {
        yearStructure = null;
    }

    @Test
    void getId() {
        Assignment a1 = new Assignment(1,"tema 1", 6, yearStructure);
        assertEquals(1, a1.getId());
    }

    @Test
    void setId() {
        Assignment a1 = new Assignment(1,"tema 1", 6, yearStructure);
        a1.setId(5);
        assertEquals(5, a1.getId());
    }

    @Test
    void getDescription() {
        Assignment a1 = new Assignment(1,"tema 1", 6, yearStructure);
        assertEquals("tema 1", a1.getDescription());
    }

    @Test
    void setDescription() {
        Assignment a1 = new Assignment(1,"tema 1", 6, yearStructure);
        a1.setDescription("desc");
        assertEquals("desc", a1.getDescription());
    }

    @Test
    void getStartWeek() {
        Assignment a1 = new Assignment(1,"tema 1", 6, yearStructure);
        assertEquals(5, a1.getStartWeek());
    }

    @Test
    void setStartWeek() {
        Assignment a1 = new Assignment(1,"tema 1", 6, yearStructure);
        a1.setStartWeek(4);
        assertEquals(4, a1.getStartWeek());
    }

    @Test
    void getDeadlineWeek() {
        Assignment a1 = new Assignment(1,"tema 1", 6, yearStructure);
        assertEquals(6, a1.getDeadlineWeek());
    }

    @Test
    void setDeadlineWeek() {
        Assignment a1 = new Assignment(1,"tema 1", 6, yearStructure);
        a1.setDeadlineWeek(6);
        assertEquals(6, a1.getDeadlineWeek());
    }

    @Test
    void testEquals() {
        Assignment a1 = new Assignment(1,"tema 1", 6, yearStructure);
        Assignment a2 = new Assignment(2,"tema 2", 5, yearStructure);
        Assignment a3 = new Assignment(1,"tema 1", 6, yearStructure);
        assertEquals(a1, a3);
        assertNotEquals(a1, a2);
    }

    @Test
    void testHashCode() {
        Assignment a1 = new Assignment(1,"tema 1", 6, yearStructure);
        Assignment a2 = new Assignment(2,"tema 2", 5, yearStructure);
        Assignment a3 = new Assignment(1,"tema 1", 6, yearStructure);
        assertEquals(a1.hashCode(), a3.hashCode());
        assertNotEquals(a1.hashCode(), a2.hashCode());
    }

    @Test
    void testToString() {
        Assignment a1 = new Assignment(1,"tema 1", 6, yearStructure);
        assertEquals("id: 1|description: tema 1|startWeek: 5|deadlineWeek: 6", a1.toString());
    }

    @Test
    void testToFileString() {
        Assignment a1 = new Assignment(1,"tema 1", 6, yearStructure);
        assertEquals("1/tema 1/5/6", a1.toFileString());
    }

    @Test
    void getCurrentWeek() {
        Assignment a1 = new Assignment(1,"tema 1", 6, yearStructure);
        assertEquals(5, a1.getCurrentWeek());
    }
}