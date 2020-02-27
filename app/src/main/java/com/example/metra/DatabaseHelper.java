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

        db.execSQL(CommunicationOrigin.CREATE_TABLE);
        db.execSQL(Message.CREATE_TABLE);
        db.execSQL(UnTrustedApp.CREATE_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + CommunicationOrigin.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + Message.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + UnTrustedApp.TABLE_NAME);

        // Create tables again
        onCreate(db);
    }

    void insertCommunicationOrigin(String name, String phoneNumber) {

        // get writable database as we want to write data
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        // `id` will be inserted automatically - no need to add it.
        values.put(CommunicationOrigin.COLUMN_NAME, name);
        values.put(CommunicationOrigin.COLUMN_PHONE_NUMBER, phoneNumber);

        // insert row
        long id = db.insert(CommunicationOrigin.TABLE_NAME, null, values);

        // close db connection
        db.close();
    }

    ArrayList<CommunicationOrigin> getAllCommunicationOrigins() {

        ArrayList<CommunicationOrigin> communicationOrigins = new ArrayList<>();

        // Select All Query
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery("SELECT  * FROM " + CommunicationOrigin.TABLE_NAME, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {

            do {

                communicationOrigins.add(new CommunicationOrigin(cursor.getInt(cursor.getColumnIndex(CommunicationOrigin.COLUMN_ID)), cursor.getString(cursor.getColumnIndex(CommunicationOrigin.COLUMN_NAME)), cursor.getString(cursor.getColumnIndex(CommunicationOrigin.COLUMN_PHONE_NUMBER))));

            } while (cursor.moveToNext());
        }

        cursor.close();

        // close db connection
        db.close();

        // return trusted sources list
        return communicationOrigins;
    }

    boolean checkSmsSender(String phoneNumber) {

        List<CommunicationOrigin> communicationOrigins = getAllCommunicationOrigins();

        for (CommunicationOrigin communicationOrigin : communicationOrigins) {

            if (communicationOrigin.getPhoneNumber().equals(phoneNumber)) {

                return true;
            }
        }
        return false;
    }

    void deleteCommunicationOriginById(CommunicationOrigin communicationOrigin) {

        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(CommunicationOrigin.TABLE_NAME, CommunicationOrigin.COLUMN_ID + " = ?", new String[]{String.valueOf(communicationOrigin.getId())});

        LogUtils.debug("Trusted Source Deleted From DB...");

        db.close();
    }

    void deleteCommunicationOriginByName(CommunicationOrigin communicationOrigin) {

        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(CommunicationOrigin.TABLE_NAME, CommunicationOrigin.COLUMN_NAME + " = ?", new String[]{String.valueOf(communicationOrigin.getName())});

        LogUtils.debug("Trusted Source Deleted From DB...");

        db.close();
    }

    void deleteCommunicationOriginByPhoneNumber(CommunicationOrigin communicationOrigin) {

        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(CommunicationOrigin.TABLE_NAME, CommunicationOrigin.COLUMN_PHONE_NUMBER + " = ?", new String[]{String.valueOf(communicationOrigin.getPhoneNumber())});

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

    void insertTrustedApp(String app_name) {

        // get writable database as we want to write data
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        // `id` will be inserted automatically - no need to add it.
        values.put(UnTrustedApp.COLUMN_APP_NAME, app_name);

        // insert row
        long id = db.insert(UnTrustedApp.TABLE_NAME, null, values);

        // close db connection
        db.close();
    }

    ArrayList<UnTrustedApp> getAllTrustedApps() {

        ArrayList<UnTrustedApp> trusted_apps = new ArrayList<>();

        try {
            // Select All Query
            SQLiteDatabase db = this.getWritableDatabase();

            Cursor cursor = db.rawQuery("SELECT  * FROM " + UnTrustedApp.TABLE_NAME, null);

            // looping through all rows and adding to list
            if (cursor.moveToFirst()) {

                do {

                    trusted_apps.add(new UnTrustedApp(cursor.getInt(cursor.getColumnIndex(UnTrustedApp.COLUMN_ID)), cursor.getString(cursor.getColumnIndex(UnTrustedApp.COLUMN_APP_NAME))));

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

        List<UnTrustedApp> trusted_apps = getAllTrustedApps();

        for (UnTrustedApp unTrustedApp : trusted_apps) {

            if (unTrustedApp.getApp_name().equals(app_name)) {

                return true;
            }
        }
        return false;
    }

    void deleteTrustedAppById(UnTrustedApp unTrustedApp) {

        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(UnTrustedApp.TABLE_NAME, UnTrustedApp.COLUMN_ID + " = ?", new String[]{String.valueOf(unTrustedApp.getId())});

        LogUtils.debug("Trusted App Deleted From DB...");

        db.close();
    }

    void deleteTrustedAppByName(String trustedApp) {

        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(UnTrustedApp.TABLE_NAME, UnTrustedApp.COLUMN_APP_NAME + " = ?", new String[]{trustedApp});

        LogUtils.debug("Trusted App Deleted From DB...");

        db.close();
    }
}
