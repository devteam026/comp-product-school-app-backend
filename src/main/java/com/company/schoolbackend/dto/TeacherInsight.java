package com.company.schoolbackend.dto;

import java.util.ArrayList;
import java.util.List;

public class TeacherInsight {
    private String name;
    private String subject;
    private double rating;
    private List<String> classes = new ArrayList<>();

    public TeacherInsight() {}

    public TeacherInsight(String name, String subject, double rating, List<String> classes) {
        this.name = name;
        this.subject = subject;
        this.rating = rating;
        this.classes = classes;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public List<String> getClasses() {
        return classes;
    }

    public void setClasses(List<String> classes) {
        this.classes = classes;
    }
}
