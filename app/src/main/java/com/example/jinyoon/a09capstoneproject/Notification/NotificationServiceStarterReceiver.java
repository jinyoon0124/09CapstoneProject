package com.example.jinyoon.a09capstoneproject.Notification;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Receives BOOT_COMPLETED, TIMEZONECHANGED, TIME_SET event to re-setup AlarmManager
 */

public class NotificationServiceStarterReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        NotificationEventReceiver.setupAlarm(context);
    }
}
