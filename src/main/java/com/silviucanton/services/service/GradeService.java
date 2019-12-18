package com.silviucanton.services.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonWriter;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.silviucanton.domain.auxiliary.AssignmentGradeDTO;
import com.silviucanton.domain.auxiliary.FeedbackDTO;
import com.silviucanton.domain.auxiliary.StudentGradeDTO;
import com.silviucanton.domain.entities.Assignment;
import com.silviucanton.domain.entities.Grade;
import com.silviucanton.domain.entities.GradeId;
import com.silviucanton.domain.entities.Student;
import com.silviucanton.domain.validators.Validator;
import com.silviucanton.exceptions.InvalidGradeException;
import com.silviucanton.repositories.CrudRepository;
import com.silviucanton.repositories.Repository;
import com.silviucanton.services.config.ApplicationContext;
import com.silviucanton.utils.observer.Observable;
import com.silviucanton.utils.observer.Observer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.PageRequest;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
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
@org.springframework.stereotype.Service
public class GradeService implements Service, Observable<GradeService> {
    private Validator<Grade> validator;
    private CrudRepository<Grade, GradeId> gradeRepository;
    private CrudRepository<Student, String> studentRepository;
    private CrudRepository<Assignment, Integer> assignmentRepository;
    private List<Observer<GradeService>> observers = new ArrayList<>();

