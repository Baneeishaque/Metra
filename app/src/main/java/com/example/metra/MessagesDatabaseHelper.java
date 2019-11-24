package com.example.metra;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MessagesDatabaseHelper extends SQLiteOpenHelper {

    // Database Version
    private static final int DATABASE_VERSION = 1;
    // Database Name
    private static final String DATABASE_NAME = ApplicationDetails.applicationName.toLowerCase() + "_db";

    public MessagesDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {

        // create trusted_sources table
        db.execSQL(Message.CREATE_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + Message.TABLE_NAME);

        // Create tables again
        onCreate(db);
    }

    void insertMessage(String sender, String messageBody) {

        // get writable database as we want to write data
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        // `id` will be inserted automatically.
        // no need to add it.
        values.put(Message.COLUMN_SENDER, sender);
        values.put(Message.COLUMN_MESSAGE_BODY, messageBody);

        // insert row
        db.insert(Message.TABLE_NAME, null, values);

        // close db connection
        db.close();
    }

    public String checkMessage(String passedMessage) {

        String result = "";

        // get readable database as we are not inserting anything
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(Message.TABLE_NAME,
                new String[]{Message.COLUMN_ID, Message.COLUMN_SENDER, Message.COLUMN_MESSAGE_BODY},
                Message.COLUMN_MESSAGE_BODY + "=?",
                new String[]{passedMessage}, null, null, null, null);

        if (cursor.moveToFirst()) {
            result = cursor.getString(cursor.getColumnIndex(Message.COLUMN_SENDER));
        }
        // close the db connection
        cursor.close();

        return result;
    }
}
