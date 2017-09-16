package com.shoorik.timesheet;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private static final String PREFS_FILE = "TimeSheet";
    private static final String PREFS_START_TIME = "Start";
    private static final String PREF_END_TIME = "End";

    private SharedPreferences _settings;
    private SharedPreferences.Editor _preferenceEditor;
    private Map<String, WeekDayInfo> _info;

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

        _settings = getSharedPreferences(PREFS_FILE, MODE_PRIVATE);

        String date = DateTimeUtil.GetCurrentDateString();

        String startId = String.format("%s-%s", date, PREFS_START_TIME);
        String endId = String.format("%s-%s", date, PREF_END_TIME);

        TextView startView = (TextView) findViewById(R.id.startText);
        String startString = _settings.getString(startId, null);
        startView.setText(startString);

        TextView endView = (TextView) findViewById(R.id.endText);
        String endString = _settings.getString(endId, null);
        endView.setText(endString);
    }

    @Override
    protected void onPause() {

        super.onPause();

        String date = DateTimeUtil.GetCurrentDateString();

        String startId = String.format("%s-%s", date, PREFS_START_TIME);
        String endId = String.format("%s-%s", date, PREF_END_TIME);

        _preferenceEditor = _settings.edit();

        TextView startView = (TextView) findViewById(R.id.startText);
        String startString = startView.getText().toString();
        _preferenceEditor.putString(startId, startView.getText().toString());

        TextView endView = (TextView) findViewById(R.id.endText);
        String endString = endView.getText().toString();
        _preferenceEditor.putString(endId, endView.getText().toString());

        _preferenceEditor.commit();
    }

    private int GetWeekDayViewId(String weekDay, boolean isStart) {

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
        String day = DateTimeUtil.GetCurrentWeekDay(now);
        WeekDayInfo infoItem = _info.get(day);
        if (infoItem == null) {
            ShowMessage("Не удалось найти информацию о дне '" + day + "'");
            return;
        }

        infoItem.StartTime = now;

        String currentTimeString = DateTimeUtil.GetCurrentTimeString(now);
        ((TextView)findViewById(R.id.startText)).setText(currentTimeString);
        ((TextView)findViewById(GetWeekDayViewId(DateTimeUtil.GetCurrentWeekDay(), true))).setText(" " + currentTimeString + " ");

        UpdateDuration(day);
    }

    public void onClickEndButton(View view) {

        Date now = new Date();
        String day = DateTimeUtil.GetCurrentWeekDay(now);
        WeekDayInfo infoItem = _info.get(day);
        if (infoItem == null) {
            ShowMessage("Не удалось найти информацию о дне '" + day + "'");
            return;
        }
        infoItem.EndTime = now;

        String currentTimeString = DateTimeUtil.GetCurrentTimeString(now);
        ((TextView)findViewById(R.id.endText)).setText(currentTimeString);
        ((TextView)findViewById(GetWeekDayViewId(DateTimeUtil.GetCurrentWeekDay(), false))).setText(" " + currentTimeString + " ");

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

            durationView.setText(String.format(" %1$02d:%2$02d ", hours, minutes));
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
