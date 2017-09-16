package com.shoorik.timesheet;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public final class DateTimeUtil {

    public static String GetLocalDateTimeString(String formatString, Date date) {
        SimpleDateFormat format = new SimpleDateFormat(formatString, Locale.US);
        format.setTimeZone(TimeZone.getTimeZone("Europe/Moscow"));
        return format.format(date);
    }

    public static String GetCurrentDateString() {
        return GetCurrentDateString(new Date());
    }

    public static String GetCurrentWeekDay() {
        return GetCurrentWeekDay(new Date());
    }

    public static String GetCurrentDateString(Date date) {
        return GetLocalDateTimeString("yyyy-MM-dd", date);
    }

    public static String GetCurrentTimeString(Date date) {
        return GetLocalDateTimeString("HH:mm", date);
    }

    public static String GetCurrentWeekDay(Date date) {
        return GetLocalDateTimeString("EEEE", date);
    }
}
