package com.okul.controller;

import com.okul.model.Announcement;
import com.okul.model.Assignment;
import com.okul.model.Course;
import com.okul.model.Enrollment;
import com.okul.model.Grade;
import com.okul.model.Submission;
import com.okul.model.User;
import com.okul.service.AnnouncementService;
import com.okul.service.AssignmentService;
import com.okul.service.CourseService;
import com.okul.service.EnrollmentService;
import com.okul.service.GradeService;
import com.okul.service.UserService;
import com.okul.util.ErrorHandler;
import com.okul.util.FileManager;
import com.okul.util.SceneManager;
import com.okul.util.Session;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.util.ArrayList;
import java.util.List;

public class TeacherController {

    @FXML private Label welcomeLabel;
    @FXML private ComboBox<Course> myCoursesCombo;

    @FXML private TextField annTitleField;
    @FXML private TextArea annContentArea;
    @FXML private ListView<String> announcementsList;

    @FXML private TextField asgTitleField;
    @FXML private TextArea asgDescArea;
    @FXML private TextField asgDueField;
    @FXML private ComboBox<Assignment> assignmentsCombo;
    @FXML private ListView<String> submissionsList;

    @FXML private ComboBox<User> gradeStudentCombo;
    @FXML private TextField scoreField;
    @FXML private ListView<String> rankingList;

    private final CourseService courseService = new CourseService();
    private final AnnouncementService announcementService = new AnnouncementService();
    private final AssignmentService assignmentService = new AssignmentService();
    private final GradeService gradeService = new GradeService();
    private final EnrollmentService enrollmentService = new EnrollmentService();
    private final UserService userService = new UserService();

    private Course selectedCourse;
    private List<Submission> submissionsData = new ArrayList<>();

    @FXML
    private void initialize() {
        User current = Session.getCurrentUser();
        if (current != null) {
            welcomeLabel.setText("Hos geldiniz, " + current.fullName() + " (Ogretmen)");
            myCoursesCombo.getItems().setAll(courseService.getCoursesByTeacher(current.getId()));
        }
    }

    @FXML
    private void handleLoadCourse() {
        selectedCourse = myCoursesCombo.getValue();
        if (selectedCourse == null) {
            ErrorHandler.showError("Lutfen bir ders seciniz.");
            return;
        }
        refreshAnnouncements();
        assignmentsCombo.getItems().setAll(assignmentService.getAssignmentsByCourse(selectedCourse.getId()));
        refreshGradeStudents();
        submissionsList.getItems().clear();
        rankingList.getItems().clear();
    }

    private boolean noCourseSelected() {
        if (selectedCourse == null) {
            ErrorHandler.showError("Once ust kisimdan bir ders secip 'Dersi Yukle' butonuna basin.");
            return true;
        }
        return false;
    }

    @FXML
    private void handlePostAnnouncement() {
        if (noCourseSelected()) {
            return;
        }
        try {
            announcementService.postAnnouncement(
                    selectedCourse.getId(),
                    Session.getCurrentUser().getId(),
                    annTitleField.getText().trim(),
                    annContentArea.getText().trim());
            ErrorHandler.showInfo("Duyuru yayinlandi.");
            annTitleField.clear();
            annContentArea.clear();
            refreshAnnouncements();
        } catch (Exception e) {
            ErrorHandler.handle("Duyuru yayinlanamadi.", e);
        }
    }

    private void refreshAnnouncements() {
        announcementsList.getItems().clear();
        for (Announcement a : announcementService.getByCourse(selectedCourse.getId())) {
            announcementsList.getItems().add(a.getTitle() + "  (" + a.getDate() + "): " + a.getContent());
        }
    }

