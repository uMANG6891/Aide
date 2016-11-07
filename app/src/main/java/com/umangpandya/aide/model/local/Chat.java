package com.umangpandya.aide.model.local;

/**
 * Created by umang on 07/11/16.
 */

public class Chat {
    private String from;
    private int messageType;
    private String body;
    private int status;
    private long timestamp;

    public Chat() {
    }

    public Chat(String from, int messageType, String body, int status, long timestamp) {
        this.from = from;
        this.messageType = messageType;
        this.body = body;
        this.status = status;
        this.timestamp = timestamp;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public int getMessageType() {
        return messageType;
    }

    public void setMessageType(int messageType) {
        this.messageType = messageType;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
