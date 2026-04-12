package com.company.schoolbackend.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.OffsetDateTime;

@Entity
@Table(name = "school_profile")
public class SchoolProfile {
    @Id
    private Long id;

    @Column(name = "school_name", nullable = false)
    private String schoolName;

    @Column(length = 1000)
    private String about;

    @Column(length = 1000)
    private String mission;

    @Column(length = 1000)
    private String vision;

    @Column(name = "logo_url")
    private String logoUrl;

    @Column(name = "campus_image_url")
    private String campusImageUrl;

    @Column(name = "school_url")
    private String schoolUrl;

    @Column(name = "sidebar_bg")
    private String sidebarBg;

    @Column(name = "brand_name")
    private String brandName;

    @Column(name = "app_title")
    private String appTitle;

    @Column(name = "app_description")
    private String appDescription;

    @Column(length = 500)
    private String address;

    @Column(length = 50)
    private String phone;

    @Column(name = "updated_at", nullable = false)
    private OffsetDateTime updatedAt;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSchoolName() {
        return schoolName;
    }

    public void setSchoolName(String schoolName) {
        this.schoolName = schoolName;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public String getMission() {
        return mission;
    }

    public void setMission(String mission) {
        this.mission = mission;
    }

    public String getVision() {
        return vision;
    }

    public void setVision(String vision) {
        this.vision = vision;
    }

    public String getLogoUrl() {
        return logoUrl;
    }

    public void setLogoUrl(String logoUrl) {
        this.logoUrl = logoUrl;
    }

    public String getCampusImageUrl() {
        return campusImageUrl;
    }

    public void setCampusImageUrl(String campusImageUrl) {
        this.campusImageUrl = campusImageUrl;
    }

    public String getSchoolUrl() {
        return schoolUrl;
    }

    public void setSchoolUrl(String schoolUrl) {
        this.schoolUrl = schoolUrl;
    }

    public String getSidebarBg() {
        return sidebarBg;
    }

    public void setSidebarBg(String sidebarBg) {
        this.sidebarBg = sidebarBg;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public String getAppTitle() {
        return appTitle;
    }

    public void setAppTitle(String appTitle) {
        this.appTitle = appTitle;
    }

    public String getAppDescription() {
        return appDescription;
    }

    public void setAppDescription(String appDescription) {
        this.appDescription = appDescription;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public OffsetDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(OffsetDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
