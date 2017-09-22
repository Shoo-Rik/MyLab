package com.shoorik.timesheet;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

public class MainActivity extends AppCompatActivity {

    private SharedPreferences _settings;
    private SharedPreferences.Editor _preferenceEditor;
    private Map<String, WeekDayInfo> _info;
    private DateTimeHelper _dateTimeHelper;

    public MainActivity() {

        _info = new HashMap<String, WeekDayInfo>();
        _info.put(WeekDayName.Monday, new WeekDayInfo(WeekDayName.Monday));
        _info.put(WeekDayName.Tuesday, new WeekDayInfo(WeekDayName.Tuesday));
        _info.put(WeekDayName.Wednesday, new WeekDayInfo(WeekDayName.Wednesday));
        _info.put(WeekDayName.Thursday, new WeekDayInfo(WeekDayName.Thursday));
        _info.put(WeekDayName.Friday, new WeekDayInfo(WeekDayName.Friday));
        _info.put(WeekDayName.Saturday, new WeekDayInfo(WeekDayName.Saturday));
        _info.put(WeekDayName.Sunday, new WeekDayInfo(WeekDayName.Sunday));
    }

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        _dateTimeHelper = new DateTimeHelper(getString(R.string.timeZone));
        _settings = getSharedPreferences(getString(R.string.app_name), MODE_PRIVATE);

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeZone(TimeZone.getTimeZone(_dateTimeHelper.GetTimeZoneString()));

        String currentWeekDay = _dateTimeHelper.GetCurrentWeekDay(calendar.getTime());

        int previousWeekDays = (calendar.get(Calendar.DAY_OF_WEEK) + 5) % 7; // hack

        for (int i = 0; i <= previousWeekDays; ++i) {

            InitializeDateTime(calendar.getTime());
            calendar.add(Calendar.DAY_OF_YEAR, -1);
        }

