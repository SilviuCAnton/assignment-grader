package domain;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import utils.Constants;
import utils.Pair;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class GradeTest {
    private Grade g1,g2,g3;

    @BeforeEach
    void setUp() {
        g1 = new Grade("asir2446", 2, 9.6f, "Camelia Serban");
        g2 = new Grade("bpir2356", 1, 8.5f, "Camelia Serban");
        g3 = new Grade("asir2446", 2, 9.6f, "Camelia Serban");
    }

    @AfterEach
    void tearDown() {
        g1 = null;
        g2 = null;
        g3 = null;
    }

    @Test
    void getId() {
        assertEquals("asir2446", g1.getId().getFirst());
        assertEquals(2, g1.getId().getSecond());
    }

    @Test
    void setId() {
        g1.setId(new Pair<>("aaaa1234",1));
        assertEquals("aaaa1234", g1.getId().getFirst());
        assertEquals(1, g1.getId().getSecond());
    }

    @Test
    void testEquals() {
        assertNotEquals(g1, g2);
        assertEquals(g1, g3);
    }

    @Test
    void testHashCode() {
        assertEquals(g1.hashCode(), g3.hashCode());
        assertNotEquals(g1.hashCode(), g2.hashCode());
    }

    @Test
    void getDate() {
        assertEquals(LocalDate.parse("30.10.2019", Constants.DATE_TIME_FORMATTER), g1.getDate());
    }

    @Test
    void setDate() {
        g1.setDate(LocalDate.parse("10.11.2019", Constants.DATE_TIME_FORMATTER));
        assertEquals(LocalDate.parse("10.11.2019", Constants.DATE_TIME_FORMATTER), g1.getDate());
    }

    @Test
    void getValue() {
        assertEquals(9.6f, g1.getValue());
    }

    @Test
    void setValue() {
        g1.setValue(5.5f);
        assertEquals(5.5f, g1.getValue());
    }

    @Test
    void getProfessor() {
        assertEquals("Camelia Serban", g1.getProfessor());
    }

    @Test
    void testToFileString() {
        assertEquals("asir2446/2/30.10.2019/9.6/Camelia Serban", g1.toFileString());
    }

    @Test
    void testToString() {
        assertEquals("studentId: asir2446|assignmentId: 2|date: 30.10.2019|value: 9.6|professor: Camelia Serban", g1.toString());
    }

    @Test
    void setProfessor() {
        g1.setProfessor("Ana");
        assertEquals("Ana", g1.getProfessor());
    }
}