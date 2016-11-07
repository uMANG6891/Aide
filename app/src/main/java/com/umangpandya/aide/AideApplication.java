package com.umangpandya.aide;

import android.app.Application;

import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by umang on 07/11/16.
 */

public class AideApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

//        FirebaseOptions options = new FirebaseOptions.Builder()
//                .setDatabaseUrl("https://academic-aloe-147215.firebaseio.com")
//                .setApiKey("AIzaSyBDkU7U0G4EzrVnLFOXlDvA11vRLTCNEeE")
//                .setApplicationId(BuildConfig.APPLICATION_ID)
//                .build();
//        FirebaseApp.initializeApp(this, options, "Aide");
        if (!FirebaseApp.getApps(this).isEmpty()) {
            FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        }
    }
}
