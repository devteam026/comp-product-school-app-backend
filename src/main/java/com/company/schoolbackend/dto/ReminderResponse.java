package com.company.schoolbackend.dto;

public class ReminderResponse {
    private boolean ok;
    private int count;
    private String message;

    public ReminderResponse() {}

    public ReminderResponse(boolean ok, int count, String message) {
        this.ok = ok;
        this.count = count;
        this.message = message;
    }

    public boolean isOk() {
        return ok;
    }

    public void setOk(boolean ok) {
        this.ok = ok;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
