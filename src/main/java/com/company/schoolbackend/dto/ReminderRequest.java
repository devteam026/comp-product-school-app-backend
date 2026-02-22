package com.company.schoolbackend.dto;

import java.util.ArrayList;
import java.util.List;

public class ReminderRequest {
    private String scope;
    private List<String> studentIds = new ArrayList<>();

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public List<String> getStudentIds() {
        return studentIds;
    }

    public void setStudentIds(List<String> studentIds) {
        this.studentIds = studentIds;
    }
}
