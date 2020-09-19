package com.ayata.firebasechat.utils;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

public class AppUtils {
    public static void toast(Context context, String string) {
//        Toast.makeText(context, string, Toast.LENGTH_SHORT).show();
    }

    public static void snackBar(View view, String message) {
        Snackbar snackbar = Snackbar.make(view, message, Snackbar.LENGTH_SHORT);
        snackbar.setAction("ok", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                snackbar.dismiss();
            }
        }).show();
    }

    public static void log(String tag, String logMessage) {
        Log.d(tag, "log: " + logMessage);
    }

    public static void nextActivity(Context context1, Class<?> activity2) {
        Intent intent = new Intent(context1, activity2);
        context1.startActivity(intent);
    }

    public static void progressHide(View view) {
        view.setVisibility(View.GONE);
    }

    public static void progressShow(View view) {
        view.setVisibility(View.VISIBLE);
    }
}
