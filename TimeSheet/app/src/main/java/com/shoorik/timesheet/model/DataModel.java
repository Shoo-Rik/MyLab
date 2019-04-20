package com.shoorik.timesheet.model;

import com.shoorik.timesheet.interfaces.IDateTimeHelper;
import com.shoorik.timesheet.common.WeekDayName;
import com.shoorik.timesheet.interfaces.IDataStorage;

import java.util.Calendar;
import java.util.Date;

public class DataModel {

    private IDataStorage _dataStorage;
    private int _workDayHours;
    private int _workDayMinutes;

    public DataModel(IDataStorage dataStorage, int workDayHours, int workDayMinutes) {

        _dataStorage = dataStorage;
        _workDayHours = workDayHours;
        _workDayMinutes = workDayMinutes;
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

    public long getWeekBalance(IDateTimeHelper dateTimeHelper, int weekNumberAgo) {

        int dayCount = 0;
        long time = 0;

        Calendar calendar = dateTimeHelper.getFirstWeekDay(weekNumberAgo);

        for (String dayName : WeekDayName.Days) {

            Date startTime = getStartTime(calendar.getTime());
            Date endTime = getEndTime(calendar.getTime());

            if ((startTime != null) && (endTime != null)) {

                time = time + endTime.getTime() - startTime.getTime();
                dayCount++;
            }
            calendar.add(Calendar.DAY_OF_YEAR, 1);
        }

        long actualTotalMinutes = time / 60000;
        long targetTotalMinutes = dayCount * (_workDayHours * 60  + _workDayMinutes);

        return actualTotalMinutes - targetTotalMinutes;
    }
}
