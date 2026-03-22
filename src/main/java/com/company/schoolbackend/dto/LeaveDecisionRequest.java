package com.company.schoolbackend.dto;

public class LeaveDecisionRequest {
    private String status;
    private String reviewerNote;

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getReviewerNote() { return reviewerNote; }
    public void setReviewerNote(String reviewerNote) { this.reviewerNote = reviewerNote; }
}
