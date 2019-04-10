package com.shoorik.timesheet.dbconnector.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.shoorik.timesheet.interfaces.IDataStorage;

import java.util.Calendar;
import java.util.Date;

public class SQLiteDataStorage implements IDataStorage {

    private SQLiteDBHelper _sqliteDbHelper;

    public SQLiteDataStorage(Context context) {

        // создаем объект для создания и управления версиями БД
        _sqliteDbHelper = new SQLiteDBHelper(context);
    }

    @Override
    public Date getStartTime(Date date) {

        return GetValue(date, true);
    }

    @Override
    public Date getEndTime(Date date) {

        return GetValue(date, false);
    }

    @Override
    public void setStartTime(Date startDateTime) {

        SetValue(startDateTime, true);
    }

    @Override
    public void setEndTime(Date endDateTime) {

        SetValue(endDateTime, false);
    }

    // =======================================================

    private Date GetValue(Date date, boolean isStartTime) {

        Date result = null;
        DateTimeHolder dateTimeParameters = InitializeDate(date);

        // подключаемся к БД
        SQLiteDatabase db = _sqliteDbHelper.getWritableDatabase();

        try {
            if (GetTimeByDate(db, dateTimeParameters, isStartTime)
                    && dateTimeParameters.IsTimeDefined(isStartTime)) {

                int year = dateTimeParameters.Year;
                int month = dateTimeParameters.Month;
                int day = dateTimeParameters.Day;
                int hours = isStartTime ? dateTimeParameters.StartHour : dateTimeParameters.EndHour;
                int minutes = isStartTime ? dateTimeParameters.StartMinute : dateTimeParameters.EndMinute;

                Calendar calendarDate = Calendar.getInstance();
                calendarDate.set(year, month, day, hours, minutes, 0);

                result = calendarDate.getTime();
            }
        }
        finally {
            // закрываем подключение к БД
            _sqliteDbHelper.close();
        }

        return result;
    }

    private void SetValue(Date dateTime, boolean isStartTime) {

        // создаем объект для данных
        ContentValues cv = new ContentValues();

        final String hourColumnName = isStartTime ? WorkTimeTable.StartHourColumnName : WorkTimeTable.EndHourColumnName;
        final String minuteColumnName = isStartTime ? WorkTimeTable.StartMinuteColumnName : WorkTimeTable.EndMinuteColumnName;

        Calendar calendarDate = Calendar.getInstance();
        calendarDate.setTime(dateTime);

        cv.put(WorkTimeTable.YearColumnName, calendarDate.get(Calendar.YEAR));
        cv.put(WorkTimeTable.MonthColumnName, calendarDate.get(Calendar.MONTH));
        cv.put(WorkTimeTable.DayColumnName, calendarDate.get(Calendar.DAY_OF_MONTH));
        cv.put(hourColumnName, calendarDate.get(Calendar.HOUR_OF_DAY));
        cv.put(minuteColumnName, calendarDate.get(Calendar.MINUTE));

        DateTimeHolder dateTimeParameters = InitializeDate(dateTime);

        // подключаемся к БД
        SQLiteDatabase db = _sqliteDbHelper.getWritableDatabase();
        try {
            if (GetTimeByDate(db, dateTimeParameters, isStartTime)) {

                String whereClause = String.format("%s=%d", WorkTimeTable.IdColumnName, dateTimeParameters.Id);
                int rowCount = db.update(WorkTimeTable.TableName, cv, whereClause, null);
            } else {
                // вставляем запись и получаем ее ID
                long rowID = db.insert(WorkTimeTable.TableName, null, cv);
            }
        }
        finally {
            _sqliteDbHelper.close();
        }
    }

    private static DateTimeHolder InitializeDate(Date date) {

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        DateTimeHolder result = new DateTimeHolder();
        result.Year = calendar.get(Calendar.YEAR);
        result.Month = calendar.get(Calendar.MONTH);
        result.Day = calendar.get(Calendar.DATE);

        return result;
    }

    private static class DateTimeHolder {
        public int Id;
        public int Year;
        public int Month;
        public int Day;
        public int StartHour;
        public int StartMinute;
        public int EndHour;
        public int EndMinute;

        public boolean IsTimeDefined(boolean isStartTime) {
            if (isStartTime) {
                return (StartHour >= 0) && (StartMinute >= 0);
            }
            else {
                return (EndHour >= 0) && (EndMinute >= 0);
            }
        }
    }

    private static boolean GetTimeByDate(SQLiteDatabase db, DateTimeHolder dateInOutParams, boolean isStartTime) {

        boolean result = false;

        String whereClause = String.format("(%s=%s) and (%s=%s) and (%s=%s)",
                WorkTimeTable.YearColumnName, dateInOutParams.Year,
                WorkTimeTable.MonthColumnName, dateInOutParams.Month,
                WorkTimeTable.DayColumnName, dateInOutParams.Day);

        Cursor cursor = db.query(WorkTimeTable.TableName, null, whereClause, null, null, null, null);

        // ставим позицию курсора на первую строку выборки, если в выборке нет строк, вернется false
        if (cursor.moveToFirst()) {
            dateInOutParams.Id = cursor.getInt(cursor.getColumnIndex(WorkTimeTable.IdColumnName));
            dateInOutParams.StartHour = cursor.getInt(cursor.getColumnIndex(WorkTimeTable.StartHourColumnName));
            dateInOutParams.StartMinute = cursor.getInt(cursor.getColumnIndex(WorkTimeTable.StartMinuteColumnName));
            dateInOutParams.EndHour = cursor.getInt(cursor.getColumnIndex(WorkTimeTable.EndHourColumnName));
            dateInOutParams.EndMinute = cursor.getInt(cursor.getColumnIndex(WorkTimeTable.EndMinuteColumnName));

            result = true;
        }

        cursor.close();
        return result;
    }
}
