package com.company.schoolbackend.dto;

public class AttendanceRecordDto {
    private String studentId;
    private String status;

    public AttendanceRecordDto() {}

    public AttendanceRecordDto(String studentId, String status) {
        this.studentId = studentId;
        this.status = status;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
