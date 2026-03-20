package com.company.schoolbackend.dto;

public class TimetableAssignmentDto {
    private Long id;
    private Long teacherId;
    private Long classId;
    private Long subjectId;
    private Long periodId;
    private String weekKey;
    private boolean locked;

    public TimetableAssignmentDto() {}

    public TimetableAssignmentDto(Long id, Long teacherId, Long classId, Long subjectId, Long periodId, String weekKey, boolean locked) {
        this.id = id;
        this.teacherId = teacherId;
        this.classId = classId;
        this.subjectId = subjectId;
        this.periodId = periodId;
        this.weekKey = weekKey;
        this.locked = locked;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getTeacherId() { return teacherId; }
    public void setTeacherId(Long teacherId) { this.teacherId = teacherId; }
    public Long getClassId() { return classId; }
    public void setClassId(Long classId) { this.classId = classId; }
    public Long getSubjectId() { return subjectId; }
    public void setSubjectId(Long subjectId) { this.subjectId = subjectId; }
    public Long getPeriodId() { return periodId; }
    public void setPeriodId(Long periodId) { this.periodId = periodId; }
    public String getWeekKey() { return weekKey; }
    public void setWeekKey(String weekKey) { this.weekKey = weekKey; }
    public boolean isLocked() { return locked; }
    public void setLocked(boolean locked) { this.locked = locked; }
}
