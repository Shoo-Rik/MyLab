package com.shoorik.timesheet.interfaces;

import java.util.Calendar;
import java.util.Date;

public interface IDateTimeHelper {

    String getDateString(Date date);

    String getTimeString(Date date);

    String getWeekDay(Date date);

    Calendar getCurrentCalendar(int weekNumberAgo);

    Calendar getFirstWeekDay(int weekNumberAgo);

    Calendar getLastWeekDay(int weekNumberAgo);

    Calendar getWeekDay(int weekDayNumber, int weekNumberAgo);
}
