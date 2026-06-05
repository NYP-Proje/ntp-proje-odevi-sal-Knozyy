package com.okul.service;

import com.okul.dao.UserDao;
import com.okul.model.Role;
import com.okul.model.User;
import com.okul.util.PasswordUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AuthService {

    private static final Logger log = LoggerFactory.getLogger(AuthService.class);
    private final UserDao userDao;

    public AuthService() {
        this(new UserDao());
    }

    public AuthService(UserDao userDao) {
        this.userDao = userDao;
    }

    public User login(String email, String password) {
        User user = userDao.findByEmail(email);
        if (user == null) {
            log.warn("Giris denemesi - kullanici yok: {}", email);
            return null;
        }
        if (PasswordUtil.check(password, user.getPasswordHash())) {
            log.info("Giris basarili: {}", email);
            return user;
        }
        log.warn("Giris denemesi - hatali parola: {}", email);
        return null;
    }

    public void ensureDefaultAdmin() {
        if (userDao.findByRole(Role.ADMIN).isEmpty()) {
            User admin = new User("Sistem", "Yonetici", "admin@okul.com",
                    PasswordUtil.hash("1234"), Role.ADMIN);
            userDao.save(admin);
            log.info("Varsayilan admin olusturuldu -> admin@okul.com / 1234");
        }
    }
}
