package domain;

public class FeedbackDTO {
    private String assignmentDescription;
    private float grade;
    private int submissionWeek, deadlineWeek;

    public FeedbackDTO(String assignmentDescription, float grade, int submissionWeek, int deadlineWeek) {
        this.assignmentDescription = assignmentDescription;
        this.grade = grade;
        this.submissionWeek = submissionWeek;
        this.deadlineWeek = deadlineWeek;
    }

    public String getAssignmentDescription() {
        return assignmentDescription;
    }

    public void setAssignmentDescription(String assignmentDescription) {
        this.assignmentDescription = assignmentDescription;
    }

    public float getGrade() {
        return grade;
    }

    public void setGrade(float grade) {
        this.grade = grade;
    }

    public int getSubmissionWeek() {
        return submissionWeek;
    }

    public void setSubmissionWeek(int submissionWeek) {
        this.submissionWeek = submissionWeek;
    }

    public int getDeadlineWeek() {
        return deadlineWeek;
    }

    public void setDeadlineWeek(int deadlineWeek) {
        this.deadlineWeek = deadlineWeek;
    }
}
