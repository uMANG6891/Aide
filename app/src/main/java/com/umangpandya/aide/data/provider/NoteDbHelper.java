package com.umangpandya.aide.data.provider;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.umangpandya.aide.data.provider.NotesContract.NoteEntry;

/**
 * Created by umang on 19/11/16.
 */

public class NoteDbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "transaction.db";
    private static final int DATABASE_VERSION = 1;

    public NoteDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String SQL_CREATE_NOTE_TABLE = "CREATE TABLE " + NoteEntry.TABLE_NAME + " ("
                + NoteEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + NoteEntry.COLUMN_KEY + " TEXT NOT NULL, "
                + NoteEntry.COLUMN_NOTE_TEXT + " TEXT NOT NULL, "
                + NoteEntry.COLUMN_CHECKED + " INTEGER NOT NULL, "
                + NoteEntry.COLUMN_TIMESTAMP + " INTEGER NOT NULL);";

        db.execSQL(SQL_CREATE_NOTE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + NoteEntry.TABLE_NAME);
        onCreate(db);
    }
}