package com.okul.model;

public class Grade {

    private String id;
    private String courseId;
    private String studentId;
    private double score;
    private String letterGrade;

    public Grade() {
    }

    public Grade(String courseId, String studentId, double score, String letterGrade) {
        this.courseId = courseId;
        this.studentId = studentId;
        this.score = score;
        this.letterGrade = letterGrade;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }

    public String getLetterGrade() {
        return letterGrade;
    }

    public void setLetterGrade(String letterGrade) {
        this.letterGrade = letterGrade;
    }
}
