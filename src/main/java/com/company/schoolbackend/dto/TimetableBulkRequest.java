package com.company.schoolbackend.dto;

import java.util.List;

public class TimetableBulkRequest {
    private String weekKey;
    private List<TimetableCreateRequest> assignments;

    public String getWeekKey() { return weekKey; }
    public void setWeekKey(String weekKey) { this.weekKey = weekKey; }
    public List<TimetableCreateRequest> getAssignments() { return assignments; }
    public void setAssignments(List<TimetableCreateRequest> assignments) { this.assignments = assignments; }
}
