package com.company.schoolbackend.dto;

public class TimetableCopyRequest {
    private String fromWeek;
    private String toWeek;
    private Long classId;

    public String getFromWeek() { return fromWeek; }
    public void setFromWeek(String fromWeek) { this.fromWeek = fromWeek; }
    public String getToWeek() { return toWeek; }
    public void setToWeek(String toWeek) { this.toWeek = toWeek; }
    public Long getClassId() { return classId; }
    public void setClassId(Long classId) { this.classId = classId; }
}
