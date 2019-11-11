package repositories.memoryPersistence;

import domain.entities.Assignment;
import domain.entities.Grade;
import domain.entities.Student;
import domain.validators.Validator;
import repositories.CrudRepository;
import repositories.GradeRepository;
import utils.Pair;

/**
 * In memory repository for grades
 */
public class GradeInMemoryRepository extends InMemoryRepository<Pair<String, Integer>, Grade> implements GradeRepository {
    private CrudRepository<String, Student> studentRepo;
    private CrudRepository<Integer, Assignment> assignmentRepo;
    public GradeInMemoryRepository(Validator<Grade> validator, CrudRepository<String, Student> studentRepo, CrudRepository<Integer, Assignment> assignmentRepo) {
        super(validator);
        this.studentRepo = studentRepo;
        this.assignmentRepo = assignmentRepo;
    }

    /**
     * returns the student repository
     * @return studentRepo - Student Crud Repository
     */
    @Override
    public CrudRepository<String, Student> getStudentRepo() {
        return studentRepo;
    }

    /**
     * sets the student repository
     * @param studentRepo - Student Crud Repository
     */
    public void setStudentRepo(CrudRepository<String, Student> studentRepo) {
        this.studentRepo = studentRepo;
    }

    /**
     * returns the assignment repository
     * @return assignmentRepo - Assignment Crud Repository
     */
    @Override
    public CrudRepository<Integer, Assignment> getAssignmentRepo() {
        return assignmentRepo;
    }

    /**
     * sets the assignment repository
     * @param assignmentRepo - Assignment Crud Repository
     */
    public void setAssignmentRepo(CrudRepository<Integer, Assignment> assignmentRepo) {
        this.assignmentRepo = assignmentRepo;
    }
}
