package com.company.schoolbackend.dto;

public class AttendanceSaveResponse {
    private boolean ok;
    private String date;
    private int savedCount;

    public AttendanceSaveResponse() {}

    public AttendanceSaveResponse(boolean ok, String date, int savedCount) {
        this.ok = ok;
        this.date = date;
        this.savedCount = savedCount;
    }

    public boolean isOk() {
        return ok;
    }

    public void setOk(boolean ok) {
        this.ok = ok;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getSavedCount() {
        return savedCount;
    }

    public void setSavedCount(int savedCount) {
        this.savedCount = savedCount;
    }
}
