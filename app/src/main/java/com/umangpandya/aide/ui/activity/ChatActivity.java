package com.umangpandya.aide.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import com.umangpandya.aide.R;
import com.umangpandya.aide.data.storage.AccountManager;
import com.umangpandya.aide.model.local.UserProfile;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by umang on 23/10/16.
 */

public class ChatActivity extends AppCompatActivity {

    @BindView(R.id.toolbar) Toolbar toolbar;
    private static final String TAG = ChatActivity.class.getSimpleName();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_activity);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        UserProfile account = AccountManager.getUserData(this);
        if (actionBar != null) {
            actionBar.setTitle(account.getDisplayName());
        }
        Toast.makeText(this, account.getDisplayName(), Toast.LENGTH_SHORT).show();

    }

}
