package com.example.metra;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class TrustedSourcesDatabaseHelper extends SQLiteOpenHelper {

    // Database Version
    private static final int DATABASE_VERSION = 1;
    // Database Name
    private static final String DATABASE_NAME = ApplicationDetails.applicationName.toLowerCase() + "_db";

    public TrustedSourcesDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {

        // create trusted_sources table
        db.execSQL(TrustedSource.CREATE_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TrustedSource.TABLE_NAME);

        // Create tables again
        onCreate(db);
    }

    void insertTrustedSource(String phone_number) {
        // get writable database as we want to write data
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        // `id` will be inserted automatically.
        // no need to add it.
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
        db.delete(TrustedSource.TABLE_NAME, TrustedSource.COLUMN_ID + " = ?",
                new String[]{String.valueOf(trustedSource.getId())});
        LogUtils.debug("Trusted Source Deleted From DB...");
        db.close();
    }
}
