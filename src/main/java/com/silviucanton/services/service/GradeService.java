package com.silviucanton.services.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonWriter;
import com.silviucanton.domain.auxiliary.AssignmentGradeDTO;
import com.silviucanton.domain.auxiliary.FeedbackDTO;
import com.silviucanton.domain.auxiliary.StudentGradeDTO;
import com.silviucanton.domain.entities.Assignment;
import com.silviucanton.domain.entities.Grade;
import com.silviucanton.domain.entities.Student;
import com.silviucanton.exceptions.InvalidGradeException;
import com.silviucanton.repositories.GradeRepository;
import com.silviucanton.services.config.ApplicationContext;
import com.silviucanton.utils.Pair;
import com.silviucanton.utils.observer.Observable;
import com.silviucanton.utils.observer.Observer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.expression.spel.ast.Assign;
import org.springframework.stereotype.Component;

import java.io.BufferedWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Service for grade operations
 */
@Component
public class GradeService implements Service, Observable<GradeService> {
    private GradeRepository gradeRepository;
    private List<Observer<GradeService>> observers = new ArrayList<>();

    @Autowired
    public GradeService(@Qualifier("gradeDatabaseRepository") GradeRepository gradeRepository) {
        this.gradeRepository = gradeRepository;
    }

    /**
     * stores a grade in the grade repository
     *
     * @param studentId       - the id of the student - String
     * @param assignmentId    - the id of the assignment - int
     * @param value           - the value of the grade - float
     * @param professor       - the professor that gives the grade - String
     * @param numberWeeksLate - number of weeks late in giving the grade
     * @param penalty         - grade penalty - int
     * @return the result of the storing operation - Grade
     */
    public Grade addGrade(String studentId, int assignmentId, float value, String professor, int numberWeeksLate, int penalty, boolean motivation, String feedback) {

        float resultValue;
        Grade g = findGrade(studentId, assignmentId);
        if (g != null) {
            throw new InvalidGradeException("A grade already exists given to this student at this assignment.");
        }

        penalty -= numberWeeksLate;
        if(motivation) {
            penalty -= 1;
        }
        if (penalty < 0) {
            penalty = 0;
        }

        if (penalty > 2) {
            throw new InvalidGradeException("You cannot grade this assignment. The student is more than 2 weeks late.");
        }

        Assignment assignment = gradeRepository.getAssignmentRepo().findOne(assignmentId);
        Student student = gradeRepository.getStudentRepo().findOne(studentId);
        if (student == null) {
            throw new IllegalArgumentException("The student does not exist.");
        }

        if (assignment == null) {
            throw new IllegalArgumentException("The assignment does not exist.");
        }

        resultValue = value - penalty;

        if (resultValue < 1) {
            resultValue = 1;
        }

        Grade grade = new Grade(student, assignment, resultValue, professor);
        grade.setDate(grade.getDate().minusWeeks(numberWeeksLate));
        Grade result = gradeRepository.save(grade);

        //Adding feedback in json file
        if (result == null) {
            FeedbackDTO feedbackDTO = new FeedbackDTO(assignment.getDescription(), grade.getValue(), ApplicationContext.getYearStructure().getCurrentWeek(ApplicationContext.getCurrentLocalDate()), assignment.getDeadlineWeek(), feedback);
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            Path path = Paths.get(ApplicationContext.getProperties().getProperty("data.catalog.feedbackPath") + studentId + ".json");
            List<FeedbackDTO> feedbackDTOList = null;

            String jsonStrings;
            try {
                jsonStrings = Files.lines(path).reduce("", (x, y) -> x + y);
                Type targetType = new TypeToken<ArrayList<FeedbackDTO>>() {
                }.getType();
                feedbackDTOList = gson.fromJson(jsonStrings, targetType);
                if (feedbackDTOList == null) {
                    feedbackDTOList = new ArrayList<>();
                }
                feedbackDTOList.add(feedbackDTO);
            } catch (IOException e) {
                e.printStackTrace();
            }


            try (BufferedWriter bf = Files.newBufferedWriter(path, StandardCharsets.UTF_8)) {
                JsonElement tree = gson.toJsonTree(feedbackDTOList);
                JsonWriter jsonWriter = gson.newJsonWriter(bf);
                jsonWriter.setLenient(true);
                gson.toJson(tree, jsonWriter);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        notifyObservers();
        return result;
    }

    /**
     * removes a grade from the grade repository
     *
     * @param studentId    - id of the student - String
     * @param assignmentId - id of the assignment - int
     * @return result of the deletion operation - Grade
     */
    public Grade removeGrade(String studentId, int assignmentId) {
        Pair<String, Integer> gradeId = new Pair<>(studentId, assignmentId);
        notifyObservers();
        return gradeRepository.delete(gradeId);
    }

    /**
     * updates a grade in the grade repository
     *
     * @param studentId    - the id of the student - String
     * @param assignmentId - the id of the assignment - int
     * @param value        - the value of the grade
     * @param professor    - the professor giving the grade
     * @return result of update operation - Grade
     */
    public Grade updateGrade(String studentId, int assignmentId, float value, String professor) {
        Student student = gradeRepository.getStudentRepo().findOne(studentId);
        Assignment assignment = gradeRepository.getAssignmentRepo().findOne(assignmentId);
        Grade grade = new Grade(student, assignment, value, professor);
        Grade res = gradeRepository.update(grade);
        notifyObservers();
        return gradeRepository.update(grade);
    }

    /**
     * finds a grade in the grade repository
     *
     * @param studentId    - id of the student
     * @param assignmentId - id of the assignment
     * @return grade - Grade
     */
    public Grade findGrade(String studentId, int assignmentId) {
        return gradeRepository.findOne(new Pair<>(studentId, assignmentId));
    }

    /**
     * returns all grades
     *
     * @return grades - list of Grade
     */
    public List<Grade> getAllGrades() {
        List<Grade> grades = new ArrayList<>();
        gradeRepository.findAll().forEach(grades::add);
        return grades;
    }

    public List<Student> getStudents() {
        List<Student> students = new ArrayList<>();
        gradeRepository.getStudentRepo().findAll().forEach(students::add);
        return students;
    }

    public List<Assignment> getAssignments() {
        List<Assignment> assignments = new ArrayList<>();
        gradeRepository.getAssignmentRepo().findAll().forEach(assignments::add);
        return assignments;
    }

    /**
     * returns the grade penalty
     *
     * @param assignmentId - the id of the assignment - int
     * @return penalty - int
     */
    public int getGradePenalty(int assignmentId) {
        int penalty = 0;
        Assignment assignment = gradeRepository.getAssignmentRepo().findOne(assignmentId);
        if (assignment == null) {
            throw new IllegalArgumentException("The assignment does not exist.");
        }
        if (ApplicationContext.getYearStructure().getCurrentWeek(ApplicationContext.getCurrentLocalDate()) > assignment.getDeadlineWeek()) {
            penalty = ApplicationContext.getYearStructure().getCurrentWeek(ApplicationContext.getCurrentLocalDate()) - assignment.getDeadlineWeek();
        }
        return penalty;
    }

    /**
     * filters students by submitted assignment
     *
     * @param assignmentId - int
     * @return the filtered students - set of Student
     */
    public Set<Student> filterStudentsBySubmission(int assignmentId) {
        return getAllGrades().stream()
                .filter(x -> x.getAssignment().getId() == assignmentId)
                .map(Grade::getStudent)
                .collect(Collectors.toSet());
    }

    /**
     * filters students by submitted assignment and professor
     *
     * @param assignmentId - int
     * @param professor    - String
     * @return the filtered students - set of Student
     */
    public Set<Student> filterStudentsBySubmissionAndProfessor(int assignmentId, String professor) {
        return getAllGrades().stream()
                .filter(x -> x.getAssignment().getId() == assignmentId)
                .filter(x -> professor.equals(x.getProfessor()))
                .map(Grade::getStudent)
                .collect(Collectors.toSet());
    }

    /**
     * filters grades by submitted assignment and submission week
     *
     * @param assignmentId - int
     * @param weekNumber   - int
     * @return the filtered grades - list of Grade
     */
    public List<Grade> filterGradesByAssignmentAndWeek(int assignmentId, int weekNumber) {
        return getAllGrades().stream()
                .filter(x -> x.getAssignment().getId() == assignmentId)
                .filter(x -> ApplicationContext.getYearStructure().getCurrentWeek(x.getDate()) == weekNumber)
                .collect(Collectors.toList());
    }

    public Map<Integer, Integer> getFinalGradesStatistics() {
        Map<Integer, Integer> gradeStatistics = new HashMap<>();
        for(int i = 1; i < 10; i++) {
            gradeStatistics.putIfAbsent(i, 0);
        }
        getFinalGrades().forEach(studentGradeDTO -> gradeStatistics.replace((int) studentGradeDTO.getFinalGrade(), gradeStatistics.get((int) studentGradeDTO.getFinalGrade()) + 1));
        return gradeStatistics;
    }

    public List<StudentGradeDTO> getFinalGrades() {
        List<StudentGradeDTO> studentGradeDTOS = new ArrayList<>();
        float gradeSum = 0;
        int nGrades = 0;
        for(Student student : getStudents()) {
            for(Assignment assignment : getAssignments()) {
                Grade grade = findGrade(student.getId(), assignment.getId());
                if(grade != null) {
                    gradeSum+= grade.getValue()*(assignment.getDeadlineWeek()-assignment.getStartWeek());
                } else {
                    gradeSum+= (assignment.getDeadlineWeek()-assignment.getStartWeek());
                }
                nGrades+= (assignment.getDeadlineWeek()-assignment.getStartWeek());
            }
            studentGradeDTOS.add(new StudentGradeDTO(student.getFirstName() + ' ' + student.getLastName(), gradeSum/nGrades));
            gradeSum = 0;
            nGrades = 0;
        }

        return studentGradeDTOS;
    }

    public List<StudentGradeDTO> getPassedStudents() {
        return getFinalGrades().stream().filter(x->x.getFinalGrade()>=4).collect(Collectors.toList());
    }

    public List<StudentGradeDTO> getNeverLateStudents() {
        List<StudentGradeDTO> studentGradeDTOS = getFinalGrades();
        List<Student> students = new ArrayList<>();
        boolean neverLate = true;
        for(Student student : getStudents()) {
            for(Assignment assignment : getAssignments()) {
                Grade grade = findGrade(student.getId(), assignment.getId());
                if(grade == null || ApplicationContext.getYearStructure().getCurrentWeek(grade.getDate()) > assignment.getDeadlineWeek()) {
                    neverLate = false;
                    break;
                }
            }
            if(neverLate) {
                students.add(student);
            } else {
                neverLate = true;
            }
        }
        return studentGradeDTOS.stream()
                .filter(studentDTO-> students.stream()
                        .filter(x->(x.getFirstName()+' ' + x.getLastName()).equals(studentDTO.getStudentName()))
                        .count()==1)
                .collect(Collectors.toList());
    }

public AssignmentGradeDTO getHardestAssignment() {
        AssignmentGradeDTO minAssignment = new AssignmentGradeDTO("tst", 11, LocalDate.now());
        int sum = 0;
        float gradeSum = 0, minGrade = 11;
        for(Assignment assignment : getAssignments()) {
            for(Student student : getStudents()) {
                Grade grade = findGrade(student.getId(), assignment.getId());
                if(grade == null) {
                    gradeSum += 1;
                } else {
                    gradeSum += grade.getValue();
                }
                sum++;
            }
            if(gradeSum/sum < minGrade) {
                minAssignment.setAssignmentName(assignment.getDescription());
                minAssignment.setGrade(gradeSum/sum);
            }
        }
        return minAssignment;
    }


    @Override
    public void addObserver(Observer<GradeService> e) {
        observers.add(e);
    }

    @Override
    public void removeObserver(Observer<GradeService> e) {
        observers.remove(e);
    }

    @Override
    public void notifyObservers() {
        observers.forEach(x -> x.update(this));
    }
}
