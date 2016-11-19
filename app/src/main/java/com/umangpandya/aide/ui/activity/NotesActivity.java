package com.umangpandya.aide.ui.activity;

import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.umangpandya.aide.R;
import com.umangpandya.aide.data.provider.NotesContract.NoteEntry;
import com.umangpandya.aide.data.storage.AccountManager;
import com.umangpandya.aide.model.local.UserProfile;
import com.umangpandya.aide.ui.adapter.NotesAdapter;
import com.umangpandya.aide.utility.Constants;
import com.umangpandya.aide.utility.UiUtility;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by umang on 13/11/16.
 */

public class NotesActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final String TAG = NotesActivity.class.getSimpleName();
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.act_n_rv_list) RecyclerView rvNotes;
    @BindView(R.id.act_n_tv_info) TextView tvInfo;

    NotesAdapter adapter;
    UserProfile currentUser;

    private Cursor DATA;

    private static final int LOADER_NOTES = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes);
        currentUser = AccountManager.getUserData(this);
        if (currentUser == null) {
            finishAffinity();
            UiUtility.startWelcomeActivity(this);
        } else {
            ButterKnife.bind(this);

            setSupportActionBar(toolbar);
            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) {
                actionBar.setTitle(R.string.title_notes);
                actionBar.setDisplayHomeAsUpEnabled(true);
            }


            adapter = new NotesAdapter(this, null);
            rvNotes.setAdapter(adapter);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        getSupportLoaderManager().initLoader(LOADER_NOTES, null, this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        getSupportLoaderManager().destroyLoader(LOADER_NOTES);
    }

    private void updateNotes() {
        adapter.swapData(DATA);
        if (DATA != null) {
            rvNotes.setVisibility(View.VISIBLE);
            tvInfo.setVisibility(View.GONE);
        } else {
            rvNotes.setVisibility(View.GONE);
            tvInfo.setVisibility(View.VISIBLE);
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

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        switch (id) {
            case LOADER_NOTES:
                return new CursorLoader(
                        this,
                        NoteEntry.CONTENT_URI,
                        Constants.NOTE_PROJECTION_COLS,
                        null,
                        null,
                        null
                );
            default:
                return null;
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        switch (loader.getId()) {
            case LOADER_NOTES:
                DATA = data;
                updateNotes();
                break;
            default:
                break;
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}
