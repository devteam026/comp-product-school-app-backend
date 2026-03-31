package com.company.schoolbackend.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.math.BigDecimal;

@Entity
@Table(name = "fee_payment_details")
public class FeePaymentDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "payment_id", nullable = false)
    private Long paymentId;

    @Column(name = "due_id", nullable = false)
    private Long dueId;

    @Column(name = "due_amount", nullable = false, precision = 12, scale = 2)
    private BigDecimal dueAmount;

    @Column(name = "default_discount", nullable = false, precision = 12, scale = 2)
    private BigDecimal defaultDiscount;

    @Column(name = "extra_discount", nullable = false, precision = 12, scale = 2)
    private BigDecimal extraDiscount;

    @Column(name = "fine_amount", nullable = false, precision = 12, scale = 2)
    private BigDecimal fineAmount;

    @Column(name = "final_amount", nullable = false, precision = 12, scale = 2)
    private BigDecimal finalAmount;

    @Column(name = "paid_amount", nullable = false, precision = 12, scale = 2)
    private BigDecimal paidAmount;

    @Column(name = "extra_discount_reason", length = 200)
    private String extraDiscountReason;

    @Column(name = "approved_by", length = 120)
    private String approvedBy;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(Long paymentId) {
        this.paymentId = paymentId;
    }

    public Long getDueId() {
        return dueId;
    }

    public void setDueId(Long dueId) {
        this.dueId = dueId;
    }

    public BigDecimal getDueAmount() {
        return dueAmount;
    }

    public void setDueAmount(BigDecimal dueAmount) {
        this.dueAmount = dueAmount;
    }

    public BigDecimal getDefaultDiscount() {
        return defaultDiscount;
    }

    public void setDefaultDiscount(BigDecimal defaultDiscount) {
        this.defaultDiscount = defaultDiscount;
    }

    public BigDecimal getExtraDiscount() {
        return extraDiscount;
    }

    public void setExtraDiscount(BigDecimal extraDiscount) {
        this.extraDiscount = extraDiscount;
    }

    public BigDecimal getFineAmount() {
        return fineAmount;
    }

    public void setFineAmount(BigDecimal fineAmount) {
        this.fineAmount = fineAmount;
    }

    public BigDecimal getFinalAmount() {
        return finalAmount;
    }

    public void setFinalAmount(BigDecimal finalAmount) {
        this.finalAmount = finalAmount;
    }

    public BigDecimal getPaidAmount() {
        return paidAmount;
    }

    public void setPaidAmount(BigDecimal paidAmount) {
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
