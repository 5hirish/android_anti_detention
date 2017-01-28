package com.alleviate.antidetention;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by felix on 28/1/17.
 */

public class Scheduler extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        set_attendance_true(context);

        parentAlarmNotify(context);

    }

    private void parentAlarmNotify(Context context) {

        NotificationCompat.Builder nb= new NotificationCompat.Builder(context);
        nb.setAutoCancel(true);
        nb.setSmallIcon(R.drawable.ic_schedule_notify);
        nb.setContentTitle("Anti Detention : Scheduling the day...");
        nb.setContentText("Setting schedule for today");

        Uri alert = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        nb.setSound(alert);
        nb.setCategory(Notification.CATEGORY_STATUS);

        Intent notify_in = new Intent(context,DashboardActivity.class);

        /*TaskStackBuilder ts = TaskStackBuilder.create(context);
        ts.addParentStack(DashboardActivity.class);
        ts.addNextIntent(notify_in);*/

        //PendingIntent pendingIntent = ts.getPendingIntent(0,PendingIntent.FLAG_UPDATE_CURRENT);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, notify_in, PendingIntent.FLAG_UPDATE_CURRENT);

        nb.setContentIntent(pendingIntent);

        NotificationManager nm = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
        nm.notify(0, nb.build());

    }

    private void set_attendance_true(Context context) {

        SimpleDateFormat std_stat_fmt = new SimpleDateFormat("dd/MM/yy");

        Calendar cal = Calendar.getInstance();
        String today_date = std_stat_fmt.format(cal.getTime());
        String today_day = get_week_day(cal.get(Calendar.DAY_OF_WEEK));

        SQLiteHelper db = new SQLiteHelper(context);
        SQLiteDatabase dbr = db.getReadableDatabase();
        SQLiteDatabase dbw = db.getWritableDatabase();


        Cursor check_cursor = dbr.query(SQLiteHelper.db_stats, new String[] {SQLiteHelper.db_stats_date}, SQLiteHelper.db_stats_date+ " = ?", new String[] {today_date}, null, null, null);

        if (check_cursor.getCount() == 0) {

            Cursor schedule_cursor = dbr.query(SQLiteHelper.db_schedule, new String[] {SQLiteHelper.db_schedule_id, SQLiteHelper.db_schedule_day}, SQLiteHelper.db_schedule_day+ " = ?", new String[] {today_day}, null, null, null);

            Toast.makeText(context,""+schedule_cursor.getCount(), Toast.LENGTH_SHORT).show();

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
