package com.shoorik.timesheet.common;

import android.content.Context;
import android.widget.Toast;

public class MessageHelper {

    public static void ShowMessage(Context context, String message) {

        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }
}
