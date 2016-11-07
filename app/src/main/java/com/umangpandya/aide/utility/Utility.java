package com.umangpandya.aide.utility;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by umang on 07/11/16.
 */

public class Utility {
    private static final String TAG = Utility.class.getSimpleName();

    public static boolean isInternetConnectionAvailable(Context context) {
        try {
            ConnectivityManager connectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            return networkInfo != null && networkInfo.isAvailable() &&
                    networkInfo.isConnected();
        } catch (Exception e) {
            Debug.e(TAG, "CheckConnectivity Exception: ", e.getMessage());
            Debug.e(TAG, "connectivity", e.toString());
        }
        return false;
    }

    public static boolean isSuccessful(int code) {
        return code == 0;
    }

}