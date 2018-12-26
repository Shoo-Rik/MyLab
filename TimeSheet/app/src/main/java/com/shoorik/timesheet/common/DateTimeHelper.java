package com.shoorik.timesheet.common;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public final class DateTimeHelper {

    private String _timeZone;

    public DateTimeHelper(String timeZone) {

        if (timeZone == null) {
            throw new NullPointerException("timeZone");
        }
        _timeZone = timeZone;
    }

    private TimeZone getTimeZone() {

        return TimeZone.getTimeZone(_timeZone);
    }

    private String getLocalDateTimeString(String formatString, Date date) {

        SimpleDateFormat format = new SimpleDateFormat(formatString, Locale.US);
        format.setTimeZone(TimeZone.getTimeZone(_timeZone));
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

    public Calendar getLocalCalendar() {

        return Calendar.getInstance(getTimeZone());
    }

    public Calendar getFirstWeekDay() {

        Calendar calendar = getLocalCalendar();
        int previousWeekDays = (calendar.get(Calendar.DAY_OF_WEEK) + 5) % 7;
        calendar.add(Calendar.DAY_OF_YEAR, -previousWeekDays);
        return calendar;
    }

    public Calendar getLastWeekDay() {

        Calendar calendar = getFirstWeekDay();
        calendar.add(Calendar.DAY_OF_YEAR, 6);
        return calendar;
    }
}
