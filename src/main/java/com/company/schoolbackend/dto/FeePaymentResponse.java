package com.company.schoolbackend.dto;

import java.util.ArrayList;
import java.util.List;

public class FeePaymentResponse {
    private Long id;
    private String studentId;
    private String totalDue;
    private String totalDefaultDiscount;
    private String totalExtraDiscount;
    private String totalFine;
    private String finalAmount;
    private String paidAmount;
    private String paymentDate;
    private String paymentMode;
    private String receiptNumber;
    private List<FeePaymentDetailResponse> details = new ArrayList<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getTotalDue() {
        return totalDue;
    }

    public void setTotalDue(String totalDue) {
        this.totalDue = totalDue;
    }

    public String getTotalDefaultDiscount() {
        return totalDefaultDiscount;
    }

    public void setTotalDefaultDiscount(String totalDefaultDiscount) {
        this.totalDefaultDiscount = totalDefaultDiscount;
    }

    public String getTotalExtraDiscount() {
        return totalExtraDiscount;
    }

    public void setTotalExtraDiscount(String totalExtraDiscount) {
        this.totalExtraDiscount = totalExtraDiscount;
    }

    public String getTotalFine() {
        return totalFine;
    }

    public void setTotalFine(String totalFine) {
        this.totalFine = totalFine;
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

    public String getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(String paymentDate) {
        this.paymentDate = paymentDate;
    }

    public String getPaymentMode() {
        return paymentMode;
    }

    public void setPaymentMode(String paymentMode) {
        this.paymentMode = paymentMode;
    }

    public String getReceiptNumber() {
        return receiptNumber;
    }

    public void setReceiptNumber(String receiptNumber) {
        this.receiptNumber = receiptNumber;
    }

    public List<FeePaymentDetailResponse> getDetails() {
        return details;
    }

    public void setDetails(List<FeePaymentDetailResponse> details) {
        this.details = details;
    }
}
