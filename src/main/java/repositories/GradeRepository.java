package repositories;

import domain.entities.Assignment;
import domain.entities.Grade;
import domain.entities.Student;
import utils.Pair;

/**
 * Grade repo interface for allowing generic getter and setter for the student and assignment repos
 */
public interface GradeRepository extends CrudRepository<Pair<String, Integer>, Grade> {
    CrudRepository<String, Student> getStudentRepo();

    CrudRepository<Integer, Assignment> getAssignmentRepo();
}
