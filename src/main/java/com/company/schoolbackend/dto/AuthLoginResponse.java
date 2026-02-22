package com.company.schoolbackend.dto;

public class AuthLoginResponse {
    private boolean ok;
    private String token;
    private UserProfile user;

    public AuthLoginResponse() {}

    public AuthLoginResponse(boolean ok, String token, UserProfile user) {
        this.ok = ok;
        this.token = token;
        this.user = user;
    }

    public boolean isOk() {
        return ok;
    }

    public void setOk(boolean ok) {
        this.ok = ok;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public UserProfile getUser() {
        return user;
    }

    public void setUser(UserProfile user) {
        this.user = user;
    }
}
