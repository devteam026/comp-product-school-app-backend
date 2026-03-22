package com.company.schoolbackend.dto;

import java.util.List;

public class EmployeeClassAssignRequest {
    private List<String> classCodes;

    public List<String> getClassCodes() {
        return classCodes;
    }

    public void setClassCodes(List<String> classCodes) {
        this.classCodes = classCodes;
    }
}
