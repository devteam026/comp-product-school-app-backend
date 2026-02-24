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
}
