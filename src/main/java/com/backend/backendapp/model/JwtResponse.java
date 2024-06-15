package com.backend.backendapp.model;

import lombok.Builder;

@Builder
public class JwtResponse {
    private String JwtToken;
    private String username;
    private String role;

    public JwtResponse(String JwtToken, String username, String role) {
        this.JwtToken = JwtToken;
        this.username = username;
        this.role = role;
    }

    public String getJwtToken() {
        return JwtToken;
    }
    public void setJwtToken(String jwtToken) {
        JwtToken = jwtToken;
    }
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public String getRole() {
        return role;
    }
    public void setRole(String role) {
        this.role = role;
    }
}
