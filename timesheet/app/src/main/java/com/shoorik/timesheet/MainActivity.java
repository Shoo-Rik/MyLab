package com.shoorik.timesheet;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    private static final String PREFS_FILE = "TimeSheet";
    private static final String PREFS_START_TIME = "Start";
    private static final String PREF_END_TIME = "End";
    SharedPreferences settings;
    SharedPreferences.Editor prefEditor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        settings = getSharedPreferences(PREFS_FILE, MODE_PRIVATE);

        String date = GetDateString();

        String startId = String.format("%s-%s", date, PREFS_START_TIME);
        String endId = String.format("%s-%s", date, PREF_END_TIME);

        TextView startView = (TextView) findViewById(R.id.startText);
        String startString = settings.getString(startId, null);
        startView.setText(startString);

        TextView endView = (TextView) findViewById(R.id.endText);
        String endString = settings.getString(endId, null);
        endView.setText(endString);
    }

    @Override
    protected void onPause() {
        super.onPause();

        String date = GetDateString();

        String startId = String.format("%s-%s", date, PREFS_START_TIME);
        String endId = String.format("%s-%s", date, PREF_END_TIME);

        prefEditor = settings.edit();

        TextView startView = (TextView) findViewById(R.id.startText);
        String startString = startView.getText().toString();
        // сохраняем в настройках
        prefEditor.putString(startId, startView.getText().toString());

        TextView endView = (TextView) findViewById(R.id.endText);
        String endString = endView.getText().toString();
        // сохраняем в настройках
        prefEditor.putString(endId, endView.getText().toString());

        prefEditor.commit();
    }

    private static String GetDateString()
    {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        return df.format(Calendar.getInstance().getTime());
    }

    private static String GetTimeString()
    {
        DateFormat df = new SimpleDateFormat("hh:mm");
        return df.format(Calendar.getInstance().getTime());
    }
    private int _currentWorkTimeInMinutes = 0;
    private final int TotalWeekTimeInMinutes = 2400;
    private java.util.Date _startWorkTimeToday;
    private java.util.Date _endWorkTimeToday;

    public void onClickEndButton(View view) {
        ((TextView)findViewById(R.id.endText)).setText(GetTimeString());
    }

    public void onClickStartButton(View view) {
        ((TextView)findViewById(R.id.startText)).setText(GetTimeString());
    }
}
