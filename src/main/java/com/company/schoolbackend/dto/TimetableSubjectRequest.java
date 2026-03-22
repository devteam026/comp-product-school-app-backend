package com.company.schoolbackend.dto;

public class TimetableSubjectRequest {
    private Long classId;
    private String name;
    private String color;

    public Long getClassId() { return classId; }
    public void setClassId(Long classId) { this.classId = classId; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getColor() { return color; }
    public void setColor(String color) { this.color = color; }
}
