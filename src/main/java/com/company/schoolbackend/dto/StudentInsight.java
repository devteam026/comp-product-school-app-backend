package com.company.schoolbackend.dto;

public class StudentInsight {
    private String name;
    private String classCode;
    private double score;

    public StudentInsight() {}

    public StudentInsight(String name, String classCode, double score) {
        this.name = name;
        this.classCode = classCode;
        this.score = score;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getClassCode() {
        return classCode;
    }

    public void setClassCode(String classCode) {
        this.classCode = classCode;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }
}
