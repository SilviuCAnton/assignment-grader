package domain.auxiliary;

import domain.auxiliary.FeedbackDTO;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FeedbackDTOTest {

    private FeedbackDTO dto1, dto2, dto3;

    @BeforeEach
    void setUp() {
        dto1 = new FeedbackDTO("Desc1", 8.4f, 3, 4, "feed1");
        dto2 = new FeedbackDTO("Desc2", 7.3f, 2, 2, "feed2");
        dto3 = new FeedbackDTO("Desc1", 8.4f, 3, 4, "feed1");
    }

    @AfterEach
    void tearDown() {
        dto1 = null;
        dto2 = null;
        dto3 = null;
    }

    @Test
    void getAssignmentDescription() {
        assertEquals("Desc1", dto1.getAssignmentDescription());
    }

    @Test
    void setAssignmentDescription() {
        dto1.setAssignmentDescription("newDesc");
        assertEquals("newDesc", dto1.getAssignmentDescription());
    }

    @Test
    void getGrade() {
        assertEquals(8.4f, dto1.getGrade());
    }

    @Test
    void setGrade() {
        dto1.setGrade(5.5f);
        assertEquals(5.5f, dto1.getGrade());
    }

    @Test
    void getSubmissionWeek() {
        assertEquals(3, dto1.getSubmissionWeek());
    }

    @Test
    void setSubmissionWeek() {
        dto1.setSubmissionWeek(4);
        assertEquals(4, dto1.getSubmissionWeek());
    }

    @Test
    void getDeadlineWeek() {
        assertEquals(4, dto1.getDeadlineWeek());
    }

    @Test
    void setDeadlineWeek() {
        dto1.setDeadlineWeek(5);
        assertEquals(5, dto1.getDeadlineWeek());
    }

    @Test
    void getFeedback() {
        assertEquals("feed1", dto1.getFeedback());
    }

    @Test
    void setFeedback() {
        dto1.setFeedback("feedNew");
        assertEquals("feedNew", dto1.getFeedback());
    }

    @Test
    void testEquals() {
        assertEquals(dto1, dto3);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testHashCode() {
        assertEquals(dto1.hashCode(), dto3.hashCode());
        assertNotEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        assertEquals("assignmentDescription: Desc1|feedback: feed1|grade: 8.4|submissionWeek: 3|deadlineWeek: 4", dto1.toString());
    }
}