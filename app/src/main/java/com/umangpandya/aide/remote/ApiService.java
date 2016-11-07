package com.umangpandya.aide.remote;

import com.umangpandya.aide.utility.Constants;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by umang on 07/11/16.
 */

public class ApiService {
    public static ApiEndPoints aideService;

    public static ApiEndPoints buildService() {
        if (aideService == null) {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(Constants.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            aideService = retrofit.create(ApiEndPoints.class);
        }
        return aideService;
    }
}