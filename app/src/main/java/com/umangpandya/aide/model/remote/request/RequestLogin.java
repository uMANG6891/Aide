package com.umangpandya.aide.model.remote.request;

/**
 * Created by umang on 07/11/16.
 */

public class RequestLogin {
    private final String user_id;
    private final String name;
    private final String email;
    private final int os = 1; // For Android
    private final String profile_picture;

    public RequestLogin(String user_id, String name, String email, String profile_picture) {
        this.user_id = user_id;
        this.name = name;
        this.email = email;
        this.profile_picture = profile_picture;
    }
}
