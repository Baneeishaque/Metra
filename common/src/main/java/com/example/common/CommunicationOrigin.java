package com.example.common;

public class CommunicationOrigin {

    static final String TABLE_NAME = "trusted_sources";

    static final String COLUMN_ID = "id";
    static final String COLUMN_NAME = "name";
    static final String COLUMN_PHONE_NUMBER = "phoneNumber";

    // Create table SQL query
    public static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + "("
                    + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + COLUMN_NAME + " TEXT,"
                    + COLUMN_PHONE_NUMBER + " TEXT"
                    + ")";

    private int id;
    private String name;
    private String phoneNumber;

    public CommunicationOrigin(int id, String name, String phoneNumber) {
        this.id = id;
        this.name = name;
        this.phoneNumber = phoneNumber;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
