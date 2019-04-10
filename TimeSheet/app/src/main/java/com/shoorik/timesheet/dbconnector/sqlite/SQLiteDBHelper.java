package com.shoorik.timesheet.dbconnector.sqlite;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.content.Context;
import android.util.Log;

public class SQLiteDBHelper extends SQLiteOpenHelper {

    final static String LOG_TAG = "TimeSheetLogs";
    final static String DB_NAME = "TimeSheetDB";
    final static int Version = 2;

    public SQLiteDBHelper(Context context) {
        // конструктор суперкласса
        super(context, DB_NAME, null, Version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d(LOG_TAG, "--- onCreate database ---");
        String sql = String.format(
            "CREATE TABLE %s (%s INTEGER PRIMARY KEY AUTOINCREMENT, %s INTEGER, %s INTEGER, %s INTEGER, %s INTEGER DEFAULT -1, %s INTEGER DEFAULT -1, %s INTEGER DEFAULT -1, %s INTEGER DEFAULT -1);",
            WorkTimeTable.TableName,
            WorkTimeTable.IdColumnName,
            WorkTimeTable.YearColumnName,
            WorkTimeTable.MonthColumnName,
            WorkTimeTable.DayColumnName,
            WorkTimeTable.StartHourColumnName,
            WorkTimeTable.StartMinuteColumnName,
            WorkTimeTable.EndHourColumnName,
            WorkTimeTable.EndMinuteColumnName);
        // создаем таблицу с полями
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        Log.d(LOG_TAG, "--- onUpgrade database ---");

        db.execSQL(String.format("DROP TABLE %s;", WorkTimeTable.TableName));

        onCreate(db);
    }
}
