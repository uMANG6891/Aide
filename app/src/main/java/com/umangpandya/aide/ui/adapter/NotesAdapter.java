package com.umangpandya.aide.ui.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.umangpandya.aide.R;
import com.umangpandya.aide.data.storage.AccountManager;
import com.umangpandya.aide.model.local.Notes;
import com.umangpandya.aide.model.local.UserProfile;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by umang on 13/11/16.
 */

public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.ViewHolder> {

    private static final String TAG = NotesAdapter.class.getSimpleName();

    Context context;
    List<DataSnapshot> DATA;

    UserProfile currentUser;

    public NotesAdapter(Context context, List<DataSnapshot> DATA) {
        this.context = context;
        this.DATA = DATA;
        currentUser = AccountManager.getUserData(context);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.lis_item_note, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        DataSnapshot dataSnapshot = DATA.get(position);
        Notes note = dataSnapshot.getValue(Notes.class);
        holder.oneItem = dataSnapshot;
        holder.oneNote = note;
        holder.tvNote.setText(note.getNote());
//        holder.tvTime.setText(String.valueOf(item.getTimestamp()));

        int checked = note.getChecked();
        if (checked == 0) {
            holder.cbChecked.setChecked(false);
        } else {
            holder.cbChecked.setChecked(true);
        }
    }

    @Override
    public int getItemCount() {
        return DATA == null ? 0 : DATA.size();
    }

    public void swapData(ArrayList<DataSnapshot> data) {
        DATA = data;
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        final CheckBox cbChecked;
        final TextView tvNote;
        //        final TextView tvTime;
        Notes oneNote;
        DataSnapshot oneItem;

        ViewHolder(View view) {
            super(view);
            cbChecked = (CheckBox) view.findViewById(R.id.item_n_cb_checked);
            tvNote = (TextView) view.findViewById(R.id.item_n_tv_note);
//            tvTime = (TextView) view.findViewById(R.id.item_n_tv_time);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    oneNote.setChecked((oneNote.getChecked() + 1) % 2);
                    if (oneNote.getChecked() == 0) {
                        cbChecked.setChecked(false);
                    } else {
                        cbChecked.setChecked(false);
                    }
                    // updating value
                    oneItem.getRef().setValue(oneNote);
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
                                                                    oneItem.getRef().removeValue();
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
                    oneNote.setChecked(b ? 1 : 0);
                    oneItem.getRef().setValue(oneNote);
                }
            });
        }
    }
}
