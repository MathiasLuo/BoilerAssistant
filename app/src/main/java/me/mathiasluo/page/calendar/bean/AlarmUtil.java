package me.mathiasluo.page.calendar.bean;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import java.util.Calendar;

/**
 * Created by mathiasluo on 16-4-8.
 */
public class AlarmUtil {

    public final static void addAlarm(Context context, Class<?> cls, String title, String time, Calendar startCalendar) {
        Intent intent = new Intent(context, AlarReceiver.class);
        intent.putExtra("title", title);
        intent.putExtra("time", time);
        PendingIntent sender = PendingIntent.getBroadcast(
                context, 0, intent, 0);
        // Schedule the alarm!
        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        am.set(AlarmManager.RTC_WAKEUP, startCalendar.getTimeInMillis(), sender);
    }


    public final static void cancelAlarm(Context context, Class<?> cls, String title, String time, Calendar startCalendar) {
        // Create the same intent, and thus a matching IntentSender, for
        // the one that was scheduled.
        Intent intent = new Intent(context, AlarReceiver.class);
        intent.putExtra("title", title);
        intent.putExtra("time", time);
        PendingIntent sender = PendingIntent.getBroadcast(
                context, 0, intent, 0);
        // And cancel the alarm.
        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        am.cancel(sender);
    }


}
