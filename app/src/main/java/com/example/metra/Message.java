package com.example.metra;

public class Message {

    static final String TABLE_NAME = "messages";

    static final String COLUMN_ID = "id";
    static final String COLUMN_SENDER = "sender";
    static final String COLUMN_MESSAGE_BODY = "messageBody";
    // Create table SQL query
    public static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + "("
                    + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + COLUMN_SENDER + " TEXT,"
                    + COLUMN_MESSAGE_BODY + " TEXT,"
                    + ")";
    private int id;
    private String sender, messageBody;

    public Message(int id, String sender, String messageBody) {
        this.id = id;
        this.sender = sender;
        this.messageBody = messageBody;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getMessageBody() {
        return messageBody;
    }

    public void setMessageBody(String messageBody) {
        this.messageBody = messageBody;
    }
}
