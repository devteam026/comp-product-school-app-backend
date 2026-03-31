package com.company.schoolbackend.dto;

import java.util.ArrayList;
import java.util.List;

public class StudentResponse {
    private String id;
    private String name;
    private String grade;
    private String section;
    private String classCode;
    private String gender;
    private String dateOfBirth;
    private String admissionNumber;
    private String registerNo;
    private String rollNumber;
    private String address;
    private String session;
    private String fatherName;
    private String motherName;
    private String parentName;
    private String parentRelation;
    private String parentPhone;
    private String parentWhatsapp;
    private String parentEmail;
    private String parentOccupation;
    private Boolean transportRequired;
    private String transportRoute;
    private String transportVehicleNo;
    private String transportStopName;
    private Boolean hostelRequired;
    private String hostelName;
    private String hostelRoomNo;
    private String previousSchoolName;
    private String previousQualification;
    private String status;
    private String feeType;
    private List<String> history = new ArrayList<>();
    private List<StudentHistoryEntry> historyEntries = new ArrayList<>();
    private String profilePhotoKey;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public String getSection() {
        return section;
    }

    public void setSection(String section) {
        this.section = section;
    }

    public String getClassCode() {
        return classCode;
    }

    public void setClassCode(String classCode) {
        this.classCode = classCode;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getAdmissionNumber() {
        return admissionNumber;
    }

    public void setAdmissionNumber(String admissionNumber) {
        this.admissionNumber = admissionNumber;
    }

    public String getRegisterNo() {
        return registerNo;
    }

    public void setRegisterNo(String registerNo) {
        this.registerNo = registerNo;
    }

    public String getRollNumber() {
        return rollNumber;
    }

    public void setRollNumber(String rollNumber) {
        this.rollNumber = rollNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getSession() {
        return session;
    }

    public void setSession(String session) {
        this.session = session;
    }

    public String getFatherName() {
        return fatherName;
    }

    public void setFatherName(String fatherName) {
        this.fatherName = fatherName;
    }

    public String getMotherName() {
        return motherName;
    }

    public void setMotherName(String motherName) {
        this.motherName = motherName;
    }

    public String getParentName() {
        return parentName;
    }

    public void setParentName(String parentName) {
        this.parentName = parentName;
    }

    public String getParentRelation() {
        return parentRelation;
    }

    public void setParentRelation(String parentRelation) {
        this.parentRelation = parentRelation;
    }

    public String getParentPhone() {
        return parentPhone;
    }

    public void setParentPhone(String parentPhone) {
        this.parentPhone = parentPhone;
    }

    public String getParentWhatsapp() {
        return parentWhatsapp;
    }

    public void setParentWhatsapp(String parentWhatsapp) {
        this.parentWhatsapp = parentWhatsapp;
    }

    public String getParentEmail() {
        return parentEmail;
    }

    public void setParentEmail(String parentEmail) {
        this.parentEmail = parentEmail;
    }

    public String getParentOccupation() {
        return parentOccupation;
    }

    public void setParentOccupation(String parentOccupation) {
        this.parentOccupation = parentOccupation;
    }

    public Boolean getTransportRequired() {
        return transportRequired;
    }

    public void setTransportRequired(Boolean transportRequired) {
        this.transportRequired = transportRequired;
    }

    public String getTransportRoute() {
        return transportRoute;
    }

    public void setTransportRoute(String transportRoute) {
        this.transportRoute = transportRoute;
    }

    public String getTransportVehicleNo() {
        return transportVehicleNo;
    }

    public void setTransportVehicleNo(String transportVehicleNo) {
        this.transportVehicleNo = transportVehicleNo;
    }

    public String getTransportStopName() {
        return transportStopName;
    }

    public void setTransportStopName(String transportStopName) {
        this.transportStopName = transportStopName;
    }

    public Boolean getHostelRequired() {
        return hostelRequired;
    }

    public void setHostelRequired(Boolean hostelRequired) {
        this.hostelRequired = hostelRequired;
    }

    public String getHostelName() {
        return hostelName;
    }

    public void setHostelName(String hostelName) {
        this.hostelName = hostelName;
    }

    public String getHostelRoomNo() {
        return hostelRoomNo;
    }

    public void setHostelRoomNo(String hostelRoomNo) {
        this.hostelRoomNo = hostelRoomNo;
    }

    public String getPreviousSchoolName() {
        return previousSchoolName;
    }

    public void setPreviousSchoolName(String previousSchoolName) {
        this.previousSchoolName = previousSchoolName;
    }

    public String getPreviousQualification() {
        return previousQualification;
    }

    public void setPreviousQualification(String previousQualification) {
        this.previousQualification = previousQualification;
    }
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getFeeType() {
        return feeType;
    }

    public void setFeeType(String feeType) {
        this.feeType = feeType;
    }

    public List<String> getHistory() {
        return history;
    }

    public void setHistory(List<String> history) {
        this.history = history;
    }

    public List<StudentHistoryEntry> getHistoryEntries() {
        return historyEntries;
    }

    public void setHistoryEntries(List<StudentHistoryEntry> historyEntries) {
        this.historyEntries = historyEntries;
    }

    public String getProfilePhotoKey() {
        return profilePhotoKey;
    }

    public void setProfilePhotoKey(String profilePhotoKey) {
        this.profilePhotoKey = profilePhotoKey;
    }
}
