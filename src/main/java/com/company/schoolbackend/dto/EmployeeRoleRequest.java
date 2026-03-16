package com.company.schoolbackend.dto;

public class EmployeeRoleRequest {
    private String role;
    private String password;
    private Boolean updatePassword;
    private Boolean active;

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    public Boolean getUpdatePassword() {
        return updatePassword;
    }

    public void setUpdatePassword(Boolean updatePassword) {
        this.updatePassword = updatePassword;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

}
