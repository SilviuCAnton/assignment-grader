package com.silviucanton.repositories.databasePersistence;

import com.silviucanton.domain.entities.User;
import com.silviucanton.repositories.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserDatabaseRepository extends CrudRepository<User, Integer> {
    public Optional<User> findByUsername(String username);
}
