package com.company.schoolbackend.dto;

public class DailyAttendancePoint {
    private String day;
    private int present;
    private int absent;

    public DailyAttendancePoint() {}

    public DailyAttendancePoint(String day, int present, int absent) {
        this.day = day;
        this.present = present;
        this.absent = absent;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
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
