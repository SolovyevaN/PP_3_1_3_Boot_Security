package ru.kata.spring.boot_security.demo.service;

import ru.kata.spring.boot_security.demo.model.User;

import java.util.List;

public interface UserService {
    void addUser(String name, String surname, int age, String password, String email, String role);
    void updateUser(User user);
    User findById(Long id);
    void deleteUser(Long id);
    List<User> getAllUsers();
}
