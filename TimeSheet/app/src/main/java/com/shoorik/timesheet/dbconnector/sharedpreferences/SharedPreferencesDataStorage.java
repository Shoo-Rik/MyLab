package com.shoorik.timesheet.dbconnector.sharedpreferences;

import android.content.ContextWrapper;
import android.content.SharedPreferences;

import com.shoorik.timesheet.common.DateTimeHelper;
import com.shoorik.timesheet.interfaces.IDateTimeHelper;
import com.shoorik.timesheet.interfaces.IDataStorage;
import com.shoorik.timesheet.R;

import java.util.Date;

public final class SharedPreferencesDataStorage implements IDataStorage {

    private IDateTimeHelper _dateTimeHelper;
    private SharedPreferences _settings;
    private String _startString;
    private String _endString;

    public SharedPreferencesDataStorage(ContextWrapper context) {

        // [TODO] DI
        _dateTimeHelper = new DateTimeHelper(context.getString(R.string.timeZone));
        _settings = context.getSharedPreferences(context.getString(R.string.app_name), context.MODE_PRIVATE);
        _startString = context.getString(R.string.start);
        _endString = context.getString(R.string.end);
    }

    @Override
    public Date getStartTime(Date startDate) {

        return getDateTime(startDate, true);
    }

    @Override
    public void setStartTime(Date startDateTime) {

        setDateTime(startDateTime, true);
    }

    @Override
    public Date getEndTime(Date endDate) {

        return getDateTime(endDate, false);
    }

    @Override
    public void setEndTime(Date endDateTime) {

        setDateTime(endDateTime, false);
    }

    private Date getDateTime(Date date, boolean isStartTime) {

        String dateString = _dateTimeHelper.getDateString(date);
        String id = String.format("%s-%s", dateString, isStartTime ? _startString : _endString);

        long time = 0;
        try {
            time = _settings.getLong(id, 0);
        }
        catch (ClassCastException ex) {
        }

        return (time > 0) ? new Date(time) : null;
    }

    private void setDateTime(Date date, boolean isStartTime) {

        if (date == null)
            return;

        String dateString = _dateTimeHelper.getDateString(date);
        String id = String.format("%s-%s", dateString, isStartTime ? _startString : _endString);

        SharedPreferences.Editor preferenceEditor = _settings.edit();

        preferenceEditor.putLong(id, date.getTime());

        preferenceEditor.commit();
    }
}
