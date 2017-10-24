package com.shoorik.timesheet;

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

    public TimeZone GetTimeZone() {

        return TimeZone.getTimeZone(_timeZone);
    }

    public String GetLocalDateTimeString(String formatString, Date date) {

        SimpleDateFormat format = new SimpleDateFormat(formatString, Locale.US);
        format.setTimeZone(TimeZone.getTimeZone(_timeZone));
        return format.format(date);
    }

    public String GetCurrentDateString(Date date) {

        return GetLocalDateTimeString("yyyy-MM-dd", date);
    }

    public String GetCurrentTimeString(Date date) {

        return GetLocalDateTimeString("HH:mm", date);
    }

    public String GetCurrentWeekDay(Date date) {

        return GetLocalDateTimeString("EEEE", date);
    }

    public Calendar GetLocalCalendar() {

        return Calendar.getInstance(GetTimeZone());
    }
}
