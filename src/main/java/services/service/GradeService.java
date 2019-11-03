package services.service;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import domain.Assignment;
import domain.FeedbackDTO;
import domain.Grade;
import domain.Student;
import domain.validators.GradeValidator;
import exceptions.InvalidGradeException;
import repositories.GradeFileRepository;
import services.config.ApplicationContext;
import utils.Pair;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Service for grade operations
 */
public class GradeService {
    private StudentService studentService;
    private AssignmentService assignmentService;
    private GradeFileRepository gradeFileRepository;

    public GradeService(StudentService studentService, AssignmentService assignmentService, String fileName) {
        this.studentService = studentService;
        this.assignmentService = assignmentService;
        this.gradeFileRepository = new GradeFileRepository(new GradeValidator(), fileName);
    }

    /**
     * stores a grade in the grade repository
     * @param studentId - the id of the student - String
     * @param assignmentId - the id of the assignment - int
     * @param value - the value of the grade - float
     * @param professor - the professor that gives the grade - String
     * @param numberWeeksLate - number of weeks late in giving the grade
     * @param penalty - grade penalty - int
     * @return the result of the storing operation - Grade
     */
    public Grade addGrade(String studentId, int assignmentId, float value, String professor, int numberWeeksLate, int penalty, boolean motivation, String feedback) {

        Grade g = findGrade(studentId, assignmentId);
        if(g != null) {
            throw new InvalidGradeException("A grade already exists given to this student at this assignment.");
        }

        penalty -= numberWeeksLate;
        if(penalty < 0) {
            penalty = 0;
        }

        if(penalty > 3 || (!motivation && penalty > 2)) {
            throw new InvalidGradeException("You cannot grade this assignment. The student is more than 2 weeks late.");
        }

        Assignment assignment = assignmentService.findAssignment(assignmentId);
        Student student = studentService.findStudent(studentId);
        if(student == null) {
            throw new IllegalArgumentException("The student does not exist.");
        }

        Grade grade = new Grade(studentId, assignmentId, value - penalty, professor);
        Grade result = gradeFileRepository.save(grade);

        //Adding feedback in json file
        if(result == null){
            FeedbackDTO feedbackDTO = new FeedbackDTO(assignment.getDescription(), grade.getValue(), ApplicationContext.getYearStructure().getCurrentWeek(), assignment.getDeadlineWeek(), feedback);
            Gson gson = new Gson();
            Path path = Paths.get(ApplicationContext.getProperties().getProperty("data.catalog.feedbackPath") + studentId + ".json");
            Collection<FeedbackDTO> feedbackDTOList;
            String jsonString = "";
            try(BufferedReader br = Files.newBufferedReader(path)) {
                String jsonStrings = br.readLine();
                Type targetType = new TypeToken<ArrayList<FeedbackDTO>>() {}.getType();
                feedbackDTOList = gson.fromJson(jsonStrings, targetType);
                if(feedbackDTOList == null) {
                    feedbackDTOList = new ArrayList<>();
                }
                feedbackDTOList.add(feedbackDTO);
                jsonString = gson.toJson(feedbackDTOList);
            } catch (IOException e) {
                e.printStackTrace();
            }

            try(BufferedWriter bf = Files.newBufferedWriter(path, StandardOpenOption.TRUNCATE_EXISTING)) {
                try {
                    bf.write(jsonString);
                    bf.newLine();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } catch (IOException e) { e.printStackTrace(); }
        }

        return result;
    }

    /**
     * removes a grade from the grade repository
     * @param studentId - id of the student - String
     * @param assignmentId - id of the assignment - int
     * @return result of the deletion operation - Grade
     */
    public Grade removeGrade(String studentId, int assignmentId) {
        Pair<String, Integer> gradeId = new Pair<>(studentId, assignmentId);
        return gradeFileRepository.delete(gradeId);
    }

    /**
     * updates a grade in the grade repository
     * @param studentId - the id of the student - String
     * @param assignmentId - the id of the assignment - int
     * @param value - the value of the grade
     * @param professor - the professor giving the grade
     * @return result of update operation - Grade
     */
    public Grade updateGrade(String studentId, int assignmentId, float value, String professor) {
        Grade grade = new Grade(studentId, assignmentId, value, professor);
        return gradeFileRepository.update(grade);
    }

    /**
     * finds a grade in the grade repository
     * @param studentId - id of the student
     * @param assignmentId - id of the assignment
     * @return grade - Grade
     */
    public Grade findGrade(String studentId, int assignmentId) {
        return gradeFileRepository.findOne(new Pair<>(studentId, assignmentId));
    }

    /**
     * returns all grades
     * @return grades - iterable of Grade
     */
    public Iterable<Grade> getAllGrades() {
        return gradeFileRepository.findAll();
    }

    public int getGradePenalty(int assignmentId) {
        int penalty = 0;
        Assignment assignment = assignmentService.findAssignment(assignmentId);
        if(assignment == null) {
            throw new IllegalArgumentException("The assignment does not exist.");
        }
        if(ApplicationContext.getYearStructure().getCurrentWeek() > assignment.getDeadlineWeek()) {
            penalty = ApplicationContext.getYearStructure().getCurrentWeek() - assignment.getDeadlineWeek();
        }
        return penalty;
    }

}
