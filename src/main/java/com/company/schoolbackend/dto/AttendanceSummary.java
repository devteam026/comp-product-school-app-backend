package com.company.schoolbackend.dto;

public class AttendanceSummary {
    private int present;
    private int absent;

    public AttendanceSummary() {}

    public AttendanceSummary(int present, int absent) {
        this.present = present;
        this.absent = absent;
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
