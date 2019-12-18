package com.silviucanton.repositories.memoryPersistence;

import com.silviucanton.domain.entities.Student;
import com.silviucanton.domain.validators.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class StudentInMemoryRepository extends InMemoryRepository<Student, String> {
    @Autowired
    public StudentInMemoryRepository(Validator<Student> validator) {
        super(validator);
    }
}
