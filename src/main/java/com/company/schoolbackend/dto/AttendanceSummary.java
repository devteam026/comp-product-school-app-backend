package com.company.schoolbackend.dto;

public class AttendanceSummary {
    private int present;
    private int absent;
    private int notRecorded;

    public AttendanceSummary() {}

    public AttendanceSummary(int present, int absent, int notRecorded) {
        this.present = present;
        this.absent = absent;
        this.notRecorded = notRecorded;
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

    public int getNotRecorded() {
        return notRecorded;
    }

    public void setNotRecorded(int notRecorded) {
        this.notRecorded = notRecorded;
    }
}
