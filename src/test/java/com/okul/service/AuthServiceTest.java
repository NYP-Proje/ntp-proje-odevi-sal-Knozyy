package com.okul.service;

import com.okul.dao.UserDao;
import com.okul.model.Role;
import com.okul.model.User;
import com.okul.util.PasswordUtil;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class AuthServiceTest {

    @Test
    void login_dogru_parolayla_kullanici_dondurur() {
        UserDao userDao = mock(UserDao.class);
        User user = new User("Ali", "Veli", "ali@okul.com", PasswordUtil.hash("1234"), Role.STUDENT);
        when(userDao.findByEmail("ali@okul.com")).thenReturn(user);

        AuthService service = new AuthService(userDao);
        User result = service.login("ali@okul.com", "1234");

        assertNotNull(result);
        assertEquals("ali@okul.com", result.getEmail());
    }

    @Test
    void login_yanlis_parolada_null_dondurur() {
        UserDao userDao = mock(UserDao.class);
        User user = new User("Ali", "Veli", "ali@okul.com", PasswordUtil.hash("1234"), Role.STUDENT);
        when(userDao.findByEmail("ali@okul.com")).thenReturn(user);

        AuthService service = new AuthService(userDao);
        assertNull(service.login("ali@okul.com", "yanlisparola"));
    }

    @Test
    void ensureDefaultAdmin_hic_admin_yoksa_olusturur() {
        UserDao userDao = mock(UserDao.class);
        when(userDao.findByRole(Role.ADMIN)).thenReturn(List.of());

        AuthService service = new AuthService(userDao);
        service.ensureDefaultAdmin();

        verify(userDao, times(1)).save(any(User.class));
    }
}
