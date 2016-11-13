package com.umangpandya.aide.model.local;

/**
 * Created by umang on 07/11/16.
 */

public class Chat {
    private String from;
    private String action = "display";
    private int message_type;
    private String body;
    private int status;
    private long timestamp;

    public Chat() {
    }

    public Chat(String from, int message_type, String body, int status, long timestamp) {
        this.from = from;
        this.message_type = message_type;
        this.body = body;
        this.status = status;
        this.timestamp = timestamp;
    }

    public String getFrom() {
        return from;
    }

    public String getAction() {
        return action;
    }

    public int getMessage_type() {
        return message_type;
    }

    public String getBody() {
        return body;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getStatus() {
        return status;
    }

    public long getTimestamp() {
        return timestamp;
    }
}
