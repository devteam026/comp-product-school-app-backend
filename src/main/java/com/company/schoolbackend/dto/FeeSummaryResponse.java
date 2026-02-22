package com.company.schoolbackend.dto;

import java.util.ArrayList;
import java.util.List;

public class FeeSummaryResponse {
    private String month;
    private int monthlyFee;
    private int paidCount;
    private int unpaidCount;
    private int freeCount;
    private String paidTotal;
    private List<String> unpaidStudentIds = new ArrayList<>();

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public int getMonthlyFee() {
        return monthlyFee;
    }

    public void setMonthlyFee(int monthlyFee) {
        this.monthlyFee = monthlyFee;
    }

    public int getPaidCount() {
        return paidCount;
    }

    public void setPaidCount(int paidCount) {
        this.paidCount = paidCount;
    }

    public int getUnpaidCount() {
        return unpaidCount;
    }

    public void setUnpaidCount(int unpaidCount) {
        this.unpaidCount = unpaidCount;
    }

    public int getFreeCount() {
        return freeCount;
    }

    public void setFreeCount(int freeCount) {
        this.freeCount = freeCount;
    }

    public String getPaidTotal() {
        return paidTotal;
    }

    public void setPaidTotal(String paidTotal) {
        this.paidTotal = paidTotal;
    }

    public List<String> getUnpaidStudentIds() {
        return unpaidStudentIds;
    }

    public void setUnpaidStudentIds(List<String> unpaidStudentIds) {
        this.unpaidStudentIds = unpaidStudentIds;
    }
}
