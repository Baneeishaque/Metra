package com.example.metra;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class TrustedAppsDatabaseHelper extends SQLiteOpenHelper {

    // Database Version
    private static final int DATABASE_VERSION = 1;
    // Database Name
    private static final String DATABASE_NAME = ApplicationDetails.applicationName.toLowerCase() + "_db";

    public TrustedAppsDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {

        // create trusted_apps table
        db.execSQL(TrustedApp.CREATE_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TrustedApp.TABLE_NAME);

        // Create tables again
        onCreate(db);
    }

    void insertTrustedApp(String app_name) {
        // get writable database as we want to write data
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        // `id` will be inserted automatically.
        // no need to add it.
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

    boolean checkSmsSender(String app_name) {
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
        db.delete(TrustedApp.TABLE_NAME, TrustedApp.COLUMN_ID + " = ?",
                new String[]{String.valueOf(trustedApp.getId())});
        LogUtils.debug("Trusted App Deleted From DB...");
        db.close();
    }
}
