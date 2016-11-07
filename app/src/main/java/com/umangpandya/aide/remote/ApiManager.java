package com.umangpandya.aide.remote;

import android.app.Activity;
import android.content.Context;

import com.google.gson.Gson;
import com.umangpandya.aide.R;
import com.umangpandya.aide.data.storage.AccountManager;
import com.umangpandya.aide.model.local.UserProfile;
import com.umangpandya.aide.model.remote.request.RequestLogin;
import com.umangpandya.aide.model.remote.response.ResponseBase;
import com.umangpandya.aide.utility.Debug;
import com.umangpandya.aide.utility.Utility;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by umang on 07/11/16.
 */

public class ApiManager {
    private static final String TAG = ApiManager.class.getSimpleName();

    protected static String getAuthToken(Context context) {
        UserProfile merchant = AccountManager.getUserData(context);
        return merchant.getServerAuthCode();
    }

    private static String returnAppropriateMessage(Context context, String message) {
        if (Utility.isInternetConnectionAvailable(context)) {
            return context.getString(R.string.unable_to_connect_server);
        } else {
            return context.getString(R.string.no_internet_connection_available);
        }
    }

    private static String toTitleCase(String givenString) {
        String[] arr = givenString.split(" ");
        StringBuilder sb = new StringBuilder();
        for (String anArr : arr) {
            sb.append(Character.toUpperCase(anArr.charAt(0))).append(anArr.substring(1)).append(" ");
        }
        return sb.toString().trim();
    }

    private static void printRequest(String API, Object param) {
        Gson gson = new Gson();
        Debug.e(TAG, API + "- request", gson.toJson(param));
    }

    private static void printResponse(String API, Object param) {
        Gson gson = new Gson();
        Debug.e(TAG, API + "- response", gson.toJson(param));
    }

    public static void userSignIn(final Activity activity, RequestLogin param, final ApiListeners.SignInListener listener) {
        printRequest("login", param);
        ApiService.buildService().userSignIn(param).enqueue(new Callback<ResponseBase>() {
            @Override
            public void onResponse(Call<ResponseBase> call, Response<ResponseBase> response) {
                printResponse("login", response.body());
                if (response.body() == null) {
                    listener.done(response, true, returnAppropriateMessage(activity, activity.getString(R.string.unable_to_connect_server)));
                } else {
                    listener.done(response,
                            !Utility.isSuccessful(response.body().getCode()),
                            response.message());
                }
            }

            @Override
            public void onFailure(Call<ResponseBase> call, Throwable t) {
                printResponse("login", t);
                listener.done(null, true, returnAppropriateMessage(activity, t.getMessage()));
            }
        });
    }

}