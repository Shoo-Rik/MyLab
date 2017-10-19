package com.shoorik.timesheet;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by Admin on 12.10.2017.
 */

public class MessageHelper {

    public static void ShowMessage(Context context, String message) {

        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }
}
