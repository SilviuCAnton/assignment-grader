package services.service;

import domain.Assignment;
import domain.FeedbackDTO;
import domain.Grade;
import domain.Student;
import domain.validators.GradeValidator;
import repositories.GradeFileRepository;
import services.config.ApplicationContext;
import utils.Pair;

import java.time.LocalDate;

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
     * @return the result of the storing operation - Grade
     */
    public Grade addGrade(String studentId, int assignmentId, float value, String professor, int numberWeeksLate) {
        int penalty = 0;
        Assignment assignment = assignmentService.findAssignment(assignmentId);
        Student student = studentService.findStudent(studentId);

        if(ApplicationContext.getYearStructure().getCurrentWeek() > assignment.getDeadlineWeek()) {
            penalty = ApplicationContext.getYearStructure().getCurrentWeek() - assignment.getDeadlineWeek();
            penalty -= numberWeeksLate;
        }

        if(penalty < 0){
            penalty = 0;
        }

        Grade grade = new Grade(studentId, assignmentId, value - penalty, professor);

        Grade result = gradeFileRepository.save(grade);

        //Adding feedback in json file
        if(result == null){
            FeedbackDTO feedback = new FeedbackDTO(assignment.getDescription(), grade.getValue(), ApplicationContext.getYearStructure().getCurrentWeek(), assignment.getDeadlineWeek());
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

}
