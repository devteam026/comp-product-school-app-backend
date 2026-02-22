package com.company.schoolbackend.dto;

import java.util.ArrayList;
import java.util.List;

public class AttendanceSaveRequest {
    private String date;
    private String classCode;
    private List<AttendanceRecordDto> records = new ArrayList<>();

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getClassCode() {
        return classCode;
    }

    public void setClassCode(String classCode) {
        this.classCode = classCode;
    }

    public List<AttendanceRecordDto> getRecords() {
        return records;
    }

    public void setRecords(List<AttendanceRecordDto> records) {
        this.records = records;
    }
}
