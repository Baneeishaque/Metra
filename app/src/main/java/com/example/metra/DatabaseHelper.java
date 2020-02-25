package com.example.metra;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    // Database Version
    private static final int DATABASE_VERSION = 1;
    // Database Name
    private static final String DATABASE_NAME = ApplicationDetails.applicationName.toLowerCase() + "_db";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {

        // create trusted_sources table
        db.execSQL(TrustedSource.CREATE_TABLE);
        db.execSQL(Message.CREATE_TABLE);
        db.execSQL(TrustedApp.CREATE_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TrustedSource.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + Message.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + TrustedApp.TABLE_NAME);

        // Create tables again
        onCreate(db);
    }

    void insertTrustedSource(String phone_number) {

        // get writable database as we want to write data
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        // `id` will be inserted automatically - no need to add it.
        values.put(TrustedSource.COLUMN_PHONE_NUMBER, phone_number);

        // insert row
        long id = db.insert(TrustedSource.TABLE_NAME, null, values);

        // close db connection
        db.close();
    }

    ArrayList<TrustedSource> getAllTrustedSources() {

        ArrayList<TrustedSource> trusted_sources = new ArrayList<>();

        // Select All Query
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery("SELECT  * FROM " + TrustedSource.TABLE_NAME, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {

            do {

                trusted_sources.add(new TrustedSource(cursor.getInt(cursor.getColumnIndex(TrustedSource.COLUMN_ID)), cursor.getString(cursor.getColumnIndex(TrustedSource.COLUMN_PHONE_NUMBER))));

            } while (cursor.moveToNext());
        }

        cursor.close();

        // close db connection
        db.close();

        // return trusted sources list
        return trusted_sources;
    }

    ArrayList<Sms> getAllMessages() {

        ArrayList<Sms> messages = new ArrayList<>();

        // Select All Query
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery("SELECT  * FROM " + Message.TABLE_NAME, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {

            do {

                messages.add(new Sms(cursor.getString(cursor.getColumnIndex(Message.COLUMN_SENDER)), cursor.getString(cursor.getColumnIndex(Message.COLUMN_MESSAGE_BODY))));

            } while (cursor.moveToNext());
        }

        cursor.close();

        // close db connection
        db.close();

        // return trusted sources list
        return messages;
    }

    boolean checkSmsSender(String phoneNumber) {

        List<TrustedSource> trusted_sources = getAllTrustedSources();

        for (TrustedSource trustedSource : trusted_sources) {

            if (trustedSource.getPhone_number().equals(phoneNumber)) {

                return true;
            }
        }
        return false;
    }

    void deleteTrustedSource(TrustedSource trustedSource) {

        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(TrustedSource.TABLE_NAME, TrustedSource.COLUMN_ID + " = ?", new String[]{String.valueOf(trustedSource.getId())});

        LogUtils.debug("Trusted Source Deleted From DB...");

        db.close();
    }

    void insertMessage(String sender, String messageBody) {

        // get writable database as we want to write data
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        // `id` will be inserted automatically - no need to add it.
        values.put(Message.COLUMN_SENDER, sender);
        values.put(Message.COLUMN_MESSAGE_BODY, messageBody);

        // insert row
        long id = db.insert(Message.TABLE_NAME, null, values);

        // close db connection
        db.close();
    }

    public String checkMessage(String passedMessage) {

        String result = "";

        // get readable database as we are not inserting anything
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(Message.TABLE_NAME,
                new String[]{Message.COLUMN_ID, Message.COLUMN_SENDER, Message.COLUMN_MESSAGE_BODY}, Message.COLUMN_MESSAGE_BODY + "=?", new String[]{passedMessage}, null, null, null, null);

        if (cursor.moveToFirst()) {

            result = cursor.getString(cursor.getColumnIndex(Message.COLUMN_SENDER));
        }

        // close the db connection
        cursor.close();

        return result;
    }

    void insertTrustedApp(String app_name) {

        // get writable database as we want to write data
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        // `id` will be inserted automatically - no need to add it.
        values.put(TrustedApp.COLUMN_APP_NAME, app_name);

        // insert row
        long id = db.insert(TrustedApp.TABLE_NAME, null, values);

        // close db connection
        db.close();
    }

    ArrayList<TrustedApp> getAllTrustedApps() {

        ArrayList<TrustedApp> trusted_apps = new ArrayList<>();

        try {
            // Select All Query
            SQLiteDatabase db = this.getWritableDatabase();

            Cursor cursor = db.rawQuery("SELECT  * FROM " + TrustedApp.TABLE_NAME, null);

            // looping through all rows and adding to list
            if (cursor.moveToFirst()) {

                do {

                    trusted_apps.add(new TrustedApp(cursor.getInt(cursor.getColumnIndex(TrustedApp.COLUMN_ID)), cursor.getString(cursor.getColumnIndex(TrustedApp.COLUMN_APP_NAME))));

                } while (cursor.moveToNext());
            }

            cursor.close();

            // close db connection
            db.close();

        } catch (Exception e) {

            LogUtils.debug(e.getLocalizedMessage());
        }

        // return trusted apps list
        return trusted_apps;
    }

    boolean checkTrustedApp(String app_name) {

        List<TrustedApp> trusted_apps = getAllTrustedApps();

        for (TrustedApp trustedApp : trusted_apps) {

            if (trustedApp.getApp_name().equals(app_name)) {

                return true;
            }
        }
        return false;
    }

    void deleteTrustedApp(TrustedApp trustedApp) {

        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(TrustedApp.TABLE_NAME, TrustedApp.COLUMN_ID + " = ?", new String[]{String.valueOf(trustedApp.getId())});

        LogUtils.debug("Trusted App Deleted From DB...");

        db.close();
    }
}
