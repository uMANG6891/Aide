package com.umangpandya.aide.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.umangpandya.aide.R;
import com.umangpandya.aide.data.storage.AccountManager;
import com.umangpandya.aide.model.local.UserProfile;
import com.umangpandya.aide.model.remote.request.RequestLogin;
import com.umangpandya.aide.model.remote.response.ResponseBase;
import com.umangpandya.aide.remote.ApiListeners;
import com.umangpandya.aide.remote.ApiManager;
import com.umangpandya.aide.utility.Debug;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Response;

public class WelcomeActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {

    private static final String TAG = WelcomeActivity.class.getSimpleName();
    @BindView(R.id.act_wc_sib_login) SignInButton signInButton;
    GoogleApiClient googleApiClient;

    private static final int RC_SIGN_IN = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        UserProfile userProfile = AccountManager.getUserData(this);
        boolean hasRegisteredOnServer = AccountManager.hasRegisteredOnServer(this);
        if (userProfile != null && hasRegisteredOnServer) {
            startHomeActivity();
        } else {
            setContentView(R.layout.activity_welcome);

            ButterKnife.bind(this);

            GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestEmail()
                    .requestProfile()
                    .requestId()
                    .build();

            googleApiClient = new GoogleApiClient.Builder(this)
                    .enableAutoManage(this, this)
                    .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                    .build();

            signInButton = (SignInButton) findViewById(R.id.act_wc_sib_login);
            signInButton.setSize(SignInButton.SIZE_WIDE);
            signInButton.setScopes(gso.getScopeArray());

            if (userProfile != null && !hasRegisteredOnServer) {
//                Save user information on the server
                handleGooglePlusSignIn(true);
            }
        }
    }

    @OnClick(R.id.act_wc_sib_login)
    void onClick() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
    }

    private void handleSignInResult(GoogleSignInResult result) {
        Debug.e(TAG, "handleSignInResult:", result.isSuccess());
        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount account = result.getSignInAccount();
            AccountManager.saveUserData(this, account);
            handleGooglePlusSignIn(true);
        } else {
            // Signed out, show unauthenticated UI.
            handleGooglePlusSignIn(false);
        }
    }

    private void handleGooglePlusSignIn(boolean isLoginSuccessful) {
        if (isLoginSuccessful) {
            UserProfile userData = AccountManager.getUserData(this);
            RequestLogin param = new RequestLogin(userData.getId(), userData.getDisplayName(), userData.getEmail(), userData.getPhotoUrl());
            ApiManager.userSignIn(this, param, new ApiListeners.SignInListener() {
                @Override
                public void done(Response<ResponseBase> response, boolean hasError, String error) {
                    if (hasError) {
                        updateUI(false, error);
                    } else {
                        AccountManager.saveHasRegisteredOnServer(WelcomeActivity.this);
                        updateUI(true, null);
                    }
                }
            });
        } else {
            updateUI(false, null);
        }
    }

    private void updateUI(boolean isLoginSuccessful, String error) {
        if (isLoginSuccessful) {
            startHomeActivity();
        } else {
            showError(error == null ? getString(R.string.error_could_not_login) : error);
        }
    }

    private void showError(String errorMessage) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(errorMessage)
                .show();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    private void startHomeActivity() {
        startActivity(new Intent(this, ChatActivity.class));
        finish();
    }
}
