package com.example.wowrandomapp.models;

public class AuthorizeCode {

    private String code, state;

    public AuthorizeCode(String code, String state) {
        this.code = code;
        this.state = state;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}
