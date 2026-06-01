package com.okul.service;

import com.okul.dao.AssignmentDao;
import com.okul.dao.SubmissionDao;
import com.okul.model.Assignment;
import com.okul.model.Submission;
import com.okul.util.FileManager;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

public class AssignmentService {

    private final AssignmentDao assignmentDao;
    private final SubmissionDao submissionDao;

    public AssignmentService() {
        this(new AssignmentDao(), new SubmissionDao());
    }

    public AssignmentService(AssignmentDao assignmentDao, SubmissionDao submissionDao) {
        this.assignmentDao = assignmentDao;
        this.submissionDao = submissionDao;
    }

    public void createAssignment(String courseId, String title, String description, String dueDate) {
        if (title == null || title.isBlank()) {
            throw new RuntimeException("Odev basligi bos olamaz.");
        }
        assignmentDao.save(new Assignment(courseId, title, description, dueDate));
    }

    public List<Assignment> getAssignmentsByCourse(String courseId) {
        return assignmentDao.findByCourseId(courseId);
    }

    public Assignment getById(String id) {
        return assignmentDao.findById(id);
    }

    public void submitFile(String assignmentId, String studentId, File file) {
        try {
            String savedPath = FileManager.save(file, studentId);
            submissionDao.save(new Submission(assignmentId, studentId, savedPath,
                    LocalDate.now().toString()));
        } catch (IOException e) {
            throw new RuntimeException("Dosya yuklenemedi.", e);
        }
    }

    public List<Submission> getSubmissions(String assignmentId) {
        return submissionDao.findByAssignmentId(assignmentId);
    }
}
