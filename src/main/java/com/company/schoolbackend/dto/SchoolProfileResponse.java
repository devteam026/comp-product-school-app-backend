package com.company.schoolbackend.dto;

public class SchoolProfileResponse {
    private String schoolName;
    private String about;
    private String mission;
    private String vision;
    private String logoUrl;
    private String campusImageUrl;
    private String schoolUrl;

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
}
