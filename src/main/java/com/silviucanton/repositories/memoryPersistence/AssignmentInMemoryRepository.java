package com.silviucanton.repositories.memoryPersistence;

import com.silviucanton.domain.entities.Assignment;
import com.silviucanton.domain.validators.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class AssignmentInMemoryRepository extends InMemoryRepository<Assignment, Integer> {

    @Autowired
    public AssignmentInMemoryRepository(Validator<Assignment> validator) {
        super(validator);
    }
}
