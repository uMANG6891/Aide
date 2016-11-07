package com.umangpandya.aide.ui.adapter;

import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.umangpandya.aide.R;
import com.umangpandya.aide.data.storage.AccountManager;
import com.umangpandya.aide.model.local.Chat;
import com.umangpandya.aide.model.local.UserProfile;
import com.umangpandya.aide.utility.Constants;
import com.umangpandya.aide.utility.Constants.MessageType;
import com.umangpandya.aide.utility.Utility;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Created by umang on 07/11/16.
 */

public class MessageAdapter extends BaseAdapter {
    AppCompatActivity con;
    List<Chat> CHATS;

    private final int USER_BUBBLE = 0;
    private final int AIDE_BUBBLE = 1;
    private final int SYSTEM_BUBBLE = 2;
    private final int VIEW_TYPE_COUNT = 3;

    public MessageAdapter(AppCompatActivity con, List<Chat> chats) {
        this.con = con;
        CHATS = chats;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view, holder;
        LayoutInflater inflater = LayoutInflater.from(con);
        switch (getItemViewType(position)) {
            case USER_BUBBLE:
                view = inflater.inflate(R.layout.item_message_user_bubble, parent, false);
                holder = new BubbleVH(view, position);
                break;
            case AIDE_BUBBLE:
                view = inflater.inflate(R.layout.item_message_aide_bubble, parent, false);
                holder = new BubbleVH(view, position);
                break;
            default:
            case SYSTEM_BUBBLE:
                view = inflater.inflate(R.layout.item_message_system_bubble, parent, false);
                holder = new SystemVH(view, position);
                break;
        }
        final Chat chat = CHATS.get(position);
        Date lastDate = new Date(0); // December 26, 1970

        if (position != 0) {
            Chat lastChat = CHATS.get(position - 1);
            lastDate = new Date(lastChat.getTimestamp());
        }
        Date currentDate = new Date(chat.getTimestamp());
        String printDate = Utility.getTimeToBePrintedInChat(currentDate, lastDate);
        if (getItemViewType(position) == SYSTEM_BUBBLE) {
            SystemVH systemVH = (SystemVH) holder;
            String message = chat.getBody();

            UserProfile user = AccountManager.getUserData(con);

            message = message.replace("%firstName%", user.getGivenName());
            message = message.replace("%fullName%", user.getDisplayName());
            systemVH.tvBody.setText(message);

            //adding date
            if (!printDate.isEmpty()) {
                systemVH.llDate.setVisibility(View.VISIBLE);
                systemVH.tvTimestamp.setText(printDate);
            } else {
                systemVH.llDate.setVisibility(View.GONE);
            }
        } else {
            final BubbleVH bubbleVH = (BubbleVH) holder;
            switch (getItemViewType(position)) {
                default:
                case USER_BUBBLE:
                    bubbleVH.ivStatus.setVisibility(View.VISIBLE);
                    bubbleVH.ivStatus.setImageResource(Utility.getMessageStatusRecourse(chat.getStatus()));
                    break;
                case AIDE_BUBBLE:
                    bubbleVH.ivStatus.setVisibility(View.GONE);
                    break;
            }
            bubbleVH.tvBody.setText(Utility.getLinkInMessage(con, chat.getBody()));
            bubbleVH.tvBody.setMovementMethod(LinkMovementMethod.getInstance());
            bubbleVH.tvTime.setText(Utility.getMessagePrintableTime(chat.getTimestamp()));
            // show image if exists
            if (chat.getMessageType() == MessageType.TEXT) {
//                bubbleVH.ivMessageImage.setVisibility(View.GONE);
            } else if (chat.getMessageType() == MessageType.IMAGE) {
//                bubbleVH.ivMessageImage.setVisibility(View.VISIBLE);
//                bubbleVH.ivMessageImage.setScaleType(ImageView.ScaleType.CENTER);
//                Glide.with(con)
//                        .load(chat.getImageUrl() + Constants.IMAGE_TYPE_THUMB)
//                        .listener(new RequestListener<String, GlideDrawable>() {
//                            @Override
//                            public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
//                                return false;
//                            }
//
//                            @Override
//                            public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
//                                bubbleVH.ivMessageImage.setScaleType(ImageView.ScaleType.CENTER_CROP);
//                                return false;
//                            }
//                        })
//                        .into(bubbleVH.ivMessageImage);
//                // loading large image
//                String folder = Utility.getLargeImageFolderName();
//                String chatImageLocation = folder + Utility.getLargeImageUrl(chat);
//                File largeImage = new File(chatImageLocation);
//                if (!largeImage.exists())
//                    new LoadImageInBackground().execute(chat.getImageUrl() + Constants.IMAGE_TYPE_LARGE,
//                            folder,
//                            chatImageLocation);
            } else if (chat.getMessageType() == MessageType.LOCATION) {
//                bubbleVH.ivMessageImage.setVisibility(View.GONE);
//                bubbleVH.tvBody.setText("\uD83D\uDCCC Location sent  ");
//                bubbleVH.tvBody.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        String[] location = chat.getBody().split(",");
//                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("geo:" + location[0] + "," + location[1]));
//                        con.startActivity(intent);
//                    }
//                });
            }

            //adding date
            if (!printDate.isEmpty()) {
                bubbleVH.llDate.setVisibility(View.VISIBLE);
                bubbleVH.tvTimestamp.setText(printDate);
            } else {
                bubbleVH.llDate.setVisibility(View.GONE);
            }
        }
        return view;
    }

