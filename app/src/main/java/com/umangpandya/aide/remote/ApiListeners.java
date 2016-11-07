package com.umangpandya.aide.remote;

import com.umangpandya.aide.model.remote.response.ResponseBase;

import retrofit2.Response;

/**
 * Created by umang on 07/11/16.
 */

public class ApiListeners {

    // For Sign in
    public interface SignInListener {
        void done(Response<ResponseBase> response, boolean hasError, String error);
    }
}
