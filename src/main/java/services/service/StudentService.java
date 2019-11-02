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
     * @return the result of the storing operation - String
     * @throws IllegalArgumentException if the student to be stored is null
     * @throws ValidationException - if the student is not valid
     */
    public String saveStudent(String id, String firstName, String lastName, int group, String email, String coordinator) throws IllegalArgumentException, ValidationException {
        String result;
        Student st = studentRepo.save(new Student(id, firstName, lastName, group, email, coordinator));
        if (st != null) {
            result = "Student already exists: " + st.toString();
        } else {
            result = "The student has been stored.";
        }
        return result;
    }

    /**
     * updates a student in the repository
     * @param id - id of the student - String
     * @param firstName - first name of the student - String
     * @param lastName - last name of the student - String
     * @param email - email of the student - String
     * @param coordinator - coordinator of the student - String
     * @return the result of the storing operation - String
     * @throws IllegalArgumentException if the student to be updated is null
     * @throws ValidationException if the student is not valid
     */
    public String updateStudent(String id, String firstName, String lastName, int group, String email, String coordinator) throws IllegalArgumentException, ValidationException {
        String result;
        Student st = studentRepo.update(new Student(id, firstName, lastName, group, email, coordinator));
        if (st != null) {
            result = "The student " + st.getFirstName() + ' ' + st.getLastName() + " has been updated.";
        } else {
            result = "The student with the given id does not exist.";
        }
        return result;
    }

    /**
     * finds a student in the repository
     * @param id - id of the student to be found - int
     * @return the result of searchind the student - String
     * @throws IllegalArgumentException if the id is null
     */
    public String findStudent(String id) throws IllegalArgumentException{
        String res;
        Student st = studentRepo.findOne(id);
        if (st != null) {
            res = st.toString();
        } else {
            res = "The student with the given id does not exist.";
        }
        return res;
    }

    /**
     * returns all students
     * @return student list - String
     */
    public String findAllStudents() {
        StringBuilder sb = new StringBuilder();
        studentRepo.findAll().forEach(x-> sb.append(x.toString()).append('\n'));
        return sb.toString();
    }

    /**
     * deletes a student from the repository
     * @param id - id of the student to be deleted - int
     * @return the result of the deletion operation - String
     * @throws IllegalArgumentException if the id is null
     */
    public String deleteStudent(String id) throws IllegalArgumentException {
        String res;
        Student st = studentRepo.delete(id);
        if (st != null) {
            res = st.getFirstName() + " " + st.getLastName() + " has been removed.";
        } else {
            res ="The student with the given id does not exist.";
        }
        return res;
    }

}
