package com.shoorik.timesheet.dbconnector.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.shoorik.timesheet.interfaces.IDataStorage;
import java.util.Date;

public class SQLiteDataStorage implements IDataStorage {

    private SQLiteDBHelper _dbHelper;

    public SQLiteDataStorage(Context context) {

        // создаем объект для создания и управления версиями БД
        _dbHelper = new SQLiteDBHelper(context);

/*        // подключаемся к БД
        SQLiteDatabase db = SQLiteDbHelper.getWritableDatabase();

        // создаем объект для данных
        ContentValues cv = new ContentValues();

        // подготовим данные для вставки в виде пар: наименование столбца - значение

        cv.put("time", _dateTimeHelper.GetCurrentDateString(_dateTimeHelper.GetLocalCalendar().getTime()));
        cv.put("type", 123);
        // вставляем запись и получаем ее ID
        long rowID = db.insert("WorkTime", null, cv);

        // делаем запрос всех данных из таблицы mytable, получаем Cursor
        Cursor c = db.query("WorkTime", null, null, null, null, null, null);

        // ставим позицию курсора на первую строку выборки
        // если в выборке нет строк, вернется false
        if (c.moveToFirst()) {

            // определяем номера столбцов по имени в выборке
            int idColIndex = c.getColumnIndex("id");
            int timeColIndex = c.getColumnIndex("time");
            int typeColIndex = c.getColumnIndex("type");

            do {
                // получаем значения по номерам столбцов и пишем все в лог
                String s1 = "ID = " + c.getInt(idColIndex);
                String s2 = ", timeColIndex = " + c.getString(timeColIndex);
                String s3 = ", typeColIndex = " + c.getString(typeColIndex);
                // переход на следующую строку
                // а если следующей нет (текущая - последняя), то false - выходим из цикла
            } while (c.moveToNext());
        }
        c.close();

        // закрываем подключение к БД
        SQLiteDbHelper.close();
*/
    }

    @Override
    public Boolean areDayTimesSet(String weekDay) {
        return null;
    }

    @Override
    public Date getStartTime(String weekDay) {
        return null;
    }

    @Override
    public void setStartTime(String weekDay, Date date) {

    }

    @Override
    public Date getEndTime(String weekDay) {
        return null;
    }

    @Override
    public void setEndTime(String weekDay, Date date) {

    }

    @Override
    public long getWeekBalance() {
        return 0;
    }

    @Override
    public void save() {

    }
}
