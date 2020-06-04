package com.example.chat_app.models;

import androidx.annotation.NonNull;

public class Message {
    private String name;
    private String message;
    private Object timestamp;
    private String sender;
    private String receiver;

    public Message() {
    }

    public Message(String sender, String receiver, String name, String message, Object timestamp) {
        this.sender = sender;
        this.receiver = receiver;
        this.name = name;
        this.message = message;
        this.timestamp = timestamp;
    }

    public String getSender() { return sender; }

    public String getReceiver() { return receiver; }

    public String getName() {
        return name;
    }

    public String getMessage() {
        return message;
    }

    public Object getTimestamp() {
        return timestamp;
    }

    @NonNull
    @Override
    public String toString() {
        return timestamp.toString();
    }
}