    @FXML
    private void handleCreateAssignment() {
        if (noCourseSelected()) {
            return;
        }
        try {
            assignmentService.createAssignment(
                    selectedCourse.getId(),
                    asgTitleField.getText().trim(),
                    asgDescArea.getText().trim(),
                    asgDueField.getText().trim());
            ErrorHandler.showInfo("Odev olusturuldu.");
            asgTitleField.clear();
            asgDescArea.clear();
            asgDueField.clear();
            assignmentsCombo.getItems().setAll(assignmentService.getAssignmentsByCourse(selectedCourse.getId()));
        } catch (Exception e) {
            ErrorHandler.handle("Odev olusturulamadi.", e);
        }
    }

    @FXML
    private void handleShowSubmissions() {
        Assignment assignment = assignmentsCombo.getValue();
        if (assignment == null) {
            ErrorHandler.showError("Lutfen bir odev seciniz.");
            return;
        }
        try {
            submissionsData = assignmentService.getSubmissions(assignment.getId());
            submissionsList.getItems().clear();
            for (Submission s : submissionsData) {
                User st = userService.getById(s.getStudentId());
                String name = (st != null) ? st.fullName() : s.getStudentId();
                submissionsList.getItems().add(
                        name + "  | teslim: " + s.getSubmitDate() + "  | dosya: " + s.getFilePath());
            }
            if (submissionsData.isEmpty()) {
                submissionsList.getItems().add("Bu odeve henuz teslim yapilmamis.");
            }
        } catch (Exception e) {
            ErrorHandler.handle("Teslimler getirilemedi.", e);
        }
    }

    @FXML
    private void handleOpenFile() {
        int index = submissionsList.getSelectionModel().getSelectedIndex();
        if (index < 0 || index >= submissionsData.size()) {
            ErrorHandler.showError("Acmak icin listeden bir teslim seciniz.");
            return;
        }
        try {
            FileManager.open(submissionsData.get(index).getFilePath());
        } catch (Exception e) {
            ErrorHandler.handle("Dosya acilamadi.", e);
        }
    }

    private void refreshGradeStudents() {
        List<User> students = new ArrayList<>();
        for (Enrollment en : enrollmentService.getCourseEnrollments(selectedCourse.getId())) {
            User st = userService.getById(en.getStudentId());
            if (st != null) {
                students.add(st);
            }
        }
        gradeStudentCombo.getItems().setAll(students);
    }

    @FXML
    private void handleAssignGrade() {
        if (noCourseSelected()) {
            return;
        }
        User student = gradeStudentCombo.getValue();
        if (student == null) {
            ErrorHandler.showError("Lutfen bir ogrenci seciniz.");
            return;
        }
        try {
            double score = Double.parseDouble(scoreField.getText().trim());
            gradeService.assignGrade(selectedCourse.getId(), student.getId(), score);
            ErrorHandler.showInfo("Not kaydedildi.");
            scoreField.clear();
        } catch (NumberFormatException e) {
            ErrorHandler.showError("Not sayisal olmali (orn 85).");
        } catch (Exception e) {
            ErrorHandler.handle("Not kaydedilemedi.", e);
        }
    }

    @FXML
    private void handleShowRanking() {
        if (noCourseSelected()) {
            return;
        }
        try {
            rankingList.getItems().clear();
            int rank = 1;
            for (Grade g : gradeService.getCourseRanking(selectedCourse.getId())) {
                User st = userService.getById(g.getStudentId());
                String name = (st != null) ? st.fullName() : g.getStudentId();
                rankingList.getItems().add(rank + ". " + name + "  -  " + g.getScore()
                        + " (" + g.getLetterGrade() + ")");
                rank++;
            }
            if (rankingList.getItems().isEmpty()) {
                rankingList.getItems().add("Bu derste henuz not girilmemis.");
            }
        } catch (Exception e) {
            ErrorHandler.handle("Siralama getirilemedi.", e);
        }
    }

    @FXML
    private void handleLogout() {
        Session.logout();
        SceneManager.switchTo(welcomeLabel, "/fxml/login.fxml", "Okul Yonetim Sistemi - Giris");
    }
}
