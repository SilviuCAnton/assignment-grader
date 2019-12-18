package com.silviucanton.services.service;

import com.silviucanton.domain.entities.Student;
import com.silviucanton.domain.validators.Validator;
import com.silviucanton.exceptions.ValidationException;
import com.silviucanton.repositories.CrudRepository;
import com.silviucanton.services.config.ApplicationContext;
import com.silviucanton.utils.observer.Observable;
import com.silviucanton.utils.observer.Observer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.PageRequest;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service for student operations
 */
@org.springframework.stereotype.Service
public class StudentService implements Service, Observable<StudentService> {
    private CrudRepository<Student, String> studentRepo;
    Validator<Student> validator;
    private List<Observer<StudentService>> observers = new ArrayList<>();

    @Autowired
    public StudentService(@Qualifier("studentDatabaseRepository") CrudRepository<Student, String> studentRepo, Validator<Student> validator) {
        this.studentRepo = studentRepo;
        this.validator = validator;
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
        validator.validate(student);
        studentRepo.save(student);
        Optional<Student> result = studentRepo.findById(student.getId());

        //Create json file with student id
        if (!result.isPresent()) {
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
        if (result.isPresent())
            return null;
        return student;
    }

    /**
     * updates a student in the repository
     *
     * @param student - the student to be updated - Student
     * @throws IllegalArgumentException if the student to be updated is null
     * @throws ValidationException      if the student is not valid
     */
    public Student updateStudent(Student student) throws IllegalArgumentException, ValidationException {
        validator.validate(student);
        Student st = studentRepo.save(student);
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
    public Optional<Student> findStudent(String id) throws IllegalArgumentException {
        return studentRepo.findById(id);
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

    public List<Student> findAllStudentsByPage(int pageIndex, int numberOfStudentPerPage) {
        List<Student> studentList = new ArrayList<>();
        studentRepo.findAll(PageRequest.of(pageIndex, numberOfStudentPerPage)).forEach(studentList::add);
        return studentList;
    }

    /**
     * deletes a student from the repository
     *
     * @param id - id of the student to be deleted - int
     * @throws IllegalArgumentException if the id is null
     */
    public void deleteStudent(String id) throws IllegalArgumentException {
        studentRepo.deleteById(id);
        notifyObservers();
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
