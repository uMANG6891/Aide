package com.umangpandya.aide.widget;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Binder;
import android.widget.AdapterView;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.umangpandya.aide.R;
import com.umangpandya.aide.data.provider.NotesContract.NoteEntry;
import com.umangpandya.aide.utility.Constants;

/**
 * Created by umang on 14/11/16.
 */

public class NotesWidgetRemoteViews extends RemoteViewsService {


    private static final String TAG = NotesWidgetRemoteViews.class.getSimpleName();

    @Override
    public RemoteViewsService.RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new RemoteViewsFactory() {

            Cursor cursor = null;
            Context context;

            @Override
            public void onCreate() {
                context = getApplicationContext();
            }

            @Override
            public void onDataSetChanged() {
                Uri uri = NoteEntry.CONTENT_URI;
                final long token = Binder.clearCallingIdentity();
                try {
                    cursor = getContentResolver().query(uri,
                            Constants.NOTE_PROJECTION_COLS,
                            null,
                            null,
                            null);
                } finally {
                    Binder.restoreCallingIdentity(token);
                }
            }

            @Override
            public void onDestroy() {
                if (cursor != null) {
                    cursor.close();
                }
            }

            @Override
            public int getCount() {
                return cursor == null ? 0 : cursor.getCount();
            }

            @Override
            public RemoteViews getViewAt(int position) {
                if (position == AdapterView.INVALID_POSITION || cursor == null || !cursor.moveToPosition(position)) {
                    return null;
                }

                final RemoteViews views = new RemoteViews(getPackageName(), R.layout.widget_item_note);
                views.setTextViewText(R.id.w_item_n_tv_note, cursor.getString(Constants.COL_NOTE_TEXT));
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
        }

                ;
    }

}