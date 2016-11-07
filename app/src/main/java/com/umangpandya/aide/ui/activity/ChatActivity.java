package com.umangpandya.aide.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

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
import com.umangpandya.aide.utility.Constants.*;
import com.umangpandya.aide.utility.Debug;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by umang on 23/10/16.
 */

public class ChatActivity extends AppCompatActivity {

    private static final String TAG = ChatActivity.class.getSimpleName();

    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.chat_lv_messages) ListView lvMessages;
    @BindView(R.id.chat_et_input) EditText etInput;
    @BindView(R.id.chat_iv_send) ImageView ivSend;

    MessageAdapter adapter;

    private DatabaseReference firebaseMessages;
    private ValueEventListener messageListener;

    private static final int WAIT_TILL_STATUS_CHANGE = 3000; // 3 seconds

    UserProfile currentUser;

    private boolean hasAlreadySentTypingStatus = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        UserProfile account = AccountManager.getUserData(this);
        if (actionBar != null) {
            actionBar.setTitle(account.getDisplayName());
        }
        currentUser = AccountManager.getUserData(this);

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
    void onClick(){
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
}