        TextView startView = (TextView) findViewById(R.id.startText);
        if (_info.get(currentWeekDay).StartTime != null) {
            startView.setText(_dateTimeHelper.GetCurrentTimeString(_info.get(currentWeekDay).StartTime));
        }
        TextView endView = (TextView) findViewById(R.id.endText);
        if (_info.get(currentWeekDay).EndTime != null) {
            endView.setText(_dateTimeHelper.GetCurrentTimeString(_info.get(currentWeekDay).EndTime));
        }
    }

    @Override protected void onPause() {
        super.onPause();

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeZone(TimeZone.getTimeZone(_dateTimeHelper.GetTimeZoneString()));

        _preferenceEditor = _settings.edit();

        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        int previousWeekDays = (dayOfWeek + 5) % 7; // hack

        for (int i = 0; i <= previousWeekDays; ++i) {

            StoreDateTime(calendar.getTime());
            calendar.add(Calendar.DAY_OF_YEAR, -1);
        }

        _preferenceEditor.commit();
    }

    private void InitializeDateTime(Date date) {

        String dateString = _dateTimeHelper.GetCurrentDateString(date);
        String weekDayName = _dateTimeHelper.GetCurrentWeekDay(date);

        String startId = String.format("%s-%s", dateString, getString(R.string.start));
        String endId = String.format("%s-%s", dateString, getString(R.string.end));

        long startTime = 0;
        try {
            startTime = _settings.getLong(startId, 0);
        }
        catch (ClassCastException ex) {
        }
        if (startTime > 0) {
            _info.get(weekDayName).StartTime = new Date(startTime);
        }

        long endTime = 0;
        try {
            endTime = _settings.getLong(endId, 0);
        }
        catch (ClassCastException ex) {
        }
        if (endTime > 0) {
            _info.get(weekDayName).EndTime = new Date(endTime);
        }

        String unknownTime = getString(R.string.unknownTime);

        TextView startView = (TextView) findViewById(GetWeekDayViewId(weekDayName, true));
        startView.setText(startTime > 0 ? _dateTimeHelper.GetCurrentTimeString(_info.get(weekDayName).StartTime) : unknownTime);

        TextView endView = (TextView) findViewById(GetWeekDayViewId(weekDayName, false));
        endView.setText(endTime > 0 ? _dateTimeHelper.GetCurrentTimeString(_info.get(weekDayName).EndTime) : unknownTime);

        UpdateDuration(weekDayName);
    }

    private void StoreDateTime(Date date) {

        String dateString = _dateTimeHelper.GetCurrentDateString(date);
        String weekDayName = _dateTimeHelper.GetCurrentWeekDay(date);

        WeekDayInfo dayInfo = _info.get(weekDayName);

        if (dayInfo.StartTime != null) {

            String startId = String.format("%s-%s", dateString, getString(R.string.start));
            _preferenceEditor.putLong(startId, dayInfo.StartTime.getTime());
        }

        if (dayInfo.EndTime != null) {

            String endId = String.format("%s-%s", dateString, getString(R.string.end));
            _preferenceEditor.putLong(endId, dayInfo.EndTime.getTime());
        }
    }

    private static int GetWeekDayViewId(String weekDay, boolean isStart) {

        switch (weekDay) {
            case WeekDayName.Monday:
                return isStart ? R.id.MondayStartText : R.id.MondayEndText;
            case WeekDayName.Tuesday:
                return isStart ? R.id.TuesdayStartText : R.id.TuesdayEndText;
            case WeekDayName.Wednesday:
                return isStart ? R.id.WednesdayStartText : R.id.WednesdayEndText;
            case WeekDayName.Thursday:
                return isStart ? R.id.ThursdayStartText : R.id.ThursdayEndText;
            case WeekDayName.Friday:
                return isStart ? R.id.FridayStartText : R.id.FridayEndText;
            case WeekDayName.Saturday:
                return isStart ? R.id.SaturdayStartText : R.id.SaturdayEndText;
            case WeekDayName.Sunday:
                return isStart ? R.id.SundayStartText : R.id.SundayEndText;
        }
        return 0;
    }

    public void onClickStartButton(View view) {

        Date now = new Date();
        String day = _dateTimeHelper.GetCurrentWeekDay(now);
        WeekDayInfo infoItem = _info.get(day);
        if (infoItem == null) {
            ShowMessage("Не удалось найти информацию о дне '" + day + "'");
            return;
        }

        infoItem.StartTime = now;

        String currentTimeString = _dateTimeHelper.GetCurrentTimeString(now);
        ((TextView)findViewById(R.id.startText)).setText(currentTimeString);
        ((TextView)findViewById(GetWeekDayViewId(_dateTimeHelper.GetCurrentWeekDay(), true))).setText(currentTimeString);

        UpdateDuration(day);
    }

    public void onClickEndButton(View view) {

        Date now = new Date();
        String day = _dateTimeHelper.GetCurrentWeekDay(now);
        WeekDayInfo infoItem = _info.get(day);
        if (infoItem == null) {
            ShowMessage("Не удалось найти информацию о дне '" + day + "'");
            return;
        }
        infoItem.EndTime = now;

        String currentTimeString = _dateTimeHelper.GetCurrentTimeString(now);
        ((TextView)findViewById(R.id.endText)).setText(currentTimeString);
        ((TextView)findViewById(GetWeekDayViewId(_dateTimeHelper.GetCurrentWeekDay(), false))).setText(currentTimeString);

        UpdateDuration(day);
    }

    private void UpdateDuration(String dayName) {

        WeekDayInfo itemInfo = _info.get(dayName);
        if (itemInfo == null || itemInfo.EndTime == null || itemInfo.StartTime == null)
            return;

        int id = GetDurationViewId(dayName);
        if (id == -1) {
            ShowMessage("Unknown day name '" + dayName + "'");
            return;
        }

        TextView durationView = (TextView)findViewById(id);

        long difference = itemInfo.EndTime.getTime() - itemInfo.StartTime.getTime();
        if (difference < 0) {
            durationView.setText(R.string.unknownTime);
            ShowMessage("Duration < 0");
        }
        else {
            long hours = difference / 3600000;
            long minutes = (difference - hours) / 60000;

            durationView.setText(String.format("%1$02d:%2$02d", hours, minutes));
        }
    }

    private int GetDurationViewId(String weekDayName) {

        switch (weekDayName) {
            case WeekDayName.Monday:
                return R.id.MondayDurationText;
            case WeekDayName.Tuesday:
                return R.id.TuesdayDurationText;
            case WeekDayName.Wednesday:
                return R.id.WednesdayDurationText;
            case WeekDayName.Thursday:
                return R.id.ThursdayDurationText;
            case WeekDayName.Friday:
                return R.id.FridayDurationText;
            case WeekDayName.Saturday:
                return R.id.SaturdayDurationText;
            case WeekDayName.Sunday:
                return R.id.SundayDurationText;
            default:
                return -1;
        }
    }

    private void ShowMessage(String message) {

        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }
}
