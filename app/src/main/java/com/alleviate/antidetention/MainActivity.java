package com.alleviate.antidetention;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Set;
import java.util.TreeSet;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent in = new Intent(MainActivity.this, DashboardActivity.class);
        startActivity(in);
        finish();

        SharedPreferences schedule_sp = getSharedPreferences("AntiDetention", MODE_PRIVATE);
        String first_install = schedule_sp.getString("First_Install", "Yes");

        //set_parent_alarm();

        if (first_install.equals("Yes")) {

            set_parent_alarm();
            schedule_sp.edit().putString("First_Install", "No").apply();

        }
    }

    private void set_parent_alarm() {

        int parent_id = 983545;

        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);

        Intent parent_intent = new Intent(getApplicationContext(), Scheduler.class);
        //parent_intent.putExtra(Constants.parent_alarm_id_key, parent_id);

        PendingIntent parent_pending_intent = PendingIntent.getBroadcast(getApplicationContext(), parent_id, parent_intent, 0);
        AlarmManager parent_alarm = (AlarmManager)getApplicationContext().getSystemService(Context.ALARM_SERVICE);

        parent_alarm.setInexactRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), AlarmManager.INTERVAL_DAY, parent_pending_intent);

        Log.d("AntiD:ParentAlarm", "Parent Alarm Set Id " + parent_id);
    }




}
