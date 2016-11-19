package com.umangpandya.aide.ui.adapter;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.umangpandya.aide.R;
import com.umangpandya.aide.data.provider.NotesContract.NoteEntry;
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
                    DATA.moveToPosition(position);
                    int checked = DATA.getInt(Constants.COL_NOTE_CHECKED);
                    checked = ++checked % 2;
                    cbChecked.setChecked(checked != 0);
                }
            });
            cbChecked.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    updateCheckedStatus(b ? 1 : 0);
                }
            });
        }

        private void updateCheckedStatus(int checked) {
            DATA.moveToPosition(position);
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
