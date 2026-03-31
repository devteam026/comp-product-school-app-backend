package com.company.schoolbackend.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.time.OffsetDateTime;

@Entity
@Table(name = "students")
public class Student {
    @Id
    @Column(length = 36)
    private String id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String grade;

    @Column(nullable = false)
    private String section;

    @Column(name = "class_code", nullable = false)
    private String classCode;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Gender gender;

    private LocalDate dateOfBirth;

    @Column(name = "admission_number")
    private String admissionNumber;

    @Column(name = "register_no")
    private String registerNo;

    @Column(name = "roll_number")
    private String rollNumber;

    private String address;

    @Column(name = "academic_session")
    private String session;

    @Column(name = "father_name")
    private String fatherName;

    @Column(name = "mother_name")
    private String motherName;

    @Column(name = "parent_name")
    private String parentName;

    @Column(name = "parent_relation")
    private String parentRelation;

    @Column(name = "parent_phone")
    private String parentPhone;

    @Column(name = "parent_whatsapp")
    private String parentWhatsapp;

    @Column(name = "parent_email")
    private String parentEmail;

    @Column(name = "parent_occupation")
    private String parentOccupation;

    @Column(name = "transport_required")
    private Boolean transportRequired;

    @Column(name = "transport_route")
    private String transportRoute;

    @Column(name = "transport_vehicle_no")
    private String transportVehicleNo;

    @Column(name = "transport_stop_name")
    private String transportStopName;

    @Column(name = "hostel_required")
    private Boolean hostelRequired;

    @Column(name = "hostel_name")
    private String hostelName;

    @Column(name = "hostel_room_no")
    private String hostelRoomNo;

    @Column(name = "previous_school_name")
    private String previousSchoolName;

    @Column(name = "previous_qualification")
    private String previousQualification;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StudentStatus status;

    @Enumerated(EnumType.STRING)
    @Column(name = "fee_type", nullable = false)
    private FeeType feeType;

    @Column(name = "profile_photo_key")
    private String profilePhotoKey;

    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private OffsetDateTime updatedAt;

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

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
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

    public StudentStatus getStatus() {
        return status;
    }

    public void setStatus(StudentStatus status) {
        this.status = status;
    }

    public FeeType getFeeType() {
        return feeType;
    }

    public void setFeeType(FeeType feeType) {
        this.feeType = feeType;
    }

    public String getProfilePhotoKey() {
        return profilePhotoKey;
    }

    public void setProfilePhotoKey(String profilePhotoKey) {
        this.profilePhotoKey = profilePhotoKey;
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(OffsetDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public OffsetDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(OffsetDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
