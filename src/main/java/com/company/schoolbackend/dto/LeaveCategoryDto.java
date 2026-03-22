package com.company.schoolbackend.dto;

public class LeaveCategoryDto {
    private Long id;
    private String name;
    private String role;
    private Integer maxDays;
    private boolean active;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
    public Integer getMaxDays() { return maxDays; }
    public void setMaxDays(Integer maxDays) { this.maxDays = maxDays; }
    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }
}
