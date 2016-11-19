package com.umangpandya.aide.utility;

import android.content.Context;
import android.support.annotation.IntDef;
import android.support.annotation.StringDef;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.umangpandya.aide.data.provider.NotesContract.NoteEntry;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by umang on 07/11/16.
 */

public class Constants {
    public static final String BASE_URL = "http://54.218.51.166:8080/";
    public static final String MESSAGE_SENDER_TYPE_AIDE = "1";

    public static DatabaseReference getFirebaseMessageUrl(Context context, String userId) {
        return FirebaseDatabase.getInstance().getReference()
                .child("threads")
                .child(userId)
                .child("messages");
    }

    public static DatabaseReference getFirebaseNotesUrl(Context context, String userId) {
        return FirebaseDatabase.getInstance().getReference()
                .child("list")
                .child(userId)
                .child("notes");
    }

    public class ResponseCode {
        public static final int SUCCESS = 0;
        public static final int REQUIRED_PARAMETERS_MISSING = 902;
    }

    @Retention(RetentionPolicy.SOURCE)
    @StringDef({ChatActionType.DISPLAY_TEXT,
            ChatActionType.NOTE_CREATE,
            ChatActionType.NOTE_SHOW})
    public @interface ChatActionType {
        String DISPLAY_TEXT = "display.text";
        String NOTE_CREATE = "note.create";
        String NOTE_SHOW = "note.show";
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

    public static final String[] NOTE_PROJECTION_COLS = {
            NoteEntry.TABLE_NAME + "." + NoteEntry._ID,
            NoteEntry.TABLE_NAME + "." + NoteEntry.COLUMN_KEY,
            NoteEntry.TABLE_NAME + "." + NoteEntry.COLUMN_NOTE_TEXT,
            NoteEntry.TABLE_NAME + "." + NoteEntry.COLUMN_CHECKED,
            NoteEntry.TABLE_NAME + "." + NoteEntry.COLUMN_TIMESTAMP,
    };
    public static final int COL_NOTE_ID = 0;
    public static final int COL_NOTE_KEY = 1;
    public static final int COL_NOTE_TEXT = 2;
    public static final int COL_NOTE_CHECKED = 3;
    public static final int COL_NOTE_TIMESTAMP = 4;

}
