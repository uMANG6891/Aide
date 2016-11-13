package com.umangpandya.aide.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.umangpandya.aide.R;
import com.umangpandya.aide.data.storage.AccountManager;
import com.umangpandya.aide.model.local.UserProfile;
import com.umangpandya.aide.ui.adapter.NotesAdapter;
import com.umangpandya.aide.utility.Constants;
import com.umangpandya.aide.utility.Debug;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by umang on 13/11/16.
 */

public class NotesActivity extends AppCompatActivity {

    private static final String TAG = NotesActivity.class.getSimpleName();
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.act_n_rv_list) RecyclerView rvNotes;
    @BindView(R.id.act_n_tv_info) TextView tvInfo;

    NotesAdapter adapter;
    UserProfile currentUser;

    private DatabaseReference firebaseNotes;
    private ValueEventListener notesListener;
    private ArrayList<DataSnapshot> DATA;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(R.string.title_notes);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        currentUser = AccountManager.getUserData(this);

        adapter = new NotesAdapter(this, null);
        rvNotes.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        firebaseNotes = Constants.getFirebaseNotesUrl(this, currentUser.getId());
        notesListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot != null) {
                    DATA = new ArrayList<>();
                    for (DataSnapshot data : dataSnapshot.getChildren()) {
                        DATA.add(0, data);
                    }
                    updateNotes();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Debug.e(TAG, "onCancelled", databaseError.getDetails());
            }
        };
        firebaseNotes.addValueEventListener(notesListener);
    }

    private void updateNotes() {
        if (DATA != null) {
            if (DATA.size() == 0) {
                rvNotes.setVisibility(View.GONE);
                tvInfo.setVisibility(View.VISIBLE);
            } else {
                rvNotes.setVisibility(View.VISIBLE);
                tvInfo.setVisibility(View.GONE);
            }
            adapter.swapData(DATA);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return false;
        }
    }
}
