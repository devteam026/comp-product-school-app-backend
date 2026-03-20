package com.company.schoolbackend.dto;

public class TimetableLockRequest {
    private Long assignmentId;
    private boolean locked;

    public Long getAssignmentId() { return assignmentId; }
    public void setAssignmentId(Long assignmentId) { this.assignmentId = assignmentId; }
    public boolean isLocked() { return locked; }
    public void setLocked(boolean locked) { this.locked = locked; }
}
