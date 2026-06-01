package com.okul.model;

public class Course {

    private String id;
    private String code;
    private String name;
    private String teacherId;
    private int quota;
    private int credit;
    private String term;

    public Course() {
    }

    public Course(String code, String name, String teacherId, int quota, int credit, String term) {
        this.code = code;
        this.name = name;
        this.teacherId = teacherId;
        this.quota = quota;
        this.credit = credit;
        this.term = term;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(String teacherId) {
        this.teacherId = teacherId;
    }

    public int getQuota() {
        return quota;
    }

    public void setQuota(int quota) {
        this.quota = quota;
    }

    public int getCredit() {
        return credit;
    }

    public void setCredit(int credit) {
        this.credit = credit;
    }

    public String getTerm() {
        return term;
    }

    public void setTerm(String term) {
        this.term = term;
    }

    @Override
    public String toString() {
        return code + " - " + name;
    }
}
