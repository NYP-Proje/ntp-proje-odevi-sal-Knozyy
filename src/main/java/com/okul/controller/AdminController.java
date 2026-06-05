package com.okul.controller;

import com.okul.model.Course;
import com.okul.model.Enrollment;
import com.okul.model.Role;
import com.okul.model.User;
import com.okul.service.CourseService;
import com.okul.service.EnrollmentService;
import com.okul.service.UserService;
import com.okul.util.ErrorHandler;
import com.okul.util.SceneManager;
import com.okul.util.Session;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.util.ArrayList;
import java.util.List;

public class AdminController {

    @FXML private Label welcomeLabel;

    @FXML private TextField firstNameField;
    @FXML private TextField lastNameField;
    @FXML private TextField accEmailField;
    @FXML private PasswordField accPasswordField;
    @FXML private ComboBox<Role> roleCombo;
    @FXML private ListView<String> usersList;

    @FXML private TextField courseCodeField;
    @FXML private TextField courseNameField;
    @FXML private ComboBox<User> teacherCombo;
    @FXML private TextField quotaField;
    @FXML private TextField creditField;
    @FXML private TextField termField;
    @FXML private ListView<String> coursesList;

    @FXML private ComboBox<Course> assignCourseCombo;
    @FXML private ComboBox<User> assignStudentCombo;
    @FXML private ListView<String> enrolledList;

    private final UserService userService = new UserService();
    private final CourseService courseService = new CourseService();
    private final EnrollmentService enrollmentService = new EnrollmentService();

    private List<User> usersData = new ArrayList<>();
    private List<Course> coursesData = new ArrayList<>();

    @FXML
    private void initialize() {
        User current = Session.getCurrentUser();
        if (current != null) {
            welcomeLabel.setText("Hos geldiniz, " + current.fullName() + " (Yonetici)");
        }
        roleCombo.getItems().addAll(Role.TEACHER, Role.STUDENT);
        refreshAll();
    }

    private void refreshAll() {
        refreshUsers();
        refreshCourses();
        refreshCombos();
    }

    @FXML
    private void handleCreateAccount() {
        Role role = roleCombo.getValue();
        if (role == null) {
            ErrorHandler.showError("Lutfen rol seciniz.");
            return;
        }
        try {
            userService.createAccount(
                    firstNameField.getText().trim(),
                    lastNameField.getText().trim(),
                    accEmailField.getText().trim(),
                    accPasswordField.getText(),
                    role);
            ErrorHandler.showInfo("Hesap olusturuldu.");
            clearAccountForm();
            refreshUsers();
            refreshCombos();
        } catch (Exception e) {
            ErrorHandler.handle("Hesap olusturulamadi.", e);
        }
    }

    @FXML
    private void handleDeleteAccount() {
        int index = usersList.getSelectionModel().getSelectedIndex();
        if (index < 0) {
            ErrorHandler.showError("Silmek icin listeden bir hesap seciniz.");
            return;
        }
        try {
            userService.deleteAccount(usersData.get(index).getId());
            ErrorHandler.showInfo("Hesap silindi.");
            refreshUsers();
            refreshCombos();
        } catch (Exception e) {
            ErrorHandler.handle("Hesap silinemedi.", e);
        }
    }

    private void refreshUsers() {
        usersData = new ArrayList<>();
        usersData.addAll(userService.getByRole(Role.TEACHER));
        usersData.addAll(userService.getByRole(Role.STUDENT));
        usersList.getItems().clear();
        for (User u : usersData) {
            usersList.getItems().add("[" + u.getRole() + "] " + u.fullName() + " - " + u.getEmail());
        }
    }

    private void clearAccountForm() {
        firstNameField.clear();
        lastNameField.clear();
        accEmailField.clear();
        accPasswordField.clear();
        roleCombo.setValue(null);
    }

