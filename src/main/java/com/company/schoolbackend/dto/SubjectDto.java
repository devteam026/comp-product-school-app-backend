package com.company.schoolbackend.dto;

public class SubjectDto {
    private Long id;
    private String name;
    private String color;
    private String classCode;

    public SubjectDto() {}

    public SubjectDto(Long id, String name, String color) {
        this.id = id;
        this.name = name;
        this.color = color;
    }

    public SubjectDto(Long id, String name, String color, String classCode) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.classCode = classCode;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getColor() { return color; }
    public void setColor(String color) { this.color = color; }
    public String getClassCode() { return classCode; }
    public void setClassCode(String classCode) { this.classCode = classCode; }
}
