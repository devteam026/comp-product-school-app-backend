package com.company.schoolbackend.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "leave_categories")
public class LeaveCategory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String role;

    @Column(name = "max_days", nullable = false)
    private Integer maxDays;

    @Column(name = "period_type")
    private String periodType;

    @Column(name = "max_per_period")
    private Integer maxPerPeriod;

    @Column(nullable = false)
    private boolean active = true;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
    public Integer getMaxDays() { return maxDays; }
    public void setMaxDays(Integer maxDays) { this.maxDays = maxDays; }
    public String getPeriodType() { return periodType; }
    public void setPeriodType(String periodType) { this.periodType = periodType; }
    public Integer getMaxPerPeriod() { return maxPerPeriod; }
    public void setMaxPerPeriod(Integer maxPerPeriod) { this.maxPerPeriod = maxPerPeriod; }
    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }
}
