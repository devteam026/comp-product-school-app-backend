package com.company.schoolbackend.dto;

public class PeriodDto {
    private Long id;
    private String dayOfWeek;
    private Integer periodNo;
    private String startTime;
    private String endTime;
    private String startDate;
    private String endDate;

    public PeriodDto() {}

    public PeriodDto(Long id, String dayOfWeek, Integer periodNo, String startTime, String endTime,
                     String startDate, String endDate) {
        this.id = id;
        this.dayOfWeek = dayOfWeek;
        this.periodNo = periodNo;
        this.startTime = startTime;
        this.endTime = endTime;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getDayOfWeek() { return dayOfWeek; }
    public void setDayOfWeek(String dayOfWeek) { this.dayOfWeek = dayOfWeek; }
    public Integer getPeriodNo() { return periodNo; }
    public void setPeriodNo(Integer periodNo) { this.periodNo = periodNo; }
    public String getStartTime() { return startTime; }
    public void setStartTime(String startTime) { this.startTime = startTime; }
    public String getEndTime() { return endTime; }
    public void setEndTime(String endTime) { this.endTime = endTime; }
    public String getStartDate() { return startDate; }
    public void setStartDate(String startDate) { this.startDate = startDate; }
    public String getEndDate() { return endDate; }
    public void setEndDate(String endDate) { this.endDate = endDate; }
}
