package repositories.filePersistence;

import domain.entities.Assignment;
import domain.entities.Grade;
import domain.entities.Student;
import domain.validators.Validator;
import repositories.CrudRepository;
import repositories.GradeRepository;
import utils.Constants;
import utils.Pair;

import java.time.LocalDate;

/**
 * Repository for grades - file data persistence
 */
public class GradeFileRepository extends AbstractFileRepository<Pair<String, Integer>, Grade> implements GradeRepository {
    private CrudRepository<String, Student> studentRepo;
    private CrudRepository<Integer, Assignment> assignmentRepo;

    public GradeFileRepository(Validator<Grade> validator, String fileName, CrudRepository<String, Student> studentRepo, CrudRepository<Integer, Assignment> assignmentRepo) {
        super(validator, fileName, false);
        this.studentRepo = studentRepo;
        this.assignmentRepo = assignmentRepo;
        super.loadDataFromFile();
    }

    /**
     * parses a grade from a file string
     *
     * @param lineToParse - the grade to be parsed - String
     * @return grade - Grade
     */
    @Override
    Grade parseEntity(String lineToParse) {
        String[] args = lineToParse.split("/");
        Student student = studentRepo.findOne(args[0]);
        Assignment assignment = assignmentRepo.findOne(Integer.parseInt(args[1]));
        Grade grade = new Grade(student, assignment, Float.parseFloat(args[3]), args[4]);
        grade.setDate(LocalDate.parse(args[2], Constants.DATE_TIME_FORMATTER));
        return grade;
    }

    /**
     * returns the student repository
     *
     * @return studentRepo - Student Crud Repository
     */
    @Override
    public CrudRepository<String, Student> getStudentRepo() {
        return studentRepo;
    }

    /**
     * sets the student repository
     *
     * @param studentRepo - Student Crud Repository
     */
    public void setStudentRepo(CrudRepository<String, Student> studentRepo) {
        this.studentRepo = studentRepo;
    }

    /**
     * returns the assignment repository
     *
     * @return assignmentRepo - Assignment Crud Repository
     */
    @Override
    public CrudRepository<Integer, Assignment> getAssignmentRepo() {
        return assignmentRepo;
    }

    /**
     * sets the assignment repository
     *
     * @param assignmentRepo - Assignment Crud Repository
     */
    public void setAssignmentRepo(CrudRepository<Integer, Assignment> assignmentRepo) {
        this.assignmentRepo = assignmentRepo;
    }
}
