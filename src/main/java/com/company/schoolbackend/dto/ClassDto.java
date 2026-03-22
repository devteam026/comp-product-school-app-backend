package com.company.schoolbackend.dto;

public class ClassDto {
    private Long id;
    private String name;
    private String section;
    private String classCode;

    public ClassDto() {}

    public ClassDto(Long id, String name, String section, String classCode) {
        this.id = id;
        this.name = name;
        this.section = section;
        this.classCode = classCode;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getSection() { return section; }
    public void setSection(String section) { this.section = section; }
    public String getClassCode() { return classCode; }
    public void setClassCode(String classCode) { this.classCode = classCode; }
}