    @Override
    public int getItemViewType(int position) {
        if (CHATS == null)
            return USER_BUBBLE;
        else {
            Chat chat = CHATS.get(position);
            if (chat.getFrom().equals(Constants.MESSAGE_SENDER_TYPE_AIDE)) {
                return AIDE_BUBBLE;
            } else {
                return USER_BUBBLE;
            }
//            else if (chat.getFrom() == SYSTEM_BUBBLE) {
//                return SYSTEM_BUBBLE;
//            }
        }
    }

    @Override
    public int getViewTypeCount() {
        return VIEW_TYPE_COUNT;
    }

    @Override
    public int getCount() {
        return CHATS == null ? 0 : CHATS.size();
    }

    @Override
    public Chat getItem(int position) {
        return (position >= 0 && position < CHATS.size()) ? CHATS.get(position) : null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    public String getChatText(int position) {
        return CHATS.get(position).getBody();
    }

    public void swapData(List<Chat> chats) {
        CHATS = chats;
        notifyDataSetChanged();
    }

    public class SystemVH extends View {

        TextView tvBody, tvTimestamp;
        LinearLayout llDate;

        public SystemVH(View itemView, int position) {
            super(con);
            tvBody = (TextView) itemView.findViewById(R.id.item_mb_tv_body);
            tvBody.setMaxWidth((int) (Utility.getScreenWidth(con) * 0.75));
            llDate = (LinearLayout) itemView.findViewById(R.id.item_md_ll_date_main);
            tvTimestamp = (TextView) itemView.findViewById(R.id.item_md_tv_date);
        }
    }

    public class BubbleVH extends View {

        TextView tvBody, tvTime, tvTimestamp;
        ImageView ivStatus;
        //        ImageView ivMessageImage;
        LinearLayout llDate, llMain;

        public BubbleVH(View itemView, final int position) {
            super(con);
            llMain = (LinearLayout) itemView.findViewById(R.id.item_mb_ll_main_click);
            tvBody = (TextView) itemView.findViewById(R.id.item_mb_tv_body);
            tvBody.setMaxWidth((int) (Utility.getScreenWidth(con) * 0.7));
            tvTime = (TextView) itemView.findViewById(R.id.item_mb_tv_time);
            ivStatus = (ImageView) itemView.findViewById(R.id.item_mb_iv_status);
            llDate = (LinearLayout) itemView.findViewById(R.id.item_md_ll_date_main);
            tvTimestamp = (TextView) itemView.findViewById(R.id.item_md_tv_date);

//            ivMessageImage = (ImageView) itemView.findViewById(R.id.item_mb_iv_uploaded);
//            ivMessageImage.setOnClickListener(new OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Chat chat = CHATS.get(position);
//                    String folder = Utility.getLargeImageFolderName();
//                    String chatImageLocation = folder + Utility.getLargeImageUrl(chat);
//                    File largeImage = new File(chatImageLocation);
//                    if (largeImage.exists()) {
//                        Intent intent = new Intent();
//                        intent.setAction(Intent.ACTION_VIEW);
//                        intent.setDataAndType(Uri.parse("file://" + chatImageLocation), "image/*");
//                        con.startActivity(intent);
//                    } else {
//                        Toast.makeText(con, R.string.please_wait_downloading_image, Toast.LENGTH_SHORT).show();
//                    }
//                }
//            });
            llMain.setOnLongClickListener(new OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    handleOnLongClick(position);
                    return true;
                }
            });
            tvBody.setOnLongClickListener(new OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    handleOnLongClick(position);
                    return true;
                }
            });
        }

        private void handleOnLongClick(int position) {
            final String message = getChatText(position);
            if (message.length() != 0) { // avoiding the case with type image and no caption text
                final String[] items = con.getResources().getStringArray(R.array.chat_long_click_menu);
                AlertDialog.Builder builder = new AlertDialog.Builder(con);
                builder.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int item) {
                        if (item == 0) {
                            if (!message.isEmpty()) {
                                ClipboardManager clipboard = (ClipboardManager) con.getSystemService(AppCompatActivity.CLIPBOARD_SERVICE);
                                ClipData clip = ClipData.newPlainText("message", message);
                                clipboard.setPrimaryClip(clip);
                                Toast.makeText(con, con.getString(R.string.text_copied), Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(con, con.getString(R.string.text_not_copied), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
                builder.show();
            }
        }
    }

    public class LoadImageInBackground extends AsyncTask<String, Void, Void> {
        @Override
        protected Void doInBackground(String... params) {
            try {
                Bitmap resource = Glide.with(con)
                        .load(params[0])
                        .asBitmap()
                        .into(-1, -1)
                        .get();

                // creating folder ithaka if it doesn't exists
                File file = new File(params[1]);
                boolean success = true;
                if (!file.exists()) {
                    success = file.mkdirs();
                }

                if (success) {
                    try {
                        resource.compress(
                                Bitmap.CompressFormat.JPEG,
                                100,
                                new FileOutputStream(params[2]));
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}
