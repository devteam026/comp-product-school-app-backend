package com.company.schoolbackend.dto;

public class LeaveCategoryRequest {
    private String name;
    private String role;
    private Integer maxDays;
    private Boolean active;

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
    public Integer getMaxDays() { return maxDays; }
    public void setMaxDays(Integer maxDays) { this.maxDays = maxDays; }
    public Boolean getActive() { return active; }
    public void setActive(Boolean active) { this.active = active; }
}
