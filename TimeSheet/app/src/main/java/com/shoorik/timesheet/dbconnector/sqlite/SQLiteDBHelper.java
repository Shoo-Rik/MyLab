package com.shoorik.timesheet.dbconnector.sqlite;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.content.Context;
import android.util.Log;

public class SQLiteDBHelper extends SQLiteOpenHelper {

    final static String LOG_TAG = "TimeSheetLogs";
    final static String DB_NAME = "TimeSheetDB";

    public SQLiteDBHelper(Context context) {
        // конструктор суперкласса
        super(context, DB_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d(LOG_TAG, "--- onCreate database ---");
        // создаем таблицу с полями
        db.execSQL("create table WorkTime (id integer primary key autoincrement, time DateTime, type integer);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
}
