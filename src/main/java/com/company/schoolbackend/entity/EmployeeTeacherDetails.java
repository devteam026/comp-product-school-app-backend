package com.company.schoolbackend.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "employee_teacher_details")
public class EmployeeTeacherDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "employee_id", nullable = false, unique = true)
    private Long employeeId;

    @Column(name = "subjects_assigned", length = 2000)
    private String subjectsAssigned;

    @Column(name = "classes_assigned", length = 2000)
    private String classesAssigned;

    @Column(name = "period_allocation", length = 2000)
    private String periodAllocation;

    @Column(name = "class_teacher")
    private Boolean classTeacher;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Long employeeId) {
        this.employeeId = employeeId;
    }

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
