package com.shoorik.timesheet.common;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public final class DateTimeHelper {

    private TimeZone _timeZone;

    public DateTimeHelper(String timeZone) {

        if (timeZone == null) {
            throw new NullPointerException("timeZone");
        }
        _timeZone = TimeZone.getTimeZone(timeZone);
    }

    private String getLocalDateTimeString(String formatString, Date date) {

        SimpleDateFormat format = new SimpleDateFormat(formatString, Locale.US);
        format.setTimeZone(_timeZone);
        return format.format(date);
    }

    public String getDateString(Date date) {

        return getLocalDateTimeString("yyyy-MM-dd", date);
    }

    public String getTimeString(Date date) {

        return getLocalDateTimeString("HH:mm", date);
    }

    public String getWeekDay(Date date) {

        return getLocalDateTimeString("EEEE", date);
    }

    public Calendar getCurrentCalendar(int weekNumberAgo) {

        Calendar result = Calendar.getInstance(_timeZone);
        result.add(Calendar.WEEK_OF_YEAR, -weekNumberAgo);
        return result;
    }

    public Calendar getFirstWeekDay(int weekNumberAgo) {

        Calendar calendar = getCurrentCalendar(weekNumberAgo);
        int previousWeekDays = (calendar.get(Calendar.DAY_OF_WEEK) + 5) % 7;
        calendar.add(Calendar.DAY_OF_YEAR, -previousWeekDays);
        return calendar;
    }

    public Calendar getLastWeekDay(int weekNumberAgo) {

        Calendar calendar = getFirstWeekDay(weekNumberAgo);
        calendar.add(Calendar.DAY_OF_YEAR, 6);
        return calendar;
    }

    public Calendar getWeekDay(int weekDayNumber, int weekNumberAgo) {

        if (weekDayNumber == 0) {
            return getCurrentCalendar(weekNumberAgo);
        }
        else {
            Calendar calendar = getFirstWeekDay(weekNumberAgo);
            calendar.add(Calendar.DAY_OF_YEAR, weekDayNumber - 1);
            return calendar;
        }
    }
}
