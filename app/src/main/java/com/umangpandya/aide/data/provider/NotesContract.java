package com.umangpandya.aide.data.provider;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by umang on 19/11/16.
 */

public class NotesContract {
    public static final String CONTENT_AUTHORITY = "com.umangpandya.aide";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_NOTE = "note";

    public static final class NoteEntry implements BaseColumns {

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_NOTE).build();

        public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_NOTE;
//        public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_TXN;

        public static final String TABLE_NAME = "note";

        public static final String COLUMN_KEY = "key";
        public static final String COLUMN_NOTE_TEXT = "note_text";
        public static final String COLUMN_TIMESTAMP = "time_stamp";


        public static Uri buildOneNoteUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

        public static String getNoteIdFromUri(Uri uri) {
            return uri.getPathSegments().get(1);
        }
    }

}