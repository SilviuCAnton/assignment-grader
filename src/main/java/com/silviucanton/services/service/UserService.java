package com.silviucanton.services.service;

import com.silviucanton.domain.entities.Student;
import com.silviucanton.domain.entities.User;
import com.silviucanton.exceptions.ValidationException;
import com.silviucanton.repositories.CrudRepository;
import com.silviucanton.repositories.databasePersistence.UserDatabaseRepository;
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

@org.springframework.stereotype.Service
public class UserService implements Service, Observable<UserService> {
    private UserDatabaseRepository userRepo;
    private List<Observer<UserService>> observers = new ArrayList<>();

    @Autowired
    public UserService(UserDatabaseRepository userRepo) {
        this.userRepo = userRepo;
    }

    public Optional<User> findUserByUsername(String username) throws IllegalArgumentException {
        return userRepo.findByUsername(username);
    }

    @Override
    public void addObserver(Observer<UserService> e) {
        observers.add(e);
    }

    @Override
    public void removeObserver(Observer<UserService> e) {
        observers.remove(e);
    }

    @Override
    public void notifyObservers() {
        observers.forEach(x -> x.update(this));
    }

    public List<User> findAllUsersByPage(int pageIndex, int rowsPerPage) {
        List<User> userList = new ArrayList<>();
        userRepo.findAll(PageRequest.of(pageIndex, rowsPerPage)).forEach(userList::add);
        return userList;
    }

    public List<User> findAllUsers() {
        List<User> userList = new ArrayList<>();
        userRepo.findAll().forEach(userList::add);
        return userList;
    }

    public User saveUser(User user) throws IllegalArgumentException {
        userRepo.save(user);
        Optional<User> result = userRepo.findById(user.getId());

        notifyObservers();
        if (result.isPresent())
            return null;
        return user;
    }

    public User updateUser(User user) throws IllegalArgumentException {
        User usr = userRepo.save(user);
        notifyObservers();
        return usr;
    }

    public void deleteUser(int id) throws IllegalArgumentException {
        userRepo.deleteById(id);
        notifyObservers();
    }
}
