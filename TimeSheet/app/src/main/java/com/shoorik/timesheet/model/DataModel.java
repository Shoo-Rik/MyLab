package com.shoorik.timesheet.model;

import android.content.ContextWrapper;

import com.shoorik.timesheet.common.DateTimeHelper;
import com.shoorik.timesheet.common.WeekDayName;
import com.shoorik.timesheet.dbconnector.DataStorageFactory;
import com.shoorik.timesheet.dbconnector.DataStorageType;
import com.shoorik.timesheet.interfaces.IDataStorage;

import java.util.Calendar;
import java.util.Date;

public class DataModel {

    IDataStorage _dataStorage;

    public DataModel(ContextWrapper context) {

        // [TODO] Remove hardcoded data storage type SharedPreferences
        _dataStorage = DataStorageFactory.GetDataStorage(context, DataStorageType.SharedPreferences);
    }

    public Date getStartTime(Date startDate) {

        return _dataStorage.getStartTime(startDate);
    }

    public Date getEndTime(Date endDate) {

        return _dataStorage.getEndTime(endDate);
    }

    public void setStartTime(Date startDateTime) {

        _dataStorage.setStartTime(startDateTime);
    }

    public void setEndTime(Date endDateTime) {

        _dataStorage.setEndTime(endDateTime);
    }

    public long getWeekBalance(DateTimeHelper dateTimeHelper) {

        int dayCount = 0;
        long time = 0;

        Calendar calendar = dateTimeHelper.getFirstWeekDay();

        for (String dayName : WeekDayName.Days) {

            Date startTime = getStartTime(calendar.getTime());
            Date endTime = getEndTime(calendar.getTime());

            if ((startTime == null) || (endTime == null))
                continue;

            time = time + endTime.getTime() - startTime.getTime();

            calendar.add(Calendar.DAY_OF_YEAR, 1);
            dayCount++;
        }

        final int workDayHours = 8;
        final int workDayMinutes = 45;

        long actualTotalMinutes = time / 60000;
        long targetTotalMinutes = dayCount * (workDayHours * 60  + workDayMinutes);

        return actualTotalMinutes - targetTotalMinutes;
    }
}
