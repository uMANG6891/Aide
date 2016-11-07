package com.umangpandya.aide.utility;

import android.content.Context;
import android.support.annotation.IntDef;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by umang on 07/11/16.
 */

public class Constants {
    public static final String BASE_URL = "http://10.0.2.2:8080/";
    public static final String MESSAGE_SENDER_TYPE_AIDE = "1";

    public static DatabaseReference getFirebaseMessageUrl(Context context, String userId) {
        return FirebaseDatabase.getInstance().getReference()
                .child("threads")
                .child(userId)
                .child("messages");
    }

    public class ResponseCode {
        public static final int SUCCESS = 0;
        public static final int REQUIRED_PARAMETERS_MISSING = 902;
    }

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({MessageType.TEXT,
            MessageType.IMAGE,
            MessageType.LOCATION})
    public @interface MessageType {
        int TEXT = 0;
        int IMAGE = 1;
        int LOCATION = 2;
    }

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({SenderType.USER,
            SenderType.AIDE})
    public @interface SenderType {
        int USER = 0;
        int AIDE = 1;
    }

}
