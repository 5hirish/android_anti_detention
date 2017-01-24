package com.alleviate.antidetention;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent in = new Intent(MainActivity.this, DashboardActivity.class);
        startActivity(in);
        finish();


        set_attendance_true();
    }

    private void set_attendance_true() {

        SimpleDateFormat std_stat_fmt = new SimpleDateFormat("dd/MM/yy");

        Calendar cal = Calendar.getInstance();
        String today_date = std_stat_fmt.format(cal.getTime());
        String today_day = get_week_day(cal.get(Calendar.DAY_OF_WEEK));

        SQLiteHelper db = new SQLiteHelper(getApplicationContext());
        SQLiteDatabase dbr = db.getReadableDatabase();
        SQLiteDatabase dbw = db.getWritableDatabase();


        Cursor check_cursor = dbr.query(SQLiteHelper.db_stats, new String[] {SQLiteHelper.db_stats_date}, SQLiteHelper.db_stats_date+ " = ?", new String[] {today_date}, null, null, null);

        if (check_cursor.getCount() == 0) {

            Cursor schedule_cursor = dbr.query(SQLiteHelper.db_schedule, new String[] {SQLiteHelper.db_schedule_id, SQLiteHelper.db_schedule_day}, SQLiteHelper.db_schedule_day+ " = ?", new String[] {today_day}, null, null, null);

            Toast.makeText(getApplicationContext(),""+schedule_cursor.getCount(), Toast.LENGTH_SHORT).show();

            if(schedule_cursor != null){
                while (schedule_cursor.moveToNext()){

                    int sid = schedule_cursor.getInt(schedule_cursor.getColumnIndex(SQLiteHelper.db_schedule_id));
                    String sday = schedule_cursor.getString(schedule_cursor.getColumnIndex(SQLiteHelper.db_schedule_day));

                    ContentValues insert_attendance = new ContentValues();
                    insert_attendance.put(SQLiteHelper.db_stats_lecture_id, sid);
                    insert_attendance.put(SQLiteHelper.db_stats_date, today_date);
                    insert_attendance.put(SQLiteHelper.db_stats_status, "True");

                    long resid = dbw.insert(SQLiteHelper.db_stats, null , insert_attendance);

                    Log.d("AntiDetention:Database","Default Attendance Inserted "+resid);

                }
                schedule_cursor.close();
            }

        }

        check_cursor.close();

        dbw.close();
        dbr.close();
        db.close();

    }

    private String get_week_day(int day) {
        String str_day = "Sunday";
        switch (day){
            case 1:
                str_day = "Sunday";
                break;
            case 2:
                str_day = "Monday";
                break;
            case 3:
                str_day = "Tuesday";
                break;
            case 4:
                str_day = "Wednesday";
                break;
            case 5:
                str_day = "Thursday";
                break;
            case 6:
                str_day = "Friday";
                break;
            case 7:
                str_day = "Saturday";
                break;
        }
        return str_day;
    }
}
