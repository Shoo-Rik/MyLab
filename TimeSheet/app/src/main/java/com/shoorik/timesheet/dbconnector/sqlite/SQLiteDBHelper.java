package com.shoorik.timesheet.dbconnector.sqlite;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.content.Context;
import android.os.WorkSource;
import android.util.Log;

import com.shoorik.timesheet.dbconnector.sqlite.dto.WorkTimeTable;

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
        String sql = String.format(
            "create table %s (%s integer primary key autoincrement, %s string unique, %s string, %s string);",
            WorkTimeTable.TableName,
            WorkTimeTable.IdColumnName,
            WorkTimeTable.DateColumnName,
            WorkTimeTable.StartTimeColumnName,
            WorkTimeTable.EndTimeColumnName);
        // создаем таблицу с полями
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
}
