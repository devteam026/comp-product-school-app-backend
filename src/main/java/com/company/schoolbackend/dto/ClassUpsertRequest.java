package com.company.schoolbackend.dto;

public class ClassUpsertRequest {
    private String classCode;
    private String name;
    private String section;
    private String grade;
    private Boolean active;

    public String getClassCode() { return classCode; }
    public void setClassCode(String classCode) { this.classCode = classCode; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getSection() { return section; }
    public void setSection(String section) { this.section = section; }
    public String getGrade() { return grade; }
    public void setGrade(String grade) { this.grade = grade; }
    public Boolean getActive() { return active; }
    public void setActive(Boolean active) { this.active = active; }
}
