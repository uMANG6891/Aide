package com.umangpandya.aide.remote;

import com.umangpandya.aide.model.remote.request.RequestLogin;
import com.umangpandya.aide.model.remote.response.ResponseBase;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by umang on 07/11/16.
 */

interface ApiEndPoints {
    @POST("user")
    Call<ResponseBase> userSignIn(@Body RequestLogin param);
}