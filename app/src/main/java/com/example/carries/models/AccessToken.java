package com.example.carries.models;

public class AccessToken {


    private String token, tokenType, scope;
    private int expiresIn;

    //("fa75ca71e373400b9ed4c4c8c8c8c5b4", "xUEWjNdGp2ERMt6UI3UDlIvc7rwPCf7J",
    ////                "client_credentials");
    public AccessToken() {
    }
    public AccessToken(String token, String tokenType, String scope, int expiresIn) {
        this.token = token;
        this.tokenType = tokenType;
        this.scope = scope;
        this.expiresIn = expiresIn;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getTokenType() {
        return tokenType;
    }

    public void setTokenType(String tokenType) {
        this.tokenType = tokenType;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public int getExpiresIn() {
        return expiresIn;
    }

    public void setExpiresIn(int expiresIn) {
        this.expiresIn = expiresIn;
    }
}
