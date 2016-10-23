package com.umangpandya.aide.utility;

import android.util.Log;

import com.umangpandya.aide.BuildConfig;

/**
 * Created by umang on 22/10/16.
 */

public class Debug {
    public static void e(String className, String tag, String message) {
        if (BuildConfig.DEBUG) {
            Log.e(className, tag + ": " + message);
        }
    }

    public static void e(String className, String tag, long message) {
        if (BuildConfig.DEBUG) {
            Log.e(className, tag + ": " + message);
        }
    }

    public static void e(String className, String tag, boolean message) {
        if (BuildConfig.DEBUG) {
            Log.e(className, tag + ": " + message);
        }
    }

}
