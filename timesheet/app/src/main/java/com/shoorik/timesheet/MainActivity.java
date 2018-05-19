package com.shoorik.timesheet;

import android.app.TimePickerDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.TimePicker;

import com.shoorik.timesheet.common.DateTimeHelper;
import com.shoorik.timesheet.common.MessageHelper;
import com.shoorik.timesheet.common.WeekDayName;
import com.shoorik.timesheet.model.DataModel;

import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    private DateTimeHelper _dateTimeHelper;
    private DataModel _model;

    public MainActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        _dateTimeHelper = new DateTimeHelper(getString(R.string.timeZone));
        _model = new DataModel(this);

        Calendar calendar = _dateTimeHelper.getFirstWeekDay();

        for (String weekDay : WeekDayName.Days) {

            InitializeDateTime(calendar.getTime(), weekDay);
            calendar.add(Calendar.DAY_OF_YEAR, 1);
        }

        calendar = _dateTimeHelper.getLocalCalendar();

        Date startTime = _model.getStartTime(calendar.getTime());
        TextView startView = (TextView) findViewById(R.id.startText);
        startView.setText((startTime != null) ? _dateTimeHelper.getTimeString(startTime) : getString(R.string.unknownTime));

        Date endTime = _model.getEndTime(calendar.getTime());
        TextView endView = (TextView) findViewById(R.id.endText);
        endView.setText((endTime != null) ? _dateTimeHelper.getTimeString(endTime) : getString(R.string.unknownTime));

        UpdateWeekBalance();
    }

    @Override
    protected void onPause() {

        super.onPause();
 //       _model.save();
    }

    private void InitializeDateTime(Date date, String weekDay) {

        Date startTime = _model.getStartTime(date);
        Date endTime = _model.getEndTime(date);

        String unknownTime = getString(R.string.unknownTime);

        TextView startView = (TextView) findViewById(GetWeekDayViewId(weekDay, true));
        startView.setText(startTime != null ? _dateTimeHelper.getTimeString(startTime) : unknownTime);

        TextView endView = (TextView) findViewById(GetWeekDayViewId(weekDay, false));
        endView.setText(endTime != null ? _dateTimeHelper.getTimeString(endTime) : unknownTime);

        UpdateDuration(date, weekDay);
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
        final Calendar c = _dateTimeHelper.getLocalCalendar();
        int mHour = c.get(Calendar.HOUR_OF_DAY);
        int mMinute = c.get(Calendar.MINUTE);

        // Launch Time Picker Dialog
        TimePickerDialog timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

                Calendar calendar = _dateTimeHelper.getLocalCalendar();
                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                calendar.set(Calendar.MINUTE, minute);
                calendar.set(Calendar.SECOND, 0);
                Date selectedDateTime = calendar.getTime();

                String weekDay = _dateTimeHelper.getWeekDay(selectedDateTime);
                String currentTimeString = _dateTimeHelper.getTimeString(selectedDateTime);

                if (isStartTime) {
                    // Set start date time
                    _model.setStartTime(selectedDateTime);
                    // Set current start time
                    ((TextView)findViewById(R.id.startText)).setText(currentTimeString);
                }
                else {
                    // Set end date time
                    _model.setEndTime(selectedDateTime);
                    // Set current end time
                    ((TextView)findViewById(R.id.endText)).setText(currentTimeString);
                }
                // Set week day's start/end time
                ((TextView)findViewById(GetWeekDayViewId(weekDay, isStartTime))).setText(currentTimeString);
                // Set week day's duration
                UpdateDuration(selectedDateTime, weekDay);
                UpdateWeekBalance();
            }
        }, mHour, mMinute, true);

        timePickerDialog.show();
    }

    public void onClickStartButton(View view) {

        onChooseTime(true);
    }

    public void onClickEndButton(View view) {

        onChooseTime(false);
    }

    private void UpdateDuration(Date date, String dayName) {

        Date startTime = _model.getStartTime(date);
        Date endTime = _model.getEndTime(date);

        if ((startTime == null) || (endTime == null))
            return;

        int id = GetDurationViewId(dayName);
        if (id == -1) {
            ShowMessage("Unknown day name '" + dayName + "'");
            return;
        }

        TextView durationView = (TextView) findViewById(id);

        long difference = endTime.getTime() - startTime.getTime();
        if (difference < 0) {

            durationView.setText(R.string.unknownTime);
            ShowMessage("Duration < 0");

        } else {

            long hours = difference / 3600000;
            long minutes = (difference % 3600000) / 60000;

            durationView.setText(String.format("%1$02d:%2$02d", hours, minutes));
        }
    }

    private void UpdateWeekBalance() {

        TextView weekBalance = (TextView)findViewById(R.id.WeekBalance);
        weekBalance.setText(GetWeekBalance());
    }

    private String GetWeekBalance() {

        long time = _model.getWeekBalance(_dateTimeHelper);
        long hours = time / 60;
        long minutes = time % 60;

        return String.format("%1$s%2$02d:%3$02d",
                (time < 0) ? "-" : "",
                (time < 0) ? -hours : hours,
                (time < 0) ? -minutes : minutes);
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
}
