package com.shoorik.timesheet.dbconnector.sharedpreferences;

import android.content.ContextWrapper;
import android.content.SharedPreferences;

import com.shoorik.timesheet.misc.DateTimeHelper;
import com.shoorik.timesheet.interfaces.IDataStorage;
import com.shoorik.timesheet.R;
import com.shoorik.timesheet.misc.WeekDayName;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public final class SharedPreferencesDataStorage implements IDataStorage {

    private class WeekDayInfo {

        public WeekDayInfo(String day) {
            Day = day;
        }

        public String Day;
        public Date StartTime;
        public Date EndTime;
    }

    private Map<String, WeekDayInfo> _info;

    private DateTimeHelper _dateTimeHelper;
    private SharedPreferences _settings;
    private String _startString;
    private String _endString;

    public SharedPreferencesDataStorage(ContextWrapper context) {

        _dateTimeHelper = new DateTimeHelper(context.getString(R.string.timeZone));
        _settings = context.getSharedPreferences(context.getString(R.string.app_name), context.MODE_PRIVATE);
        _startString = context.getString(R.string.start);
        _endString = context.getString(R.string.end);

        _info = new HashMap<String, WeekDayInfo>();
        _info.put(WeekDayName.Monday, new WeekDayInfo(WeekDayName.Monday));
        _info.put(WeekDayName.Tuesday, new WeekDayInfo(WeekDayName.Tuesday));
        _info.put(WeekDayName.Wednesday, new WeekDayInfo(WeekDayName.Wednesday));
        _info.put(WeekDayName.Thursday, new WeekDayInfo(WeekDayName.Thursday));
        _info.put(WeekDayName.Friday, new WeekDayInfo(WeekDayName.Friday));
        _info.put(WeekDayName.Saturday, new WeekDayInfo(WeekDayName.Saturday));
        _info.put(WeekDayName.Sunday, new WeekDayInfo(WeekDayName.Sunday));

        Calendar calendar = _dateTimeHelper.GetLocalCalendar();
        int previousWeekDays = (calendar.get(Calendar.DAY_OF_WEEK) + 5) % 7; // hack

        for (int i = 0; i <= previousWeekDays; ++i) {

            InitializeDateTime(_dateTimeHelper, calendar.getTime());
            calendar.add(Calendar.DAY_OF_YEAR, -1);
        }
    }

    public Boolean areDayTimesSet(String weekDay) {

        WeekDayInfo info = _info.get(weekDay);
        return (info != null && info.EndTime != null && info != null);
    }

    public Date getStartTime(String weekDay) {

        return _info.get(weekDay).StartTime;
    }

    public void setStartTime(String weekDay, Date date) {

        _info.get(weekDay).StartTime = date;
    }

    public Date getEndTime(String weekDay) {

        return _info.get(weekDay).EndTime;
    }

    public void setEndTime(String weekDay, Date date) {

        _info.get(weekDay).EndTime = date;
    }

    public long getWeekBalance() {

        int dayCount = 0;
        long time = 0;

        for (WeekDayInfo item : _info.values()) {

            if (item.StartTime == null || item.EndTime == null)
                continue;

            time = time + item.EndTime.getTime() - item.StartTime.getTime();
            dayCount++;
        }

        final int workDayHours = 8;
        final int workDayMinutes = 45;

        long actualTotalMinutes = time / 60000;
        long targetTotalMinutes = dayCount * (workDayHours * 60  + workDayMinutes);

        return actualTotalMinutes - targetTotalMinutes;
    }

    private void InitializeDateTime(DateTimeHelper dateTimeHelper, Date date) {

        String dateString = dateTimeHelper.GetCurrentDateString(date);
        String weekDay = dateTimeHelper.GetCurrentWeekDay(date);

        String startId = String.format("%s-%s", dateString, _startString);
        String endId = String.format("%s-%s", dateString, _endString);

        long startTime = 0;
        try {
            startTime = _settings.getLong(startId, 0);
        }
        catch (ClassCastException ex) {
        }
        if (startTime > 0) {
            setStartTime(weekDay, new Date(startTime));
        }

        long endTime = 0;
        try {
            endTime = _settings.getLong(endId, 0);
        }
        catch (ClassCastException ex) {
        }
        if (endTime > 0) {
            setEndTime(weekDay, new Date(endTime));
        }
    }

    public void save() {

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeZone(_dateTimeHelper.GetTimeZone());

        SharedPreferences.Editor preferenceEditor = _settings.edit();

        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        int previousWeekDays = (dayOfWeek + 5) % 7; // hack

        for (int i = 0; i <= previousWeekDays; ++i) {

            StoreDateTime(preferenceEditor, calendar.getTime());
            calendar.add(Calendar.DAY_OF_YEAR, -1);
        }

        preferenceEditor.commit();
    }

    private void StoreDateTime(SharedPreferences.Editor preferenceEditor, Date date) {

        String dateString = _dateTimeHelper.GetCurrentDateString(date);
        String weekDayName = _dateTimeHelper.GetCurrentWeekDay(date);

        Date startDate = getStartTime(weekDayName);
        if (startDate != null) {

            String startId = String.format("%s-%s", dateString, _startString);
            preferenceEditor.putLong(startId, startDate.getTime());
        }

        Date endDate = getEndTime(weekDayName);
        if (endDate != null) {

            String endId = String.format("%s-%s", dateString, _endString);
            preferenceEditor.putLong(endId, endDate.getTime());
        }
    }
}
