package com.example.common;

public class TrustedApp {

    static final String TABLE_NAME = "untrusted_apps";

    static final String COLUMN_ID = "id";
    static final String COLUMN_APP_NAME = "app_name";
    // Create table SQL query
    public static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + "("
                    + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + COLUMN_APP_NAME + " TEXT"
                    + ")";
    private int id;
    private String app_name;

    public TrustedApp(int id, String app_name) {
        this.id = id;
        this.app_name = app_name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getApp_name() {
        return app_name;
    }

    public void setApp_name(String app_name) {
        this.app_name = app_name;
    }
}
