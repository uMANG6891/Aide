package com.umangpandya.aide.ui.adapter;

import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.umangpandya.aide.R;
import com.umangpandya.aide.data.provider.NotesContract.NoteEntry;
import com.umangpandya.aide.data.storage.AccountManager;
import com.umangpandya.aide.utility.Constants;
import com.umangpandya.aide.utility.Debug;

/**
 * Created by umang on 13/11/16.
 */

public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.ViewHolder> {

    private static final String TAG = NotesAdapter.class.getSimpleName();

    private Context context;
    private Cursor DATA;

    public NotesAdapter(Context context, Cursor DATA) {
        this.context = context;
        this.DATA = DATA;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_note, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (DATA != null && DATA.moveToPosition(holder.getAdapterPosition())) {
            holder.position = holder.getAdapterPosition();
            holder.tvNote.setText(DATA.getString(Constants.COL_NOTE_TEXT));
//        holder.tvTime.setText(String.valueOf(item.getTimestamp()));

            int checked = DATA.getInt(Constants.COL_NOTE_CHECKED);
            if (checked == 0) {
                holder.cbChecked.setChecked(false);
            } else {
                holder.cbChecked.setChecked(true);
            }
        }
    }

    @Override
    public int getItemCount() {
        if (DATA != null)
            Debug.e(TAG, "length", DATA.getCount());
        return DATA == null ? 0 : DATA.getCount();
    }

    public void swapData(Cursor data) {
        DATA = data;
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        final CheckBox cbChecked;
        final TextView tvNote;
        int position;

        ViewHolder(View view) {
            super(view);
            cbChecked = (CheckBox) view.findViewById(R.id.item_n_cb_checked);
            tvNote = (TextView) view.findViewById(R.id.item_n_tv_note);
//            tvTime = (TextView) view.findViewById(R.id.item_n_tv_time);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    updateCheckedStatus();
                }
            });
            view.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                            context,
                            android.R.layout.simple_selectable_list_item);
                    arrayAdapter.add("Delete");
                    AlertDialog.Builder builderSingle = new AlertDialog.Builder(context);
                    builderSingle.setAdapter(
                            arrayAdapter,
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(final DialogInterface dialog, int which) {
                                    switch (which) {
                                        // Delete
                                        case 0:
                                            AlertDialog.Builder builderInner = new AlertDialog.Builder(context);
                                            builderInner
                                                    .setMessage(R.string.delete_note_message)
                                                    .setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            context.getContentResolver().delete(
                                                                    NoteEntry.CONTENT_URI,
                                                                    NoteEntry._ID + " = ?",
                                                                    new String[]{String.valueOf(DATA.getInt(Constants.COL_NOTE_ID))}
                                                            );
                                                        }
                                                    })
                                                    .setNegativeButton(R.string.dismiss, new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialogInterface, int i) {
                                                            dialogInterface.dismiss();
                                                        }
                                                    })
                                                    .show();
                                            break;

                                    }
                                }
                            });
                    builderSingle.show();
                    return true;
                }
            });
            cbChecked.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    updateCheckedStatus();
                }
            });
        }

        private void updateCheckedStatus() {
            DATA.moveToPosition(position);
            int checked = DATA.getInt(Constants.COL_NOTE_CHECKED);
            checked = ++checked % 2;
            ContentValues cv = new ContentValues();
            cv.put(NoteEntry.COLUMN_CHECKED, checked);
            context.getContentResolver().update(
                    NoteEntry.CONTENT_URI,
                    cv,
                    NoteEntry._ID + " = ?",
                    new String[]{String.valueOf(DATA.getInt(Constants.COL_NOTE_ID))}
            );
        }
    }
}
