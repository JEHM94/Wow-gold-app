package com.jehm.wowrandomapp.models;

public class WowToken {
    private String price;

    public WowToken(String price) {
        this.price = price;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}
