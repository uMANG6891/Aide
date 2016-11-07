package com.umangpandya.aide.data.storage;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.gson.Gson;
import com.umangpandya.aide.model.local.UserProfile;

/**
 * Created by umang on 22/10/16.
 */

public class AccountManager {
    private static String SP_USER_DATA = "user_data";
    private static String SP_REGISTERED_ON_SERVER = "registered_on_server";

    private static SharedPreferences createSharedPreferenceObject(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context);
    }

    public static void saveUserData(Context context, GoogleSignInAccount account) {
        Gson gson = new Gson();
        UserProfile userProfile = new UserProfile();
        userProfile.setId(account.getId());
        userProfile.setDisplayName(account.getDisplayName());
        userProfile.setEmail(account.getEmail());
        userProfile.setFamilyName(account.getFamilyName());
        userProfile.setGivenName(account.getGivenName());
        userProfile.setGrantedScopes(account.getGrantedScopes());
        userProfile.setPhotoUrl(
                account.getPhotoUrl() == null
                        ? null
                        : account.getPhotoUrl().toString()
        );
        userProfile.setServerAuthCode(account.getServerAuthCode());
        userProfile.setIdToken(account.getIdToken());
        createSharedPreferenceObject(context).edit().putString(SP_USER_DATA, gson.toJson(userProfile)).apply();
    }

    public static UserProfile getUserData(Context context) {
        Gson gson = new Gson();
        String data = createSharedPreferenceObject(context).getString(SP_USER_DATA, null);
        return data != null ? gson.fromJson(data, UserProfile.class) : null;
    }

    public static void saveHasRegisteredOnServer(Context context) {
        createSharedPreferenceObject(context).edit().putBoolean(SP_REGISTERED_ON_SERVER, true).apply();
    }

    public static boolean hasRegisteredOnServer(Context context) {
        return createSharedPreferenceObject(context).getBoolean(SP_REGISTERED_ON_SERVER, false);
    }

}
