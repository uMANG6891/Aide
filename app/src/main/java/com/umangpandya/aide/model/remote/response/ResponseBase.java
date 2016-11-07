package com.umangpandya.aide.model.remote.response;

import com.google.gson.annotations.SerializedName;

/**
 * Created by umang on 07/11/16.
 */

public class ResponseBase {
    @SerializedName("code") private int code;
    @SerializedName("message") private String message;

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
