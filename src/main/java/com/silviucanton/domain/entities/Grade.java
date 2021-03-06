package com.silviucanton.domain.entities;

import com.silviucanton.services.config.ApplicationContext;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Objects;

/**
 * Assignment grade entity
 */
@javax.persistence.Entity
@Table(name = "grades")
public class Grade implements Entity<GradeId> {

    @Id
    private GradeId id;
    @Column(name = "date")
    private LocalDate date;
    @Column(name = "weeknumber")
    private int weekNumber;
    @Column(name = "value")
    private float value;
    @Column(name = "professor")
    private String professor;

    @ManyToOne
    @JoinColumn(name = "studentId", insertable = false, updatable = false)
    private Student student;

    @ManyToOne
    @JoinColumn(name = "assignmentId", insertable = false, updatable = false)
    private Assignment assignment;

    protected Grade() {
    }

    public int getWeekNumber() {
        return weekNumber;
    }

    public void setWeekNumber(int weekNumber) {
        this.weekNumber = weekNumber;
    }

    public Grade(Student student, Assignment assignment, float value, String professor) {
        this.student = student;
        this.assignment = assignment;
        this.date = ApplicationContext.getCurrentLocalDate();
        this.weekNumber = ApplicationContext.getYearStructure().getCurrentWeek(this.date);
        this.value = value;
        this.professor = professor;
        this.setId(new GradeId(student.getId(), assignment.getId()));
    }

    /**
     * returns the student of the grade
     *
     * @return student - Student
     */
    public Student getStudent() {
        return student;
    }

    /**
     * sets the student of the Grade
     *
     * @param student - Student
     */
    public void setStudent(Student student) {
        this.student = student;
    }

    /**
     * returns the assignment of the grade
     *
     * @return assignment - Assignment
     */
    public Assignment getAssignment() {
        return assignment;
    }

    /**
     * sets the assignment of the grade
     *
     * @param assignment - Assignment
     */
    public void setAssignment(Assignment assignment) {
        this.assignment = assignment;
    }

    /**
     * returns the date of the grade
     *
     * @return date - LocalDate
     */
    public LocalDate getDate() {
        return date;
    }

    /**
     * sets the date of the grade
     *
     * @param date - LocalDate
     */
    public void setDate(LocalDate date) {
        this.date = date;
    }

    /**
     * returns the value of the grade
     *
     * @return value - float
     */
    public float getValue() {
        return value;
    }

    /**
     * sets the value of the grade
     *
     * @param value - float
     */
    public void setValue(float value) {
        this.value = value;
    }

    /**
     * returns the professor that gave the grade
     *
     * @return professor - String
     */
    public String getProfessor() {
        return professor;
    }

    /**
     * sets the professor that gave the grade
     *
     * @param professor - String
     */
    public void setProfessor(String professor) {
        this.professor = professor;
    }

    /**
     * returns if two grades are equal or not
     *
     * @param o - other grade - Grade
     * @return true if grades are equal, false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Grade grade = (Grade) o;
        return Float.compare(grade.value, value) == 0 &&
                Objects.equals(date, grade.date) &&
                Objects.equals(professor, grade.professor);
    }

    /**
     * returns the hashcode of the grade
     *
     * @return hashCode - int
     */
    @Override
    public int hashCode() {
        return Objects.hash(date, value, professor);
    }

    @Override
    public GradeId getId() {
        return id;
    }

    @Override
    public void setId(GradeId stringIntegerPair) {
        this.id = stringIntegerPair;
    }

    /**
     * returns the fileString representation of the grade
     *
     * @return gradeFileString - String
     */
    @Override
    public String toFileString() {
        String month = "";
        String day = "";
        if (date.getMonthValue() < 10) {
            month += '0';
        }
        if (date.getDayOfMonth() < 10) {
            day += '0';
        }
        month += date.getMonthValue();
        day += date.getDayOfMonth();
        return getId().getFirst() + '/' +
                getId().getSecond() + '/' +
                day + '.' + month + '.' + date.getYear() + '/' +
                value + '/' +
                professor;
    }

    /**
     * returns the string representation of the grade
     *
     * @return gradeString - String
     */
    @Override
    public String toString() {
        String month = "";
        String day = "";
        if (date.getMonthValue() < 10) {
            month += '0';
        }
        if (date.getDayOfMonth() < 10) {
            day += '0';
        }
        month += date.getMonthValue();
        day += date.getDayOfMonth();
        return "studentId: " + getId().getFirst() + '|' +
                "assignmentId: " + getId().getSecond() + '|' +
                "date: " + day + '.' + month + '.' + date.getYear() + '|' +
                "value: " + value + '|' +
                "professor: " + professor;
    }
}
