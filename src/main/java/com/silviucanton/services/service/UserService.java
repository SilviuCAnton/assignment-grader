package com.silviucanton.services.service;

import com.silviucanton.domain.entities.Student;
import com.silviucanton.domain.entities.User;
import com.silviucanton.repositories.CrudRepository;
import com.silviucanton.repositories.databasePersistence.UserDatabaseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.util.Optional;

@org.springframework.stereotype.Service
public class UserService implements Service {
    private UserDatabaseRepository userRepo;

    @Autowired
    public UserService(UserDatabaseRepository userRepo) {
        this.userRepo = userRepo;
    }

    public Optional<User> findUserByUsername(String username) throws IllegalArgumentException {
        return userRepo.findByUsername(username);
    }
}
