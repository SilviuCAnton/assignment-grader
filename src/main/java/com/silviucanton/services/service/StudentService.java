package com.silviucanton.services.service;

import com.silviucanton.domain.entities.Student;
import com.silviucanton.exceptions.ValidationException;
import com.silviucanton.repositories.CrudRepository;
import com.silviucanton.repositories.filePersistence.StudentFileRepository;
import com.silviucanton.services.config.ApplicationContext;
import com.silviucanton.utils.observer.Observable;
import com.silviucanton.utils.observer.Observer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service for student operations
 */
@Component
public class StudentService implements Service, Observable<StudentService> {
    private CrudRepository<String, Student> studentRepo;
    private List<Observer<StudentService>> observers = new ArrayList<>();

    @Autowired
    public StudentService(@Qualifier("studentDatabaseRepository") CrudRepository<String, Student> studentRepo) {
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
                if (!file.exists() && !file.createNewFile()) {
                    throw new IOException("File could not be created");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        notifyObservers();
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
        Student st = studentRepo.update(student);
        notifyObservers();
        return st;
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
     * @return student list - list of Student
     */
    public List<Student> findAllStudents() {
        List<Student> studentList = new ArrayList<>();
        studentRepo.findAll().forEach(studentList::add);
        return studentList;
    }

    /**
     * deletes a student from the repository
     *
     * @param id - id of the student to be deleted - int
     * @return the result of the deletion operation - Student
     * @throws IllegalArgumentException if the id is null
     */
    public Student deleteStudent(String id) throws IllegalArgumentException {
        Student student = studentRepo.delete(id);
        notifyObservers();
        return student;
    }

    /**
     * filters students by group
     *
     * @param group - the group to filter by - Int
     * @return result - Iterable of Student
     */
    public List<Student> filterStudentsByGroup(int group) {
        return findAllStudents().stream()
                .filter(x -> x.getGroup() == group)
                .collect(Collectors.toList());
    }


    @Override
    public void addObserver(Observer<StudentService> e) {
        observers.add(e);
    }

    @Override
    public void removeObserver(Observer<StudentService> e) {
        observers.remove(e);
    }

    @Override
    public void notifyObservers() {
        observers.forEach(x -> x.update(this));
    }
}
