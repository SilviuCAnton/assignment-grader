package services.service;

import domain.entities.Student;
import exceptions.ValidationException;
import repositories.CrudRepository;
import repositories.filePersistence.StudentFileRepository;
import services.config.ApplicationContext;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service for student operations
 */
public class StudentService implements Service {
    private CrudRepository<String, Student> studentRepo;

    public StudentService(CrudRepository<String, Student> studentRepo) {
        this.studentRepo = studentRepo;
    }

    /**
     * returns the student repository
     *
     * @return studentRepo - StudentFileRepository
     */
    public CrudRepository<String, Student> getStudentRepo() {
        return studentRepo;
    }

    /**
     * sets the student repository
     *
     * @param studentRepo - StudentFileRepository
     */
    public void setStudentRepo(StudentFileRepository studentRepo) {
        this.studentRepo = studentRepo;
    }

    /**
     * saves a student in the repository
     *
     * @param student - the student to be saved - Student
     * @return the result of the storing operation - Student
     * @throws IllegalArgumentException if the student to be stored is null
     * @throws ValidationException      - if the student is not valid
     */
    public Student saveStudent(Student student) throws IllegalArgumentException, ValidationException {
        Student st = studentRepo.save(student);

        //Create json file with student id
        if (st == null) {
            File file = new File(ApplicationContext.getProperties().getProperty("data.catalog.feedbackPath") + student.getId() + ".json");
            try {
                if (!file.createNewFile()) {
                    throw new IOException("File could not be created.");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return st;
    }

    /**
     * updates a student in the repository
     *
     * @param student - the student to be updated - Student
     * @throws IllegalArgumentException if the student to be updated is null
     * @throws ValidationException      if the student is not valid
     */
    public Student updateStudent(Student student) throws IllegalArgumentException, ValidationException {
        return studentRepo.update(student);
    }

    /**
     * finds a student in the repository
     *
     * @param id - id of the student to be found - int
     * @return the result of searchind the student - Student
     * @throws IllegalArgumentException if the id is null
     */
    public Student findStudent(String id) throws IllegalArgumentException {
        return studentRepo.findOne(id);
    }

    /**
     * returns all students
     *
     * @return student list - iterable of Student
     */
    public Iterable<Student> findAllStudents() {
        return studentRepo.findAll();
    }

    /**
     * deletes a student from the repository
     *
     * @param id - id of the student to be deleted - int
     * @return the result of the deletion operation - Student
     * @throws IllegalArgumentException if the id is null
     */
    public Student deleteStudent(String id) throws IllegalArgumentException {
        return studentRepo.delete(id);
    }

    /**
     * filters students by group
     *
     * @param group - the group to filter by - Int
     * @return result - Iterable of Student
     */
    public Iterable<Student> filterStudentsByGroup(int group) {
        List<Student> students = new ArrayList<>();
        findAllStudents().forEach(students::add);
        return students.stream()
                .filter(x -> x.getGroup() == group)
                .collect(Collectors.toList());
    }
}
