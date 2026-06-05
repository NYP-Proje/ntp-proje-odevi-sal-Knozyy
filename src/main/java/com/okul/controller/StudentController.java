package com.okul.controller;

import com.okul.model.Announcement;
import com.okul.model.Assignment;
import com.okul.model.Course;
import com.okul.model.Enrollment;
import com.okul.model.TranscriptRow;
import com.okul.model.User;
import com.okul.service.AnnouncementService;
import com.okul.service.AssignmentService;
import com.okul.service.CourseService;
import com.okul.service.EnrollmentService;
import com.okul.service.GradeService;
import com.okul.util.ErrorHandler;
import com.okul.util.SceneManager;
import com.okul.util.Session;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.stage.FileChooser;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class StudentController {

    @FXML private Label welcomeLabel;

    @FXML private ListView<String> availableCoursesList;
    @FXML private ListView<String> myCoursesList;

    @FXML private ComboBox<Course> courseCombo;
    @FXML private ComboBox<Assignment> assignmentsCombo;
    @FXML private ListView<String> announcementsList;

    @FXML private ListView<String> transcriptList;
    @FXML private Label gpaLabel;

    private final CourseService courseService = new CourseService();
    private final EnrollmentService enrollmentService = new EnrollmentService();
    private final AssignmentService assignmentService = new AssignmentService();
    private final AnnouncementService announcementService = new AnnouncementService();
    private final GradeService gradeService = new GradeService();

    private List<Course> availableData = new ArrayList<>();

    @FXML
    private void initialize() {
        User current = Session.getCurrentUser();
        if (current != null) {
            welcomeLabel.setText("Hos geldiniz, " + current.fullName() + " (Ogrenci)");
        }
        refreshCourses();
    }

    private String studentId() {
        return Session.getCurrentUser().getId();
    }

    @FXML
    private void handleEnroll() {
        int index = availableCoursesList.getSelectionModel().getSelectedIndex();
        if (index < 0 || index >= availableData.size()) {
            ErrorHandler.showError("Kayit olmak icin listeden bir ders seciniz.");
            return;
        }
        try {
            enrollmentService.enroll(availableData.get(index).getId(), studentId());
            ErrorHandler.showInfo("Derse basariyla kayit oldunuz.");
            refreshCourses();
        } catch (Exception e) {
            ErrorHandler.handle("Kayit yapilamadi.", e);
        }
    }

    private void refreshCourses() {

        availableData = courseService.getAllCourses();
        availableCoursesList.getItems().clear();
        for (Course c : availableData) {
            int remaining = courseService.remainingQuota(c);
            availableCoursesList.getItems().add(
                    c.getCode() + " - " + c.getName()
                            + "  | Kalan kontenjan: " + remaining + "/" + c.getQuota()
                            + "  | Kredi: " + c.getCredit());
        }

        myCoursesList.getItems().clear();
        List<Course> myCourses = new ArrayList<>();
        for (Enrollment en : enrollmentService.getStudentEnrollments(studentId())) {
            Course c = courseService.getById(en.getCourseId());
            if (c != null) {
                myCourses.add(c);
                myCoursesList.getItems().add(c.getCode() + " - " + c.getName());
            }
        }

        courseCombo.getItems().setAll(myCourses);
    }

    @FXML
    private void handleLoadStudentCourse() {
        Course c = courseCombo.getValue();
        if (c == null) {
            ErrorHandler.showError("Lutfen kayitli oldugunuz bir ders seciniz.");
            return;
        }
        assignmentsCombo.getItems().setAll(assignmentService.getAssignmentsByCourse(c.getId()));
        announcementsList.getItems().clear();
        for (Announcement an : announcementService.getByCourse(c.getId())) {
            announcementsList.getItems().add(an.getTitle() + "  (" + an.getDate() + "): " + an.getContent());
        }
        if (announcementsList.getItems().isEmpty()) {
            announcementsList.getItems().add("Bu derse ait duyuru yok.");
        }
    }

    @FXML
    private void handleUpload() {
        Assignment assignment = assignmentsCombo.getValue();
        if (assignment == null) {
            ErrorHandler.showError("Once teslim edilecek odevi seciniz.");
            return;
        }
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Odev dosyasini sec");
        chooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("PDF / DOCX", "*.pdf", "*.docx"));
        File file = chooser.showOpenDialog(welcomeLabel.getScene().getWindow());
        if (file == null) {
            return;
        }
        try {
            assignmentService.submitFile(assignment.getId(), studentId(), file);
            ErrorHandler.showInfo("Odev teslim edildi.");
        } catch (Exception e) {
            ErrorHandler.handle("Dosya yuklenemedi.", e);
        }
    }

    @FXML
    private void handleShowTranscript() {
        try {
            String sid = studentId();
            transcriptList.getItems().clear();
            for (TranscriptRow r : gradeService.getTranscript(sid)) {
                transcriptList.getItems().add(
                        r.getCourseCode() + " - " + r.getCourseName()
                                + "  | Kredi: " + r.getCredit()
                                + "  | Not: " + r.getScore() + " (" + r.getLetterGrade() + ")");
            }
            if (transcriptList.getItems().isEmpty()) {
                transcriptList.getItems().add("Henuz girilmis notunuz yok.");
            }
            double gpa = gradeService.calculateGpa(sid);
            gpaLabel.setText("GANO (Genel Not Ortalamasi): " + gpa);
        } catch (Exception e) {
            ErrorHandler.handle("Transkript getirilemedi.", e);
        }
    }

    @FXML
    private void handleLogout() {
        Session.logout();
        SceneManager.switchTo(welcomeLabel, "/fxml/login.fxml", "Okul Yonetim Sistemi - Giris");
    }
}
