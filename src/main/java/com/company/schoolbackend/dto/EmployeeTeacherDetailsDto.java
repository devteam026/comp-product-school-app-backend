package com.company.schoolbackend.dto;

public class EmployeeTeacherDetailsDto {
    private String subjectsAssigned;
    private String classesAssigned;
    private String periodAllocation;
    private Boolean classTeacher;

    public String getSubjectsAssigned() {
        return subjectsAssigned;
    }

    public void setSubjectsAssigned(String subjectsAssigned) {
        this.subjectsAssigned = subjectsAssigned;
    }

    public String getClassesAssigned() {
        return classesAssigned;
    }

    public void setClassesAssigned(String classesAssigned) {
        this.classesAssigned = classesAssigned;
    }

    public String getPeriodAllocation() {
        return periodAllocation;
    }

    public void setPeriodAllocation(String periodAllocation) {
        this.periodAllocation = periodAllocation;
    }

    public Boolean getClassTeacher() {
        return classTeacher;
    }

    public void setClassTeacher(Boolean classTeacher) {
        this.classTeacher = classTeacher;
    }
}
