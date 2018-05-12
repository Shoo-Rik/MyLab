package com.shoorik.timesheet.interfaces;

import java.util.Date;

public interface IDataStorage {

    Boolean areDayTimesSet(String weekDay);

    Date getStartTime(String weekDay);
    void setStartTime(String weekDay, Date date);

    Date getEndTime(String weekDay);
    void setEndTime(String weekDay, Date date);

    long getWeekBalance();

    void save();
}
