package com.shoorik.timesheet.client;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;

import com.shoorik.timesheet.R;
import com.shoorik.timesheet.common.DateTimeHelper;
import com.shoorik.timesheet.interfaces.IDateTimeHelper;
import com.shoorik.timesheet.common.MessageHelper;
import com.shoorik.timesheet.common.WeekDayName;
import com.shoorik.timesheet.dbconnector.DataStorageFactory;
import com.shoorik.timesheet.model.DataModel;

import java.security.InvalidParameterException;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    private IDateTimeHelper _dateTimeHelper;
    private DataModel _model;
    private int _weekNumberAgo = 0;

    public MainActivity() { }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(Thread paramThread, Throwable paramThrowable) { int i = 0; }
        });

        setContentView(R.layout.activity_main);

        // [TODO] DI
        _dateTimeHelper = new DateTimeHelper(getString(R.string.timeZone));

        _model = new DataModel(
                DataStorageFactory.GetDataStorage(this, getString(R.string.DataStorageType)),
                getResources().getInteger(R.integer.workDayDuration_hours),
                getResources().getInteger(R.integer.workDayDuration_minutes));

        UpdateAllControls();
    }

    private void UpdateAllControls() {

        UpdateHeader();

        UpdateWeekDays();

        UpdateWeekBalance();

        UpdateCurrentTimes();
    }

    private void UpdateHeader() {
        // Update header
        TextView weekHeaderView = (TextView) findViewById(R.id.periodText);
        String weekHeaderText = String.format("From %1$s To %2$s",
                _dateTimeHelper.getDateString(_dateTimeHelper.getFirstWeekDay(_weekNumberAgo).getTime()),
                _dateTimeHelper.getDateString(_dateTimeHelper.getLastWeekDay(_weekNumberAgo).getTime()));
        weekHeaderView.setText(weekHeaderText);
    }

    private void UpdateDuration(Date date, String dayName) {

        Date startTime = _model.getStartTime(date);
        Date endTime = _model.getEndTime(date);

        int id = GetDurationViewId(dayName);
        if (id == -1) {
            ShowMessage("Unknown day name '" + dayName + "'");
            return;
        }

        TextView durationView = (TextView) findViewById(id);

        if ((startTime == null) || (endTime == null)) {

            durationView.setText(R.string.unknownTime);
            return;
        }

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

    private void UpdateCurrentTimes() {

        Calendar calendar = _dateTimeHelper.getCurrentCalendar(_weekNumberAgo);

        // Update current Start Time
        Date startTime = _model.getStartTime(calendar.getTime());
        TextView startView = (TextView) findViewById(R.id.startText);
        startView.setText((_weekNumberAgo != 0) ? "N/A" :
                (startTime != null) ? _dateTimeHelper.getTimeString(startTime) : getString(R.string.unknownTime));

        // Update current End Time
        Date endTime = _model.getEndTime(calendar.getTime());
        TextView endView = (TextView) findViewById(R.id.endText);
        endView.setText((_weekNumberAgo != 0) ? "N/A" :
                (endTime != null) ? _dateTimeHelper.getTimeString(endTime) : getString(R.string.unknownTime));
    }

    private void UpdateWeekDays() {

        Calendar calendar = _dateTimeHelper.getFirstWeekDay(_weekNumberAgo);

        for (String weekDay : WeekDayName.Days) {

            UpdateSpecificWeekDay(calendar.getTime(), weekDay);
            calendar.add(Calendar.DAY_OF_YEAR, 1);
        }
    }

    private void UpdateSpecificWeekDay(Date date, String weekDay) {

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

    private void onChooseTime(final Boolean isStartTime, final int weekDayNumber) {

        final int weekNumberAgo = (weekDayNumber == 0) ? 0 : _weekNumberAgo;

        // Get Current Time
        final Calendar c = _dateTimeHelper.getCurrentCalendar(weekNumberAgo);
        int mHour = c.get(Calendar.HOUR_OF_DAY);
        int mMinute = c.get(Calendar.MINUTE);

        // Launch Time Picker Dialog
        TimePickerDialog timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

                Calendar calendar = _dateTimeHelper.getWeekDay(weekDayNumber, weekNumberAgo);
                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                calendar.set(Calendar.MINUTE, minute);
                calendar.set(Calendar.SECOND, 0);
                Date selectedDateTime = calendar.getTime();

                String weekDay = _dateTimeHelper.getWeekDay(selectedDateTime);
                String currentTimeString = _dateTimeHelper.getTimeString(selectedDateTime);

                String currentWeekDay = _dateTimeHelper.getWeekDay(_dateTimeHelper.getCurrentCalendar(weekNumberAgo).getTime());
                boolean isCurrentWeekDay = currentWeekDay.equalsIgnoreCase(weekDay);

                if (isStartTime) {
                    // Set start date time
                    _model.setStartTime(selectedDateTime);

                    if (isCurrentWeekDay) {
                        // Set current start time
                        ((TextView) findViewById(R.id.startText)).setText(currentTimeString);
                    }
                }
                else {
                    // Set end date time
                    _model.setEndTime(selectedDateTime);

                    if (isCurrentWeekDay) {
                        // Set current end time
                        ((TextView) findViewById(R.id.endText)).setText(currentTimeString);
                    }
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

    public void onClickTimeButton(View view) throws InvalidParameterException {

        int weekDayNumber;
        boolean isStartTime = false;
        switch (view.getId()) {
            case R.id.startWorkDay: weekDayNumber = 0; isStartTime = true; break;
            case R.id.endWorkDay: weekDayNumber = 0; break;
            case R.id.MondayStartText: weekDayNumber = 1; isStartTime = true; break;
            case R.id.MondayEndText: weekDayNumber = 1; break;
            case R.id.TuesdayStartText: weekDayNumber = 2; isStartTime = true; break;
            case R.id.TuesdayEndText: weekDayNumber = 2; break;
            case R.id.WednesdayStartText: weekDayNumber = 3; isStartTime = true; break;
            case R.id.WednesdayEndText: weekDayNumber = 3; break;
            case R.id.ThursdayStartText: weekDayNumber = 4; isStartTime = true; break;
            case R.id.ThursdayEndText: weekDayNumber = 4; break;
            case R.id.FridayStartText: weekDayNumber = 5; isStartTime = true; break;
            case R.id.FridayEndText: weekDayNumber = 5; break;
            case R.id.SaturdayStartText: weekDayNumber = 6; isStartTime = true; break;
            case R.id.SaturdayEndText: weekDayNumber = 6; break;
            case R.id.SundayStartText: weekDayNumber = 7; isStartTime = true; break;
            case R.id.SundayEndText: weekDayNumber = 7; break;
            default: throw new InvalidParameterException("Unknown week day number");
        }

        onChooseTime(isStartTime, weekDayNumber);
    }

    public void onClickWeek(View view) throws InvalidParameterException {

        // Get Current Time
        final Calendar c = _dateTimeHelper.getCurrentCalendar(_weekNumberAgo);
        int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH);
        int mDay = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog dlg = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                Calendar nowDate = _dateTimeHelper.getCurrentCalendar(0);
                nowDate.set(nowDate.get(Calendar.YEAR), nowDate.get(Calendar.MONTH), nowDate.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
                int nowDayOfWeek = (nowDate.get(Calendar.DAY_OF_WEEK) + 5) % 7 + 1;

                Calendar newDate = Calendar.getInstance();
                newDate.set(year, month, dayOfMonth, 0, 0, 0);
                int newDayOfWeek = (newDate.get(Calendar.DAY_OF_WEEK) + 5) % 7 + 1;

                long dayDiff = Math.round((double)(nowDate.getTimeInMillis() - newDate.getTimeInMillis())/(1000*3600*24));
                _weekNumberAgo = (int)(dayDiff / 7) + ((nowDayOfWeek < newDayOfWeek) ? 1 : 0);

                UpdateAllControls();
            }
        }, mYear, mMonth, mDay);

        dlg.getDatePicker().setMaxDate(System.currentTimeMillis());
        dlg.show();
    }

    private String GetWeekBalance() {

        long time = _model.getWeekBalance(_dateTimeHelper, _weekNumberAgo);
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
