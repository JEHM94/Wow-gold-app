package com.jehm.wowrandomapp.models;

public class AccessToken {


    private String access_token, token_type;
    private int expires_in;

    //("fa75ca71e373400b9ed4c4c8c8c8c5b4", "xUEWjNdGp2ERMt6UI3UDlIvc7rwPCf7J",
    ////                "client_credentials");
    public AccessToken() {
    }

    public AccessToken(String access_token, String token_type, int expires_in) {
        this.access_token = access_token;
        this.token_type = token_type;
        this.expires_in = expires_in;
    }

    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    public String getToken_type() {
        return token_type;
    }

    public void setToken_type(String token_type) {
        this.token_type = token_type;
    }

    public int getExpires_in() {
        return expires_in;
    }

    public void setExpires_in(int expires_in) {
        this.expires_in = expires_in;
    }
}
