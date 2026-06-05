package com.okul.service;

import com.okul.dao.UserDao;
import com.okul.model.Role;
import com.okul.model.User;
import com.okul.util.PasswordUtil;

import java.util.List;

public class UserService {

    private final UserDao userDao;

    public UserService() {
        this(new UserDao());
    }

    public UserService(UserDao userDao) {
        this.userDao = userDao;
    }

    public void createAccount(String firstName, String lastName, String email,
                              String password, Role role) {
        if (email == null || email.isBlank()) {
            throw new RuntimeException("E-posta bos olamaz.");
        }
        if (password == null || password.isBlank()) {
            throw new RuntimeException("Parola bos olamaz.");
        }
        if (userDao.findByEmail(email) != null) {
            throw new RuntimeException("Bu e-posta zaten kayitli.");
        }
        User user = new User(firstName, lastName, email, PasswordUtil.hash(password), role);
        userDao.save(user);
    }

    public List<User> getByRole(Role role) {
        return userDao.findByRole(role);
    }

    public User getById(String id) {
        return userDao.findById(id);
    }

    public void deleteAccount(String id) {
        userDao.deleteById(id);
    }
}
