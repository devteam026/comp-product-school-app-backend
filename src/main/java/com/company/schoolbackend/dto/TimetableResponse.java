package com.company.schoolbackend.dto;

import java.util.List;

public class TimetableResponse {
    private String weekKey;
    private List<PeriodDto> periods;
    private List<TimetableAssignmentDto> assignments;
    private List<TeacherDto> teachers;
    private List<SubjectDto> subjects;
    private List<ClassDto> classes;

    public String getWeekKey() { return weekKey; }
    public void setWeekKey(String weekKey) { this.weekKey = weekKey; }
    public List<PeriodDto> getPeriods() { return periods; }
    public void setPeriods(List<PeriodDto> periods) { this.periods = periods; }
    public List<TimetableAssignmentDto> getAssignments() { return assignments; }
    public void setAssignments(List<TimetableAssignmentDto> assignments) { this.assignments = assignments; }
    public List<TeacherDto> getTeachers() { return teachers; }
    public void setTeachers(List<TeacherDto> teachers) { this.teachers = teachers; }
    public List<SubjectDto> getSubjects() { return subjects; }
    public void setSubjects(List<SubjectDto> subjects) { this.subjects = subjects; }
    public List<ClassDto> getClasses() { return classes; }
    public void setClasses(List<ClassDto> classes) { this.classes = classes; }
}
