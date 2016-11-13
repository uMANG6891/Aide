package com.umangpandya.aide.model.local;

/**
 * Created by umang on 13/11/16.
 */

public class Notes {
    private int checked;
    private String note;
    private long timestamp;

    public Notes() {
    }

    public void setChecked(int checked) {
        this.checked = checked;
    }

    public int getChecked() {
        return checked;
    }

    public String getNote() {
        return note;
    }

    public long getTimestamp() {
        return timestamp;
    }
}
