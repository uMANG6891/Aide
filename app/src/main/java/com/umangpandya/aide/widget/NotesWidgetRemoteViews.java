package com.umangpandya.aide.widget;

import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.widget.AdapterView;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.umangpandya.aide.R;
import com.umangpandya.aide.data.storage.AccountManager;
import com.umangpandya.aide.model.local.Notes;
import com.umangpandya.aide.model.local.UserProfile;
import com.umangpandya.aide.utility.Constants;
import com.umangpandya.aide.utility.Debug;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by umang on 14/11/16.
 */

public class NotesWidgetRemoteViews extends RemoteViewsService {


    private static final String TAG = NotesWidgetRemoteViews.class.getSimpleName();

    @Override
    public RemoteViewsService.RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new RemoteViewsFactory() {
            List<DataSnapshot> DATA;
            private DatabaseReference firebaseNotes;
            private ValueEventListener notesListener;
            Context context;

            @Override
            public void onCreate() {
                context = getApplicationContext();
                DATA = new ArrayList<>();

                UserProfile currentUser = AccountManager.getUserData(context);
                firebaseNotes = Constants.getFirebaseNotesUrl(context, currentUser.getId());
                notesListener = new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot != null) {
                            DATA = new ArrayList<>();
                            for (DataSnapshot data : dataSnapshot.getChildren()) {
                                DATA.add(0, data);
                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Debug.e(TAG, "onCancelled", databaseError.getDetails());
                    }
                };
                firebaseNotes.addValueEventListener(notesListener);

            }

            @Override
            public void onDataSetChanged() {
                Binder.restoreCallingIdentity(Binder.clearCallingIdentity());
            }

            @Override
            public void onDestroy() {
                firebaseNotes.removeEventListener(notesListener);
            }

            @Override
            public int getCount() {
                return DATA == null ? 0 : DATA.size();
            }

            @Override
            public RemoteViews getViewAt(int position) {
                if (position == AdapterView.INVALID_POSITION) {
                    return null;
                }
                DataSnapshot dataSnapshot = DATA.get(position);
                Notes note = dataSnapshot.getValue(Notes.class);

                final RemoteViews views = new RemoteViews(getPackageName(), R.layout.widget_item_note);
                Debug.e(TAG, "position", position);
                views.setTextViewText(R.id.w_item_n_tv_note, note.getNote());
                return views;
            }

            @Override
            public RemoteViews getLoadingView() {
                return new RemoteViews(getPackageName(), R.layout.widget_item_note);
            }

            @Override
            public int getViewTypeCount() {
                return 1;
            }

            @Override
            public long getItemId(int position) {
                return position;
            }

            @Override
            public boolean hasStableIds() {
                return true;
            }
        };
    }

}