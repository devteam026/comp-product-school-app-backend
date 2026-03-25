package com.company.schoolbackend.dto;

public class GuardianInfo {
    private String id;
    private String parentName;
    private String parentRelation;
    private String parentPhone;
    private String parentWhatsapp;
    private String parentEmail;
    private String parentOccupation;
    private String fatherName;
    private String motherName;

    public GuardianInfo(
            String id,
            String parentName,
            String parentRelation,
            String parentPhone,
            String parentWhatsapp,
            String parentEmail,
            String parentOccupation,
            String fatherName,
            String motherName
    ) {
        this.id = id;
        this.parentName = parentName;
        this.parentRelation = parentRelation;
        this.parentPhone = parentPhone;
        this.parentWhatsapp = parentWhatsapp;
        this.parentEmail = parentEmail;
        this.parentOccupation = parentOccupation;
        this.fatherName = fatherName;
        this.motherName = motherName;
    }

    public String getId() {
        return id;
    }

    public String getParentName() {
        return parentName;
    }

    public String getParentRelation() {
        return parentRelation;
    }

    public String getParentPhone() {
        return parentPhone;
    }

    public String getParentWhatsapp() {
        return parentWhatsapp;
    }

    public String getParentEmail() {
        return parentEmail;
    }

    public String getParentOccupation() {
        return parentOccupation;
    }

    public String getFatherName() {
        return fatherName;
    }

    public String getMotherName() {
        return motherName;
    }
}
