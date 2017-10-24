package com.shoorik.timesheet;

import java.util.Date;

/**
 * Created by Admin on 23.10.2017.
 */

public interface ITimeSheetStorage {

    Boolean areDayTimesSet(String weekDay);

    Date getStartTime(String weekDay);
    void setStartTime(String weekDay, Date date);

    Date getEndTime(String weekDay);
    void setEndTime(String weekDay, Date date);

    long getWeekBalance();

    void storeDateTimes();
}
