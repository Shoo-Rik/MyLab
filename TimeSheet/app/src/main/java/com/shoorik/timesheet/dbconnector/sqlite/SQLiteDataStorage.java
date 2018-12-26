package com.shoorik.timesheet.dbconnector.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.shoorik.timesheet.R;
import com.shoorik.timesheet.common.DateTimeHelper;
import com.shoorik.timesheet.dbconnector.sqlite.dto.WorkTimeTable;
import com.shoorik.timesheet.interfaces.IDataStorage;

import java.sql.Time;
import java.util.Calendar;
import java.util.Date;

public class SQLiteDataStorage implements IDataStorage {

    private SQLiteDBHelper _dbHelper;
    private DateTimeHelper _dateTimeHelper;

    public SQLiteDataStorage(Context context) {

        // создаем объект для создания и управления версиями БД
        _dbHelper = new SQLiteDBHelper(context);

        _dateTimeHelper = new DateTimeHelper(context.getString(R.string.timeZone));
    }

    private Date GetValue(Date date, boolean isStartTime) {

        Date result = null;

        DateTimeHolder dateInOutParams = GetDateTimeHolder(date);

        // подключаемся к БД
        SQLiteDatabase db = _dbHelper.getWritableDatabase();

        if (GetTimesByDate(db, dateInOutParams, 1)) {

            Time resultTime = isStartTime ? dateInOutParams.StartTime : dateInOutParams.EndTime;

            Calendar calendarDate = Calendar.getInstance();
            calendarDate.setTime(date);
            Calendar calendarTime = Calendar.getInstance();
            calendarTime.setTimeInMillis(resultTime.getTime());

            calendarDate.set(calendarDate.get(Calendar.YEAR), calendarDate.get(Calendar.MONTH), calendarDate.get(Calendar.DATE),
                    calendarTime.get(Calendar.HOUR), calendarTime.get(Calendar.MINUTE), calendarTime.get(Calendar.SECOND));
            result = calendarDate.getTime();
        }
        // закрываем подключение к БД
        _dbHelper.close();

        return result;
    }

    private void SetValue(Date dateTime, boolean isStartTime) {

        // создаем объект для данных
        ContentValues cv = new ContentValues();

        // подготовим данные для вставки в виде пар: наименование столбца - значение
        cv.put(WorkTimeTable.DateColumnName, _dateTimeHelper.getDateString(dateTime));

        if (isStartTime) {
            cv.put(WorkTimeTable.StartTimeColumnName, _dateTimeHelper.getTimeString(dateTime));
        }
        else {
            cv.put(WorkTimeTable.EndTimeColumnName, _dateTimeHelper.getTimeString(dateTime));
        }

        DateTimeHolder dateInOutParams = GetDateTimeHolder(dateTime);

        // подключаемся к БД
        SQLiteDatabase db = _dbHelper.getWritableDatabase();

        if (GetTimesByDate(db, dateInOutParams, 1)) {

            int rowCount = db.update(WorkTimeTable.TableName, cv, null, null);

            Time startDate = dateInOutParams.StartTime;
            Time endDate = dateInOutParams.EndTime;
        }
        else {

            // вставляем запись и получаем ее ID
            long rowID = db.insert(WorkTimeTable.TableName, null, cv);
        }

        // закрываем подключение к БД
        _dbHelper.close();
    }

    private static DateTimeHolder GetDateTimeHolder(Date date) {

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        DateTimeHolder dateInOutParams = new DateTimeHolder();
        dateInOutParams.Year = calendar.get(Calendar.YEAR);
        dateInOutParams.Month = calendar.get(Calendar.MONTH);
        dateInOutParams.Day = calendar.get(Calendar.DATE);

        return dateInOutParams;
    }

    private static class DateTimeHolder {
        public int Id;
        public int Year;
        public int Month;
        public int Day;
        public Time StartTime;
        public Time EndTime;
    }

    private boolean GetTimesByDate(SQLiteDatabase db, DateTimeHolder dateInOutParams, int intervalInDays) {

        boolean result = true;

        Calendar c = Calendar.getInstance();
        c.set(dateInOutParams.Year, dateInOutParams.Month, dateInOutParams.Day, 0, 0, 0);
        Date currentDay = c.getTime();
        c.add(Calendar.DATE, intervalInDays);
        Date nextDay = c.getTime();

        String whereClause = String.format("(%s>='%s') and (%s<'%s')",
                WorkTimeTable.DateColumnName,
                _dateTimeHelper.getDateString(currentDay),
                WorkTimeTable.DateColumnName,
                _dateTimeHelper.getDateString(nextDay));

        Cursor cursor = db.query(WorkTimeTable.TableName, null, whereClause, null, null, null, null);

        // ставим позицию курсора на первую строку выборки
        // если в выборке нет строк, вернется false
        if (cursor.moveToFirst()) {

            // определяем номера столбцов по имени в выборке
            int idColIndex = cursor.getColumnIndex(WorkTimeTable.IdColumnName);
            //int dateColIndex = cursor.getColumnIndex(WorkTimeTable.DateColumnName);
            int startTimeColIndex = cursor.getColumnIndex(WorkTimeTable.StartTimeColumnName);
            int endTimeColIndex = cursor.getColumnIndex(WorkTimeTable.EndTimeColumnName);

            dateInOutParams.Id = cursor.getInt(idColIndex);
            dateInOutParams.StartTime = Time.valueOf(cursor.getString(startTimeColIndex));
            dateInOutParams.EndTime = Time.valueOf(cursor.getString(endTimeColIndex));
        }
        else {
            result = false;
        }
        cursor.close();

        return result;
    }

    @Override
    public Date getStartTime(Date startDate) {

        return GetValue(startDate, true);
    }

    @Override
    public Date getEndTime(Date endDate) {

        return GetValue(endDate, false);
    }

    @Override
    public void setStartTime(Date startDateTime) {

        SetValue(startDateTime, true);
    }

    @Override
    public void setEndTime(Date endDateTime) {

        SetValue(endDateTime, false);
    }
}
