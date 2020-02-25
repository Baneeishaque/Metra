package com.example.metra;

public class AbstractModel {

    private String address;
    private String message;

    public AbstractModel(String address, String message) {

        this.address = address;
        this.message = message;
    }

    public AbstractModel() {
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
