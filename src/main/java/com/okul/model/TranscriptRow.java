package com.okul.model;

public class TranscriptRow {

    private String courseCode;
    private String courseName;
    private int credit;
    private double score;
    private String letterGrade;
    private double gpaValue;

    public TranscriptRow(String courseCode, String courseName, int credit,
                         double score, String letterGrade, double gpaValue) {
        this.courseCode = courseCode;
        this.courseName = courseName;
        this.credit = credit;
        this.score = score;
        this.letterGrade = letterGrade;
        this.gpaValue = gpaValue;
    }

    public String getCourseCode() {
        return courseCode;
    }

    public String getCourseName() {
        return courseName;
    }

    public int getCredit() {
        return credit;
    }

    public double getScore() {
        return score;
    }

    public String getLetterGrade() {
        return letterGrade;
    }

    public double getGpaValue() {
        return gpaValue;
    }
}
