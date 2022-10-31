package com.evoila.springsecuritytask.model;

public class AuthResponse {

    private String username;
    private String accessToken;
    private String email;


    public AuthResponse() { }


    public AuthResponse(String username, String email, String accessToken) {
        this.username = username;
        this.email = email;
        this.accessToken = accessToken;
    }


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }


}
