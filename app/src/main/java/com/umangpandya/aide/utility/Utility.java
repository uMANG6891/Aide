package com.umangpandya.aide.utility;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.annotation.DrawableRes;
import android.support.v7.app.AppCompatActivity;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ClickableSpan;
import android.util.DisplayMetrics;
import android.util.Patterns;
import android.view.View;

import com.umangpandya.aide.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by umang on 07/11/16.
 */

public class Utility {
    private static final String TAG = Utility.class.getSimpleName();

    public static boolean isInternetConnectionAvailable(Context context) {
        try {
            ConnectivityManager connectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            return networkInfo != null && networkInfo.isAvailable() &&
                    networkInfo.isConnected();
        } catch (Exception e) {
            Debug.e(TAG, "CheckConnectivity Exception: ", e.getMessage());
            Debug.e(TAG, "connectivity", e.toString());
        }
        return false;
    }

    public static boolean isSuccessful(int code) {
        return code == 0;
    }


    public static String getTimeToBePrintedInChat(Date currentDate, Date lastDate) {
        String time = "";
        boolean printTime = false;
        SimpleDateFormat sdf = new SimpleDateFormat("d", Locale.US);
        int last = Integer.parseInt(sdf.format(lastDate));
        int current = Integer.parseInt(sdf.format(currentDate));
        if (current != last) {
            printTime = true;
        } else {
            sdf = new SimpleDateFormat("M", Locale.US);
            last = Integer.parseInt(sdf.format(lastDate));
            current = Integer.parseInt(sdf.format(currentDate));
            if (current != last) {
                printTime = true;
            } else {
                sdf = new SimpleDateFormat("yyyy", Locale.US);
                last = Integer.parseInt(sdf.format(lastDate));
                current = Integer.parseInt(sdf.format(currentDate));
                if (current != last) {
                    printTime = true;
                }
            }
        }
        if (printTime) {
            sdf = new SimpleDateFormat("MMM d, yyyy", Locale.US);
            time = sdf.format(currentDate);
        }
        return time;
    }

    @DrawableRes
    public static int getMessageStatusRecourse(int status) {
        switch (status) {
            default:
            case 0:
                return R.drawable.message_unsent;
            case 1:
                return R.drawable.message_got_receipt_from_server;
            case 2:
                return R.drawable.msg_status_client_received;
            case 3:
                return R.drawable.msg_status_client_read;
        }
    }

    public static SpannableStringBuilder getLinkInMessage(final Context con, String body) {
        final String[] words = body.split(" ");
        SpannableStringBuilder sbMain, sb;
        sbMain = new SpannableStringBuilder();
        for (int i = 0; i < words.length; i++) {
            sb = new SpannableStringBuilder();
            sb.append(words[i]);
            if (words[i].matches(Patterns.WEB_URL.pattern())) {
                final int finalI = i;
                sb.setSpan(new ClickableSpan() {
                    @Override
                    public void onClick(View widget) {
                        Uri uri;
                        if (!words[finalI].startsWith("http") && !words[finalI].startsWith("https")) {
                            uri = Uri.parse("http://" + words[finalI]);
                        } else {
                            uri = Uri.parse(words[finalI]);
                        }
                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, uri);
                        con.startActivity(browserIntent);
                    }
                }, 0, words[i].length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            } else if (words[i].matches(Patterns.PHONE.pattern())) {
                final int finalI = i;
                sb.setSpan(new ClickableSpan() {
                    @Override
                    public void onClick(View widget) {
                        Intent intent = new Intent(Intent.ACTION_DIAL);
                        intent.setData(Uri.parse("tel:" + words[finalI]));
                        con.startActivity(intent);
                    }
                }, 0, words[i].length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
            sb.append(" ");
            sbMain.append(sb);
        }
        return sbMain;
    }

    public static String getMessagePrintableTime(long timestamp) {
        Date d = new Date(timestamp);
        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a", Locale.US);
        return sdf.format(d);
    }

    public static int getScreenWidth(AppCompatActivity context) {
        DisplayMetrics metrics = new DisplayMetrics();
        context.getWindowManager().getDefaultDisplay().getMetrics(metrics);
        return metrics.widthPixels;
    }
}