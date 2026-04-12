package com.company.schoolbackend.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.math.BigDecimal;

@Entity
@Table(name = "fine_rules")
public class FineRule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "days_from", nullable = false)
    private int daysFrom;

    @Column(name = "days_to", nullable = false)
    private int daysTo;

    @Enumerated(EnumType.STRING)
    @Column(name = "fine_type", nullable = false, length = 20)
    private FineType fineType;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal value;

    @Column(nullable = false)
    private boolean active = true;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getDaysFrom() {
        return daysFrom;
    }

    public void setDaysFrom(int daysFrom) {
        this.daysFrom = daysFrom;
    }

    public int getDaysTo() {
        return daysTo;
    }

    public void setDaysTo(int daysTo) {
        this.daysTo = daysTo;
    }

    public FineType getFineType() {
        return fineType;
    }

    public void setFineType(FineType fineType) {
        this.fineType = fineType;
    }

    public BigDecimal getValue() {
        return value;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