    @Autowired
    public GradeService(@Qualifier("gradeDatabaseRepository") Repository<Grade, GradeId> gradeRepository, @Qualifier("studentDatabaseRepository") Repository<Student, String> studentRepository, @Qualifier("assignmentDatabaseRepository") Repository<Assignment, Integer> assignmentRepository, @Qualifier("gradeValidator") Validator<Grade> validator) {
        this.gradeRepository = (CrudRepository<Grade, GradeId>) gradeRepository;
        this.studentRepository = (CrudRepository<Student, String>) studentRepository;
        this.assignmentRepository = (CrudRepository<Assignment, Integer>) assignmentRepository;
        this.validator = validator;
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
        Optional<Grade> g = findGrade(studentId, assignmentId);
        if (g.isPresent()) {
            throw new InvalidGradeException("A grade already exists given to this student at this assignment.");
        }

        penalty -= numberWeeksLate;
        if (motivation) {
            penalty -= 1;
        }
        if (penalty < 0) {
            penalty = 0;
        }

        if (penalty > 2) {
            throw new InvalidGradeException("You cannot grade this assignment. The student is more than 2 weeks late.");
        }

        Optional<Assignment> assignment = assignmentRepository.findById(assignmentId);
        Optional<Student> student = studentRepository.findById(studentId);
        if (!student.isPresent()) {
            throw new IllegalArgumentException("The student does not exist.");
        }

        if (!assignment.isPresent()) {
            throw new IllegalArgumentException("The assignment does not exist.");
        }

        resultValue = value - penalty;

        if (resultValue < 1) {
            resultValue = 1;
        }

        Grade grade = new Grade(student.get(), assignment.get(), resultValue, professor);
        grade.setDate(grade.getDate().minusWeeks(numberWeeksLate));
        validator.validate(grade);
        gradeRepository.save(grade);
        Optional<Grade> result = gradeRepository.findById(grade.getId());
        //Adding feedback in json file
        if (result.isPresent()) {
            FeedbackDTO feedbackDTO = new FeedbackDTO(assignment.get().getDescription(), grade.getValue(), ApplicationContext.getYearStructure().getCurrentWeek(ApplicationContext.getCurrentLocalDate()), assignment.get().getDeadlineWeek(), feedback);
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
        return result.get();
    }

    /**
     * removes a grade from the grade repository
     *
     * @param studentId    - id of the student - String
     * @param assignmentId - id of the assignment - int
     */
    public void removeGrade(String studentId, int assignmentId) {
        GradeId gradeId = new GradeId(studentId, assignmentId);
        gradeRepository.deleteById(gradeId);
        notifyObservers();
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
        Optional<Student> student = studentRepository.findById(studentId);
        Optional<Assignment> assignment = assignmentRepository.findById(assignmentId);
        Grade grade = new Grade(student.get(), assignment.get(), value, professor);
        validator.validate(grade);
        gradeRepository.save(grade);
        notifyObservers();
        return grade;
    }

    /**
     * finds a grade in the grade repository
     *
     * @param studentId    - id of the student
     * @param assignmentId - id of the assignment
     * @return grade - Grade
     */
    public Optional<Grade> findGrade(String studentId, int assignmentId) {
        return gradeRepository.findById(new GradeId(studentId, assignmentId));
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
        studentRepository.findAll().forEach(students::add);
        return students;
    }

    public List<Student> findAllStudentsByPage(int pageIndex, int numberOfStudentPerPage) {
        List<Student> studentList = new ArrayList<>();
        studentRepository.findAll(PageRequest.of(pageIndex, numberOfStudentPerPage)).forEach(studentList::add);
        return studentList;
    }

    public List<Assignment> getAssignments() {
        List<Assignment> assignments = new ArrayList<>();
        assignmentRepository.findAll().forEach(assignments::add);
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
        Optional<Assignment> assignment = assignmentRepository.findById(assignmentId);
        if (!assignment.isPresent()) {
            throw new IllegalArgumentException("The assignment does not exist.");
        }
        if (ApplicationContext.getYearStructure().getCurrentWeek(ApplicationContext.getCurrentLocalDate()) > assignment.get().getDeadlineWeek()) {
            penalty = ApplicationContext.getYearStructure().getCurrentWeek(ApplicationContext.getCurrentLocalDate()) - assignment.get().getDeadlineWeek();
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

    public Optional<Assignment> findAssignmentByID(int id) {
        return assignmentRepository.findById(id);
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
        for (int i = 1; i < 10; i++) {
            gradeStatistics.putIfAbsent(i, 0);
        }
        getFinalGrades().forEach(studentGradeDTO -> gradeStatistics.replace((int) studentGradeDTO.getFinalGrade(), gradeStatistics.get((int) studentGradeDTO.getFinalGrade()) + 1));
        return gradeStatistics;
    }

    public List<StudentGradeDTO> getFinalGrades() {
        List<StudentGradeDTO> studentGradeDTOS = new ArrayList<>();
        float gradeSum = 0;
        int nGrades = 0;
        List<Assignment> assignments = getAssignments();
        for (Student student : getStudents()) {
            for (Assignment assignment : assignments) {
                Optional<Grade> grade = findGrade(student.getId(), assignment.getId());
                if (grade.isPresent()) {
                    gradeSum += grade.get().getValue() * (assignment.getDeadlineWeek() - assignment.getStartWeek());
                } else {
                    gradeSum += (assignment.getDeadlineWeek() - assignment.getStartWeek());
                }
                nGrades += (assignment.getDeadlineWeek() - assignment.getStartWeek());
            }
            studentGradeDTOS.add(new StudentGradeDTO(student.getFirstName() + ' ' + student.getLastName(), gradeSum / nGrades));
            gradeSum = 0;
            nGrades = 0;
        }

        return studentGradeDTOS;
    }

    public List<StudentGradeDTO> getPassedStudents() {
        return getFinalGrades().stream().filter(x -> x.getFinalGrade() >= 4).collect(Collectors.toList());
    }

    public List<StudentGradeDTO> getNeverLateStudents(List<StudentGradeDTO> finalGrades) {
        List<Student> students = new ArrayList<>();
        boolean neverLate = true;
        List<Assignment> assignments = getAssignments();
        List<Student> allStudents = getStudents();
        for (Student student : allStudents) {
            for (Assignment assignment : assignments) {
                Optional<Grade> grade = findGrade(student.getId(), assignment.getId());
                if (!grade.isPresent() || ApplicationContext.getYearStructure().getCurrentWeek(grade.get().getDate()) > assignment.getDeadlineWeek()) {
                    neverLate = false;
                    break;
                }
            }
            if (neverLate) {
                students.add(student);
            } else {
                neverLate = true;
            }
        }
        return finalGrades.stream()
                .filter(studentDTO -> students.stream()
                        .filter(x -> (x.getFirstName() + ' ' + x.getLastName()).equals(studentDTO.getStudentName()))
                        .count() == 1)
                .collect(Collectors.toList());
    }

    public AssignmentGradeDTO getHardestAssignment() {
        AssignmentGradeDTO minAssignment = new AssignmentGradeDTO("tst", 11, LocalDate.now());
        int sum = 0;
        float gradeSum = 0, minGrade = 11;
        List<Student> students = getStudents();
        List<Assignment> assignments = getAssignments();
        for (Assignment assignment : assignments) {
            for (Student student : students) {
                Optional<Grade> grade = findGrade(student.getId(), assignment.getId());
                if (!grade.isPresent()) {
                    gradeSum += 1;
                } else {
                    gradeSum += grade.get().getValue();
                }
                sum++;
            }
            if (gradeSum / sum < minGrade) {
                minAssignment.setAssignmentName(assignment.getDescription());
                minAssignment.setGrade(gradeSum / sum);
            }
        }
        return minAssignment;
    }

    private void addTableHeader(PdfPTable table, String h1, String h2) {
        PdfPCell header1 = new PdfPCell();
        PdfPCell header2 = new PdfPCell();
        header1.setBackgroundColor(BaseColor.LIGHT_GRAY);
        header1.setBorderWidth(2);
        header1.setPhrase(new Phrase(h1));
        header2.setBackgroundColor(BaseColor.LIGHT_GRAY);
        header2.setBorderWidth(2);
        header2.setPhrase(new Phrase(h2));
        table.addCell(header1);
        table.addCell(header2);
    }

    public void exportPdfFinalGrades(String path) {
        Document document = new Document();
        try {
            PdfWriter.getInstance(document, new FileOutputStream(path, false));
            document.open();
            PdfPTable table = new PdfPTable(2);
            addTableHeader(table, "Name", "Final Grade");
            addFinalGradesRows(table, getFinalGrades());
            document.add(table);
            document.close();
        } catch (DocumentException | FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void addFinalGradesRows(PdfPTable table, List<StudentGradeDTO> aList) {
        aList.forEach(x -> {
            PdfPCell cell1 = new PdfPCell();
            cell1.setPhrase(new Phrase(x.getStudentName()));
            table.addCell(cell1);
            PdfPCell cell2 = new PdfPCell();
            cell2.setPhrase(new Phrase(String.valueOf(x.getFinalGrade())));
            table.addCell(cell2);
        });
    }

    public void exportPdfPassedStudents(String path) {
        Document document = new Document();
        try {
            PdfWriter.getInstance(document, new FileOutputStream(path, false));
            document.open();
            PdfPTable table = new PdfPTable(2);
            addTableHeader(table, "Name", "Final Grade");
            addFinalGradesRows(table, getPassedStudents());
            document.add(table);
            document.close();
        } catch (DocumentException | FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void exportPdfNeverLateStudents(String path) {
        Document document = new Document();
        try {
            PdfWriter.getInstance(document, new FileOutputStream(path, false));
            document.open();
            PdfPTable table = new PdfPTable(2);
            addTableHeader(table, "Name", "Final Grade");
            addFinalGradesRows(table, getNeverLateStudents(getFinalGrades()));
            document.add(table);
            document.close();
        } catch (DocumentException | FileNotFoundException e) {
            e.printStackTrace();
        }
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
