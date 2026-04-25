package com.company.schoolbackend.dto;

import java.util.ArrayList;
import java.util.List;

public class DashboardResponse {
  private int totalStudents;
  private int maleCount;
  private int femaleCount;
  private AttendanceSummary attendanceToday;
  private FeeStats feeStats;
  private List<DailyAttendancePoint> dailyAttendance = new ArrayList<>();
  private List<ClassAttendancePoint> classAttendance = new ArrayList<>();
  private List<ClassAttendancePoint> classStudentCounts = new ArrayList<>();
  private int newAdmissionsThisMonth;
  private int pendingLeaveCount;
  private int totalEmployees;
  private int workingEmployees;
  private int onLeaveEmployees;

    public int getTotalStudents() {
        return totalStudents;
    }

    public void setTotalStudents(int totalStudents) {
        this.totalStudents = totalStudents;
    }

    public int getMaleCount() {
        return maleCount;
    }

    public void setMaleCount(int maleCount) {
        this.maleCount = maleCount;
    }

    public int getFemaleCount() {
        return femaleCount;
    }

    public void setFemaleCount(int femaleCount) {
        this.femaleCount = femaleCount;
    }

    public AttendanceSummary getAttendanceToday() {
        return attendanceToday;
    }

    public void setAttendanceToday(AttendanceSummary attendanceToday) {
        this.attendanceToday = attendanceToday;
    }

    public FeeStats getFeeStats() {
        return feeStats;
    }

    public void setFeeStats(FeeStats feeStats) {
        this.feeStats = feeStats;
    }

  public List<DailyAttendancePoint> getDailyAttendance() {
    return dailyAttendance;
  }

  public void setDailyAttendance(List<DailyAttendancePoint> dailyAttendance) {
    this.dailyAttendance = dailyAttendance;
  }

  public List<ClassAttendancePoint> getClassAttendance() {
    return classAttendance;
  }

  public void setClassAttendance(List<ClassAttendancePoint> classAttendance) {
    this.classAttendance = classAttendance;
  }

  public List<ClassAttendancePoint> getClassStudentCounts() {
    return classStudentCounts;
  }

  public void setClassStudentCounts(List<ClassAttendancePoint> classStudentCounts) {
    this.classStudentCounts = classStudentCounts;
  }

  public int getNewAdmissionsThisMonth() {
    return newAdmissionsThisMonth;
  }

  public void setNewAdmissionsThisMonth(int newAdmissionsThisMonth) {
    this.newAdmissionsThisMonth = newAdmissionsThisMonth;
  }

  public int getPendingLeaveCount() {
    return pendingLeaveCount;
  }

  public void setPendingLeaveCount(int pendingLeaveCount) {
    this.pendingLeaveCount = pendingLeaveCount;
  }

  public int getTotalEmployees() {
    return totalEmployees;
  }

  public void setTotalEmployees(int totalEmployees) {
    this.totalEmployees = totalEmployees;
  }

  public int getWorkingEmployees() {
    return workingEmployees;
  }

  public void setWorkingEmployees(int workingEmployees) {
    this.workingEmployees = workingEmployees;
  }

  public int getOnLeaveEmployees() {
    return onLeaveEmployees;
  }

  public void setOnLeaveEmployees(int onLeaveEmployees) {
    this.onLeaveEmployees = onLeaveEmployees;
  }
}
