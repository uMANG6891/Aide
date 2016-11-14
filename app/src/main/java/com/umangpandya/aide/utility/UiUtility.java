package com.umangpandya.aide.utility;

import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.view.inputmethod.InputMethodManager;

import com.umangpandya.aide.ui.activity.NotesActivity;

/**
 * Created by umang on 13/11/16.
 */

public class UiUtility {

    public static void startNotesActivity(Context context) {
        Intent i = new Intent(context, NotesActivity.class);
        context.startActivity(i);
    }

    public static void closeKeyboard(Context context, IBinder iBinder) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(iBinder, 0);
    }

}
