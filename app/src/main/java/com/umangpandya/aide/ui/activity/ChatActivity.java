package com.umangpandya.aide.ui.activity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.umangpandya.aide.R;
import com.umangpandya.aide.data.storage.AccountManager;
import com.umangpandya.aide.model.local.Chat;
import com.umangpandya.aide.model.local.UserProfile;
import com.umangpandya.aide.ui.adapter.MessageAdapter;
import com.umangpandya.aide.utility.Constants;
import com.umangpandya.aide.utility.Constants.MessageType;
import com.umangpandya.aide.utility.Debug;
import com.umangpandya.aide.utility.UiUtility;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by umang on 23/10/16.
 */

public class ChatActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = ChatActivity.class.getSimpleName();

    @BindView(R.id.drawer_layout) DrawerLayout drawer;
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.chat_lv_messages) ListView lvMessages;
    @BindView(R.id.chat_et_input) EditText etInput;
    @BindView(R.id.chat_iv_send) ImageView ivSend;

    MessageAdapter adapter;

    private DatabaseReference firebaseMessages;
    private ValueEventListener messageListener;

    private static final int WAIT_TILL_STATUS_CHANGE = 3000; // 3 seconds

    UserProfile currentUser;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
//        UserProfile account = AccountManager.getUserData(this);
        if (actionBar != null) {
//            actionBar.setTitle(account.getDisplayName());
            actionBar.setTitle(getString(R.string.app_name));
        }
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                super.onDrawerSlide(drawerView, slideOffset);
                UiUtility.closeKeyboard(ChatActivity.this, toolbar.getWindowToken());
            }
        };
        if (drawer != null) {
            drawer.addDrawerListener(toggle);
        }
        toggle.syncState();

        currentUser = AccountManager.getUserData(this);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        if (navigationView != null) {
            navigationView.setNavigationItemSelectedListener(this);

            View headerView = navigationView.getHeaderView(0);
            if (headerView != null) {
                String url = currentUser.getPhotoUrl();
                url = url == null ? null : url.trim();
                if (url != null && url.length() != 0 && !url.equalsIgnoreCase("null")) {
                    final ImageView ivUserImage = (ImageView) headerView.findViewById(R.id.nav_bar_iv_user_image);
                    Glide.with(this)
                            .load(currentUser.getPhotoUrl())
                            .asBitmap()
                            .centerCrop()
                            .placeholder(R.mipmap.ic_launcher)
                            .error(R.mipmap.ic_launcher)
                            .into(new BitmapImageViewTarget(ivUserImage) {
                                @Override
                                protected void setResource(Bitmap resource) {
                                    RoundedBitmapDrawable circularBitmapDrawable =
                                            RoundedBitmapDrawableFactory.create(getResources(), resource);
                                    circularBitmapDrawable.setCircular(true);
                                    ivUserImage.setImageDrawable(circularBitmapDrawable);
                                }
                            });
                }
                TextView tvName = (TextView) navigationView.getHeaderView(0).findViewById(R.id.nav_bar_tv_name);
                TextView tvEmail = (TextView) navigationView.getHeaderView(0).findViewById(R.id.nav_bar_tv_email);
                tvName.setText(currentUser.getDisplayName());
                tvEmail.setText(currentUser.getEmail());
            }
        }

        adapter = new MessageAdapter(this, null);
        lvMessages.setAdapter(adapter);
        etInput = (EditText) findViewById(R.id.chat_et_input);
        TextWatcher tw = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                String message = etInput.getText().toString().trim();
                if (message.length() == 0) {
                    handleTextEmpty();
                } else {
                    handleTextNotEmpty();
                    sendTypingStatus();
                }
            }
        };

        etInput.addTextChangedListener(tw);

        etInput.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                final int lastPos = adapter.getCount() - 1;

                if (lvMessages.getLastVisiblePosition() == lastPos) {
                    etInput.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            lvMessages.setSelection(lastPos);
                        }
                    }, 300);
                }
                return false;
            }
        });
    }

    @OnClick({R.id.chat_iv_send})
    void onClick() {
        String textContent = etInput.getText().toString().trim();
        if (textContent.length() > 0) {
            sendMessage(MessageType.TEXT, textContent, null);
        }
    }

    private void sendMessage(int messageType, String textContent, String image_url) {
        Chat chat = new Chat(
                currentUser.getId(),
                messageType,
                textContent,
                0,
                System.currentTimeMillis()
        );
        DatabaseReference message = firebaseMessages.push();
        message.setValue(chat);
        etInput.setText("");
        handleTextEmpty();
    }

    private void handleTextEmpty() {
        ivSend.setEnabled(false);
        ivSend.setAlpha(0.5f);
//        ivCamera.setVisibility(View.VISIBLE);
    }

    private void handleTextNotEmpty() {
        ivSend.setEnabled(true);
        ivSend.setAlpha(1.0f);
//        ivCamera.setVisibility(View.GONE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        firebaseMessages = Constants.getFirebaseMessageUrl(this, currentUser.getId());
        messageListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot != null) {
                    Chat chat;
                    List<Chat> chats = new ArrayList<>();
                    for (DataSnapshot data : dataSnapshot.getChildren()) {
                        chat = data.getValue(Chat.class);
                        chats.add(chat);
                        if (chat.getFrom().equals(Constants.MESSAGE_SENDER_TYPE_AIDE)) {
                            // message sent by wizard
                            if (chat.getStatus() == 1) {
                                // play sound notifying new message on screen
//                                Utility.playIncomingMessageOnUI(ChatActivity.this);
                            }
                            if (chat.getStatus() == 1 || chat.getStatus() == 2) {
                                DatabaseReference update = firebaseMessages.child(data.getKey());
                                chat.setStatus(3);
                                update.setValue(chat);
                            }
                        }
                    }
                    updateChatData(chats);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Debug.e(TAG, "onCancelled", databaseError.getDetails());
            }
        };
        firebaseMessages.addValueEventListener(messageListener);
    }

    private void updateChatData(List<Chat> chats) {
        adapter.swapData(chats);
        lvMessages.setSelection(chats.size() - 1);
    }

    private void sendTypingStatus() {
//        if (!hasAlreadySentTypingStatus) {
//            // before wait
//            hasAlreadySentTypingStatus = true;
//            jaTypingUsers.put(AccountManager.getUserId(this));
//            firebaseTravelerTyping.setValue(1);
//            new Thread() {
//                @Override
//                public void run() {
//                    super.run();
//                    try {
//                        synchronized (this) {
//                            wait(WAIT_TILL_STATUS_CHANGE);
//                        }
//                        // after wait
//                        removeMyIdFromTyping();
//                        hasAlreadySentTypingStatus = false;
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//                }
//            }.start();
//        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.navigation_item_notes:
                closeDrawer();
                UiUtility.startNotesActivity(this);
                return true;
            case R.id.navigation_author:
                closeDrawer();
                AlertDialog.Builder dialog = new AlertDialog.Builder(this);
                dialog.setMessage(R.string.author_message)
                        .show();
                return true;
//            case R.id.nav_logout:
//                closeDrawer();
//                AlertDialog.Builder dialog = new AlertDialog.Builder(this);
//                dialog.setTitle(R.string.nav_logout)
//                        .setMessage(R.string.logout_detail)
//                        .setPositiveButton(R.string.nav_logout, new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                dialog.dismiss();
//                                AccountManager.removeEveryThingFromSP(BaseActivity.this);
//                                UIUtility.openRegisterPage(BaseActivity.this);
//
//                                LoginManager.getInstance().logOut();
//                                finish();
//                            }
//                        })
//                        .setNegativeButton(R.string.dismiss, new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                dialog.dismiss();
//                            }
//                        })
//                        .show();
//                return true;

            default:
                return false;
        }
    }

    private void closeDrawer() {
        if (drawer != null) {
            drawer.closeDrawer(GravityCompat.START);
        }
    }

}
