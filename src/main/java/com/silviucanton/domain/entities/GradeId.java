package com.silviucanton.domain.entities;

import com.silviucanton.utils.Pair;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Table;
import java.util.Objects;

@Embeddable
@Table(name = "grades")
public class GradeId implements Pair<String, Integer> {

    @Column(name = "studentid")
    private String studentId;
    @Column(name = "assignmentid")
    private Integer assignmentId;

    public GradeId(String studentId, Integer assignmentId) {
        this.studentId = studentId;
        this.assignmentId = assignmentId;
    }

    protected GradeId() {
    }


    @Override
    public String getFirst() {
        return studentId;
    }

    @Override
    public void setFirst(String first) {
        this.studentId = first;
    }

    @Override
    public Integer getSecond() {
        return assignmentId;
    }

    @Override
    public void setSecond(Integer second) {
        this.assignmentId = second;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GradeId gradeId = (GradeId) o;
        return Objects.equals(studentId, gradeId.studentId) &&
                Objects.equals(assignmentId, gradeId.assignmentId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(studentId, assignmentId);
    }
}
