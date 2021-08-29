package com.example.istv_andorid.data.model;

public class LoggedInUser {
    private String token;
    private String username;

    public String getToken() {
        return token;
    }

    public String getUsername() {
        return username;
    }

    public LoggedInUser(String token, String username) {
        this.token = token;
        this.username = username;
    }

}
