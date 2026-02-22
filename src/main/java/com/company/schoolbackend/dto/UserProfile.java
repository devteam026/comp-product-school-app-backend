package com.company.schoolbackend.dto;

public class UserProfile {
    private String username;
    private String displayName;
    private String role;
    private String classCode;

    public UserProfile() {}

    public UserProfile(String username, String displayName, String role, String classCode) {
        this.username = username;
        this.displayName = displayName;
        this.role = role;
        this.classCode = classCode;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getClassCode() {
        return classCode;
    }

    public void setClassCode(String classCode) {
        this.classCode = classCode;
    }
}
