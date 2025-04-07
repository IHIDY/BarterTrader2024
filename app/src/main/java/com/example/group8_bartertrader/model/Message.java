package com.example.group8_bartertrader.model;

public class Message {
    private String senderId;
    private String content;
    private long timestamp;

    public Message() {
    }

    /**
     * constructor for message
     * @param senderId
     * @param content
     * @param timestamp
     */
    public Message(String senderId, String content, long timestamp) {
        this.senderId = senderId;
        this.content = content;
        this.timestamp = timestamp;
    }

    /**
     * id getter
     * @return
     */
    public String getSenderId() {
        return senderId;
    }

    /**
     * content getter
     * @return
     */
    public String getContent() {
        return content;
    }

    /**
     * timestamp getter
     * @return
     */
    public long getTimestamp() {
        return timestamp;
    }
}
