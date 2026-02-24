package com.company.schoolbackend.dto;

public class ClassAttendancePoint {
    private String classCode;
    private int present;
    private int absent;

    public ClassAttendancePoint() {}

    public ClassAttendancePoint(String classCode, int present, int absent) {
        this.classCode = classCode;
        this.present = present;
        this.absent = absent;
    }

    public String getClassCode() {
        return classCode;
    }

    public void setClassCode(String classCode) {
        this.classCode = classCode;
    }

    public int getPresent() {
        return present;
    }

    public void setPresent(int present) {
        this.present = present;
    }

    public int getAbsent() {
        return absent;
    }

    public void setAbsent(int absent) {
        this.absent = absent;
    }
}
