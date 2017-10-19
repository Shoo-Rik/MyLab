package com.shoorik.timesheet;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        _dateTimeHelper = new DateTimeHelper(getString(R.string.timeZone));
        _settings = getSharedPreferences(getString(R.string.app_name), MODE_PRIVATE);

        Calendar calendar = _dateTimeHelper.GetLocalCalendar();

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

        // Start alarm
//        SetAlarm(30);
//        AlarmReceiver.schedule(getApplicationContext());
    }

    @Override
    protected void onPause() {
        super.onPause();

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeZone(_dateTimeHelper.GetTimeZone());

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
        UpdateWeekBalance();
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

    private void onChooseTime(final Boolean isStartTime) {

        // Get Current Time
        final Calendar c = _dateTimeHelper.GetLocalCalendar();
        int mHour = c.get(Calendar.HOUR_OF_DAY);
        int mMinute = c.get(Calendar.MINUTE);

        // Launch Time Picker Dialog
        TimePickerDialog timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

                Calendar calendar = _dateTimeHelper.GetLocalCalendar();
                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                calendar.set(Calendar.MINUTE, minute);
                calendar.set(Calendar.SECOND, 0);
                Date selectedDateTime = calendar.getTime();

                String day = _dateTimeHelper.GetCurrentWeekDay(selectedDateTime);
                String currentTimeString = _dateTimeHelper.GetCurrentTimeString(selectedDateTime);

                WeekDayInfo infoItem = _info.get(day);
                if (infoItem == null) {
                    ShowMessage("Unknown day name '" + day + "'");
                    return;
                }

                if (isStartTime) {
                    // Set start date time
                    infoItem.StartTime = selectedDateTime;
                    // Set current start time
                    ((TextView)findViewById(R.id.startText)).setText(currentTimeString);
                }
                else {
                    // Set end date time
                    infoItem.EndTime = selectedDateTime;
                    // Set current end time
                    ((TextView)findViewById(R.id.endText)).setText(currentTimeString);
                }
                // Set week day's start/end time
                ((TextView)findViewById(GetWeekDayViewId(day, isStartTime))).setText(currentTimeString);
                // Set week day's duration
                UpdateDuration(day);
                UpdateWeekBalance();
            }
        }, mHour, mMinute, true);

        timePickerDialog.show();
    }

    public void onClickStartButton(View view) {

        onChooseTime(true);

        /*// Идентификатор уведомления
        final int NOTIFY_ID = 101;

        Context context = getApplicationContext();
        Intent notificationIntent = new Intent(context, MainActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(context, NOTIFY_ID, notificationIntent, PendingIntent.FLAG_CANCEL_CURRENT);

        Notification.Builder builder = new Notification.Builder(context);
        builder.setContentIntent(contentIntent).
                setContentTitle(getString(R.string.app_name)).
                setSmallIcon(R.mipmap.app_icon).
                setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)).
                setVibrate(new long[] {500, 500, 500, 500, 500, 500});

        Notification notification = builder.build();

        NotificationManager notificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(NOTIFY_ID, notification);*/
    }

    public void onClickEndButton(View view) {

        onChooseTime(false);
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
            long minutes = (difference - hours * 3600000) / 60000;

            durationView.setText(String.format("%1$02d:%2$02d", hours, minutes));
        }
    }

    private void UpdateWeekBalance() {

        TextView weekBalance = (TextView)findViewById(R.id.WeekBalance);
        weekBalance.setText(GetWeekBalance( _info.values()));
    }

    private static String GetWeekBalance(Collection<WeekDayInfo> weekDayInfoCollection) {

        long time = 0;
        int dayCount = 0;

        for (WeekDayInfo item : weekDayInfoCollection) {

            if (item.StartTime == null || item.EndTime == null)
                continue;

            time = time + item.EndTime.getTime() - item.StartTime.getTime();
            dayCount++;
        }

        final int workDayHours = 8;
        final int workDayMinutes = 45;

        long actualTotalMinutes = time / 60000;
        long targetTotalMinutes = dayCount * (workDayHours * 60  + workDayMinutes);
        long hours = (actualTotalMinutes - targetTotalMinutes) / 60;
        long minutes = (actualTotalMinutes - targetTotalMinutes - hours * 60);

        return String.format("%1$s%2$02d:%3$02d",
                (minutes < 0) ? "-" : "",
                (hours < 0) ? -hours : hours,
                (minutes < 0) ? -minutes : minutes);
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

        MessageHelper.ShowMessage(this, message);
    }

    /*private void SetAlarm(int seconds) {

        AlarmManager alarmManager = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, MainActivity.class);
        PendingIntent alarmIntent = PendingIntent.getBroadcast(this, 0, intent, 0);

        Calendar calendar = _dateTimeHelper.GetLocalCalendar();
        calendar.setTimeInMillis(System.currentTimeMillis());
        //// TODO: use calendar.add(Calendar.SECOND,MINUTE,HOUR, int);
        calendar.add(Calendar.SECOND, seconds);

        //ALWAYS recompute the calendar after using add, set, roll
        Date date = calendar.getTime();

        alarmManager.setExact(AlarmManager.RTC_WAKEUP, date.getTime(), alarmIntent);
    }*/
}
