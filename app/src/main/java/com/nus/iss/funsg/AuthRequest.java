package com.nus.iss.funsg;

public class AuthRequest {
    //this class is use for login validation


    private String username;
    private String password;
    public AuthRequest(){}
    public AuthRequest(String username, String password) {
        this.username = username;
        this.password = password;
    }
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
