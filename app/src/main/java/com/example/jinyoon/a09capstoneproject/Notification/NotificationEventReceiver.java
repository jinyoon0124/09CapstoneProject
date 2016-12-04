package com.example.jinyoon.a09capstoneproject.Notification;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.util.Log;

import java.util.Calendar;
import java.util.Date;

/**
 * Receive Notificaiton Intent
 */

public class NotificationEventReceiver extends WakefulBroadcastReceiver{
    private final String LOG_TAG = getClass().getSimpleName();
    private static final String ACTION_NOTIFICATION_SERVICE = "ACTION_NOTIFICATION_SERVICE";


    //Alarm for Notification
    public static void setupAlarm(Context context) {

        //get trigger time (current time)
        //TODO: In order the trigger time to be set at 4:00pm for example, use code in comment
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
//        calendar.setTimeInMillis(System.currentTimeMillis());
//        calendar.set(Calendar.HOUR_OF_DAY, 16);
//        calendar.set(Calendar.MINUTE, 30);
        long triggerAt = calendar.getTimeInMillis();

        //set up pending intent
        Intent intent = new Intent(context, NotificationEventReceiver.class);
        intent.setAction(ACTION_NOTIFICATION_SERVICE);
        PendingIntent notificationPendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        //set up alarmManager
        //TODO: In order to trigger everyday at 4:30 pm, use AlarmManager.INTERVAL_DAY for repeat
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,
                triggerAt,
                1000*60,
                notificationPendingIntent
        );

    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        Intent serviceIntent = null;
        if(ACTION_NOTIFICATION_SERVICE.equals(action)){
            Log.i(LOG_TAG, "onReceive from alarm, starting notificaiton");
            serviceIntent = new Intent(context, NotificationIntentService.class);
            serviceIntent.setAction(ACTION_NOTIFICATION_SERVICE);
        }

        if(serviceIntent !=null){
            startWakefulService(context, serviceIntent);
        }
    }
}
