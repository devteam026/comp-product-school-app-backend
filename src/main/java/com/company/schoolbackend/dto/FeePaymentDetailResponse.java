package com.company.schoolbackend.dto;

public class FeePaymentDetailResponse {
    private Long id;
    private Long dueId;
    private String dueAmount;
    private String defaultDiscount;
    private String extraDiscount;
    private String fineAmount;
    private String finalAmount;
    private String paidAmount;
    private String extraDiscountReason;
    private String approvedBy;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getDueId() {
        return dueId;
    }

    public void setDueId(Long dueId) {
        this.dueId = dueId;
    }

    public String getDueAmount() {
        return dueAmount;
    }

    public void setDueAmount(String dueAmount) {
        this.dueAmount = dueAmount;
    }

    public String getDefaultDiscount() {
        return defaultDiscount;
    }

    public void setDefaultDiscount(String defaultDiscount) {
        this.defaultDiscount = defaultDiscount;
    }

    public String getExtraDiscount() {
        return extraDiscount;
    }

    public void setExtraDiscount(String extraDiscount) {
        this.extraDiscount = extraDiscount;
    }

    public String getFineAmount() {
        return fineAmount;
    }

    public void setFineAmount(String fineAmount) {
        this.fineAmount = fineAmount;
    }

    public String getFinalAmount() {
        return finalAmount;
    }

    public void setFinalAmount(String finalAmount) {
        this.finalAmount = finalAmount;
    }

    public String getPaidAmount() {
        return paidAmount;
    }

    public void setPaidAmount(String paidAmount) {
        this.paidAmount = paidAmount;
    }

    public String getExtraDiscountReason() {
        return extraDiscountReason;
    }

    public void setExtraDiscountReason(String extraDiscountReason) {
        this.extraDiscountReason = extraDiscountReason;
    }

    public String getApprovedBy() {
        return approvedBy;
    }

    public void setApprovedBy(String approvedBy) {
        this.approvedBy = approvedBy;
    }
}
