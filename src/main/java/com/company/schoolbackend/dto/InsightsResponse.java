package com.company.schoolbackend.dto;

import java.util.ArrayList;
import java.util.List;

public class InsightsResponse {
    private List<StudentInsight> topStudents = new ArrayList<>();
    private List<StudentInsight> atRiskStudents = new ArrayList<>();
    private List<TeacherInsight> topTeachers = new ArrayList<>();

    public List<StudentInsight> getTopStudents() {
        return topStudents;
    }

    public void setTopStudents(List<StudentInsight> topStudents) {
        this.topStudents = topStudents;
    }

    public List<StudentInsight> getAtRiskStudents() {
        return atRiskStudents;
    }

    public void setAtRiskStudents(List<StudentInsight> atRiskStudents) {
        this.atRiskStudents = atRiskStudents;
    }

    public List<TeacherInsight> getTopTeachers() {
        return topTeachers;
    }

    public void setTopTeachers(List<TeacherInsight> topTeachers) {
        this.topTeachers = topTeachers;
    }
}
