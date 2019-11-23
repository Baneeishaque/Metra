package com.example.metra;

public class TrustedSource {

    static final String TABLE_NAME = "trusted_sources";

    static final String COLUMN_ID = "id";
    static final String COLUMN_PHONE_NUMBER = "phone_number";
    // Create table SQL query
    public static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + "("
                    + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + COLUMN_PHONE_NUMBER + " TEXT"
                    + ")";
    private int id;
    private String phone_number;

    public TrustedSource(int id, String phone_number) {
        this.id = id;
        this.phone_number = phone_number;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }
}
