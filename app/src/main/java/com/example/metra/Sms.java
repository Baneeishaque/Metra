package com.example.metra;

public class Sms {

    private String address;
    private String message;

    public Sms(String address, String message) {

        this.address = address;
        this.message = message;
    }

    public Sms() {
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
