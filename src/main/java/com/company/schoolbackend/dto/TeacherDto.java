package com.company.schoolbackend.dto;

import java.util.List;

public class TeacherDto {
    private Long id;
    private String name;
    private List<Long> subjectIds;

    public TeacherDto() {}

    public TeacherDto(Long id, String name, List<Long> subjectIds) {
        this.id = id;
        this.name = name;
        this.subjectIds = subjectIds;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public List<Long> getSubjectIds() { return subjectIds; }
    public void setSubjectIds(List<Long> subjectIds) { this.subjectIds = subjectIds; }
}