    @FXML
    private void handleCreateCourse() {
        User teacher = teacherCombo.getValue();
        if (teacher == null) {
            ErrorHandler.showError("Lutfen dersin ogretmenini seciniz.");
            return;
        }
        try {
            int quota = Integer.parseInt(quotaField.getText().trim());
            int credit = Integer.parseInt(creditField.getText().trim());
            courseService.createCourse(
                    courseCodeField.getText().trim(),
                    courseNameField.getText().trim(),
                    teacher.getId(),
                    quota,
                    credit,
                    termField.getText().trim());
            ErrorHandler.showInfo("Ders olusturuldu.");
            clearCourseForm();
            refreshCourses();
        } catch (NumberFormatException e) {
            ErrorHandler.showError("Kontenjan ve kredi alanlari sayi olmali.");
        } catch (Exception e) {
            ErrorHandler.handle("Ders olusturulamadi.", e);
        }
    }

    @FXML
    private void handleDeleteCourse() {
        int index = coursesList.getSelectionModel().getSelectedIndex();
        if (index < 0) {
            ErrorHandler.showError("Silmek icin listeden bir ders seciniz.");
            return;
        }
        try {
            courseService.deleteCourse(coursesData.get(index).getId());
            ErrorHandler.showInfo("Ders silindi.");
            refreshCourses();
        } catch (Exception e) {
            ErrorHandler.handle("Ders silinemedi.", e);
        }
    }

    private void refreshCourses() {
        coursesData = courseService.getAllCourses();
        coursesList.getItems().clear();
        for (Course c : coursesData) {
            User t = userService.getById(c.getTeacherId());
            String teacherName = (t != null) ? t.fullName() : "-";
            coursesList.getItems().add(
                    c.getCode() + " - " + c.getName()
                            + "  | Ogretmen: " + teacherName
                            + "  | Kontenjan: " + c.getQuota()
                            + "  | Kredi: " + c.getCredit()
                            + "  | Donem: " + c.getTerm());
        }

        assignCourseCombo.getItems().setAll(coursesData);
    }

    private void clearCourseForm() {
        courseCodeField.clear();
        courseNameField.clear();
        teacherCombo.setValue(null);
        quotaField.clear();
        creditField.clear();
        termField.clear();
    }

    @FXML
    private void handleAssign() {
        Course course = assignCourseCombo.getValue();
        User student = assignStudentCombo.getValue();
        if (course == null || student == null) {
            ErrorHandler.showError("Lutfen ders ve ogrenci seciniz.");
            return;
        }
        try {
            enrollmentService.enroll(course.getId(), student.getId());
            ErrorHandler.showInfo("Ogrenci derse atandi.");
            handleShowEnrollments();
        } catch (Exception e) {
            ErrorHandler.handle("Atama yapilamadi.", e);
        }
    }

    @FXML
    private void handleShowEnrollments() {
        Course course = assignCourseCombo.getValue();
        if (course == null) {
            ErrorHandler.showError("Once bir ders seciniz.");
            return;
        }
        try {
            enrolledList.getItems().clear();
            for (Enrollment en : enrollmentService.getCourseEnrollments(course.getId())) {
                User st = userService.getById(en.getStudentId());
                String name = (st != null) ? st.fullName() : en.getStudentId();
                enrolledList.getItems().add(name + "   (kayit tarihi: " + en.getEnrollDate() + ")");
            }
            if (enrolledList.getItems().isEmpty()) {
                enrolledList.getItems().add("Bu derse henuz kayitli ogrenci yok.");
            }
        } catch (Exception e) {
            ErrorHandler.handle("Kayitlar getirilemedi.", e);
        }
    }

    private void refreshCombos() {
        teacherCombo.getItems().setAll(userService.getByRole(Role.TEACHER));
        assignStudentCombo.getItems().setAll(userService.getByRole(Role.STUDENT));
    }

    @FXML
    private void handleLogout() {
        Session.logout();
        SceneManager.switchTo(welcomeLabel, "/fxml/login.fxml", "Okul Yonetim Sistemi - Giris");
    }
}
