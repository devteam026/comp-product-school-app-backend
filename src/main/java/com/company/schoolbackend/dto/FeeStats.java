package com.company.schoolbackend.dto;

public class FeeStats {
    private int paid;
    private int unpaid;
    private int free;

    public FeeStats() {}

    public FeeStats(int paid, int unpaid, int free) {
        this.paid = paid;
        this.unpaid = unpaid;
        this.free = free;
    }

    public int getPaid() {
        return paid;
    }

    public void setPaid(int paid) {
        this.paid = paid;
    }

    public int getUnpaid() {
        return unpaid;
    }

    public void setUnpaid(int unpaid) {
        this.unpaid = unpaid;
    }

    public int getFree() {
        return free;
    }

    public void setFree(int free) {
        this.free = free;
    }
}
