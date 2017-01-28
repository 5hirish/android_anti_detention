package com.alleviate.antidetention;


import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;


import java.util.Calendar;

public class BootReceiver extends BroadcastReceiver{

    Context receviercontext;

    @Override
    public void onReceive(Context context, Intent intent) {

        receviercontext = context;

        if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")
                || intent.getAction().equals("android.intent.action.QUICKBOOT_POWERON")
                || intent.getAction().equals("android.intent.action.REBOOT")) {

            set_parent_alarm(context);
        }

    }

    private void set_parent_alarm(Context context) {

        int parent_id = 983545;

        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);

        Intent parent_intent = new Intent(context, Scheduler.class);
        //parent_intent.putExtra(Constants.parent_alarm_id_key, parent_id);

        PendingIntent parent_pending_intent = PendingIntent.getBroadcast(context, parent_id, parent_intent, 0);
        AlarmManager parent_alarm = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);

        parent_alarm.setInexactRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), AlarmManager.INTERVAL_DAY, parent_pending_intent);

        Log.d("AntiD:ParentAlarm", "Parent Alarm Set Id " + parent_id);
    }
}
