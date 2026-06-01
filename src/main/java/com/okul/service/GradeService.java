package com.okul.service;

import com.okul.dao.CourseDao;
import com.okul.dao.GradeDao;
import com.okul.model.Course;
import com.okul.model.Grade;
import com.okul.model.TranscriptRow;
import com.okul.util.GradeCalculator;

import java.util.ArrayList;
import java.util.List;

public class GradeService {

    private final GradeDao gradeDao;
    private final CourseDao courseDao;

    public GradeService() {
        this(new GradeDao(), new CourseDao());
    }

    public GradeService(GradeDao gradeDao, CourseDao courseDao) {
        this.gradeDao = gradeDao;
        this.courseDao = courseDao;
    }

    public void assignGrade(String courseId, String studentId, double score) {
        if (score < 0 || score > 100) {
            throw new RuntimeException("Not 0 ile 100 arasinda olmali.");
        }
        String letter = GradeCalculator.scoreToLetter(score);
        Grade existing = gradeDao.findByCourseAndStudent(courseId, studentId);
        if (existing == null) {
            gradeDao.save(new Grade(courseId, studentId, score, letter));
        } else {
            existing.setScore(score);
            existing.setLetterGrade(letter);
            gradeDao.update(existing);
        }
    }

    public List<TranscriptRow> getTranscript(String studentId) {
        List<TranscriptRow> rows = new ArrayList<>();
        for (Grade g : gradeDao.findByStudentId(studentId)) {
            Course c = courseDao.findById(g.getCourseId());
            if (c == null) {
                continue;
            }
            double gpaValue = GradeCalculator.letterToGpa(g.getLetterGrade());
            rows.add(new TranscriptRow(c.getCode(), c.getName(), c.getCredit(),
                    g.getScore(), g.getLetterGrade(), gpaValue));
        }
        return rows;
    }

    public double calculateGpa(String studentId) {
        return GradeCalculator.weightedGpa(getTranscript(studentId));
    }

    public List<Grade> getCourseRanking(String courseId) {
        List<Grade> grades = gradeDao.findByCourseId(courseId);
        grades.sort((a, b) -> Double.compare(b.getScore(), a.getScore()));
        return grades;
    }
}
