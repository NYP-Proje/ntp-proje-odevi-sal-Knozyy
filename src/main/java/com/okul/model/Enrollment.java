package com.okul.model;

public class Enrollment {

    private String id;
    private String courseId;
    private String studentId;
    private String enrollDate;

    public Enrollment() {
    }

    public Enrollment(String courseId, String studentId, String enrollDate) {
        this.courseId = courseId;
        this.studentId = studentId;
        this.enrollDate = enrollDate;
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

    public String getEnrollDate() {
        return enrollDate;
    }

    public void setEnrollDate(String enrollDate) {
        this.enrollDate = enrollDate;
    }
}
