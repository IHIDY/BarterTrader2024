package com.example.group8_bartertrader.model;

public class Message {
    private String senderId;
    private String content;
    private long timestamp;

    public Message() {
    }

    public Message(String senderId, String content, long timestamp) {
        this.senderId = senderId;
        this.content = content;
        this.timestamp = timestamp;
    }

    public String getSenderId() {
        return senderId;
    }

    public String getContent() {
        return content;
    }

    public long getTimestamp() {
        return timestamp;
    }
}
