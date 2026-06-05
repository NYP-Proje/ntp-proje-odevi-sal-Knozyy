package com.okul.controller;

import com.okul.model.User;
import com.okul.service.AuthService;
import com.okul.util.ErrorHandler;
import com.okul.util.SceneManager;
import com.okul.util.Session;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class LoginController {

    @FXML private TextField emailField;
    @FXML private PasswordField passwordField;

    private final AuthService authService = new AuthService();

    @FXML
    private void handleLogin() {
        String email = emailField.getText().trim();
        String password = passwordField.getText();

        if (email.isEmpty() || password.isEmpty()) {
            ErrorHandler.showError("Lutfen e-posta ve parola giriniz.");
            return;
        }

        try {
            User user = authService.login(email, password);
            if (user == null) {
                ErrorHandler.showError("E-posta veya parola hatali.");
                return;
            }
            Session.setCurrentUser(user);
            openDashboard(user);
        } catch (Exception e) {
            ErrorHandler.handle("Giris sirasinda bir hata olustu.", e);
        }
    }

    private void openDashboard(User user) {
        String fxml;
        String title;
        switch (user.getRole()) {
            case ADMIN:
                fxml = "/fxml/admin.fxml";
                title = "Yonetici Paneli";
                break;
            case TEACHER:
                fxml = "/fxml/teacher.fxml";
                title = "Ogretmen Paneli";
                break;
            default:
                fxml = "/fxml/student.fxml";
                title = "Ogrenci Paneli";
                break;
        }
        SceneManager.switchTo(emailField, fxml, title);
    }
}
