package ru.kata.spring.boot_security.demo.service;

import ru.kata.spring.boot_security.demo.models.Role;
import ru.kata.spring.boot_security.demo.models.User;

import java.util.Set;

public interface UserService {
    Iterable<User> findAll();
    User findOne(Long id);

    void saveUser(User user);

    void updateUser(User updatedUser);

    void delete(Long id);

    Set<Role> getRole();

    User getAuthUser();
}
