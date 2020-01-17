package com.silviucanton.domain.auxiliary;

import java.time.LocalDate;

public class AssignGrDTOImpl implements AssignmentGradeDTO {

    private String assignmentName;
    private float grade;
    private LocalDate date;

    public AssignGrDTOImpl(String assignmentName, float grade, LocalDate date) {
        this.assignmentName = assignmentName;
        this.grade = grade;
        this.date = date;
    }

    @Override
    public String getAssignmentName() {
        return assignmentName;
    }

    public void setAssignnmentName(String assingmentName) {
        this.assignmentName = assingmentName;
    }

    @Override
    public float getGrade() {
        return grade;
    }

    public void setGrade(float grade) {
        this.grade = grade;
    }

    @Override
    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }
}
