package services.service;

import domain.Student;
import domain.validators.StudentValidator;
import exceptions.ValidationException;
import repositories.StudentFileRepository;

/**
 * Service for student operations
 */
public class StudentService {
    private StudentFileRepository studentRepo;

    public StudentService(String fileName) {
        this.studentRepo = new StudentFileRepository(new StudentValidator(), fileName);
    }

    /**
     * returns the student repository
     * @return studentRepo - StudentFileRepository
     */
    public StudentFileRepository getStudentRepo() {
        return studentRepo;
    }

    /**
     * sets the student repository
     * @param studentRepo - StudentFileRepository
     */
    public void setStudentRepo(StudentFileRepository studentRepo) {
        this.studentRepo = studentRepo;
    }

    /**
     * saves a student in the repository
     * @param id - id of the student - String
     * @param firstName - first name of the student - String
     * @param lastName - last name of the student - String
     * @param email - email of the student - String
     * @param coordinator - coordinator of the student - String
     * @return the result of the storing operation - Student
     * @throws IllegalArgumentException if the student to be stored is null
     * @throws ValidationException - if the student is not valid
     */
    public Student saveStudent(String id, String firstName, String lastName, int group, String email, String coordinator) throws IllegalArgumentException, ValidationException {
        return studentRepo.save(new Student(id, firstName, lastName, group, email, coordinator));
    }

    /**
     * updates a student in the repository
     * @param id - id of the student - String
     * @param firstName - first name of the student - String
     * @param lastName - last name of the student - String
     * @param email - email of the student - String
     * @param coordinator - coordinator of the student - String
     * @return the result of the storing operation - Student
     * @throws IllegalArgumentException if the student to be updated is null
     * @throws ValidationException if the student is not valid
     */
    public Student updateStudent(String id, String firstName, String lastName, int group, String email, String coordinator) throws IllegalArgumentException, ValidationException {
        return studentRepo.update(new Student(id, firstName, lastName, group, email, coordinator));
    }

    /**
     * finds a student in the repository
     * @param id - id of the student to be found - int
     * @return the result of searchind the student - Student
     * @throws IllegalArgumentException if the id is null
     */
    public Student findStudent(String id) throws IllegalArgumentException{
        return studentRepo.findOne(id);
    }

    /**
     * returns all students
     * @return student list - iterable of Student
     */
    public Iterable<Student> findAllStudents() {
        return studentRepo.findAll();
    }

    /**
     * deletes a student from the repository
     * @param id - id of the student to be deleted - int
     * @return the result of the deletion operation - Student
     * @throws IllegalArgumentException if the id is null
     */
    public Student deleteStudent(String id) throws IllegalArgumentException {
        return studentRepo.delete(id);
    }

}
