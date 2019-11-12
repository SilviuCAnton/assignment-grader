package domain.entities;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class StudentTest {

    private Student st1;
    private Student st2;
    private Student st3;

    @BeforeEach
    void setUp() {
        st1 = new Student("aaaa1111", "st1", "st11", 221, "st1@gm.com", "cord1");
        st2 = new Student("aaaa1112", "st2", "st22", 221, "st2@gm.com", "cord2");
        st3 = new Student("aaaa1111", "st1", "st11", 221, "st1@gm.com", "cord1");
    }

    @AfterEach
    void tearDown() {
        st1 = null;
        st2 = null;
        st3 = null;
    }

    @Test
    void getId() {
        assertEquals("aaaa1112", st2.getId());
    }

    @Test
    void setId() {
        st1.setId("bpaa2345");
        assertEquals("bpaa2345", st1.getId());
    }

    @Test
    void getFirstName() {
        assertEquals("st2", st2.getFirstName());
    }

    @Test
    void setFirstName() {
        st1.setFirstName("as");
        assertEquals("as", st1.getFirstName());
    }

    @Test
    void getLastName() {
        assertEquals("st22", st2.getLastName());
    }

    @Test
    void setLastName() {
        st1.setLastName("as");
        assertEquals("as", st1.getLastName());
    }

    @Test
    void getGroup() {
        assertEquals(221, st1.getGroup());
    }

    @Test
    void setGroup() {
        st1.setGroup(223);
        assertEquals(223, st1.getGroup());
    }

    @Test
    void testEquals() {
        assertEquals(st1, st3);
        assertNotEquals(st2, st1);
    }

    @Test
    void testHashCode() {
        assertEquals(st1.hashCode(), st3.hashCode());
        assertNotEquals(st1.hashCode(), st2.hashCode());
    }

    @Test
    void getEmail() {
        assertEquals("st2@gm.com", st2.getEmail());
    }

    @Test
    void setEmail() {
        st1.setEmail("a@a.c");
        assertEquals("a@a.c", st1.getEmail());
    }

    @Test
    void getCoordinator() {
        assertEquals("cord2", st2.getCoordinator());
    }

    @Test
    void setCoordinator() {
        st1.setCoordinator("ana");
        assertEquals("ana", st1.getCoordinator());
    }

    @Test
    void testToString() {
        assertEquals("id: aaaa1111|firstName: st1|lastName: st11|group: 221|email: st1@gm.com|coordinator: cord1", st1.toString());
    }

    @Test
    void testToFileString() {
        assertEquals("aaaa1111/st1/st11/221/st1@gm.com/cord1", st1.toFileString());
    }


}