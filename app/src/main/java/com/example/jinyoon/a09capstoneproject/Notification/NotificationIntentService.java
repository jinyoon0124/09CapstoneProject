package com.example.jinyoon.a09capstoneproject.Notification;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;

import com.example.jinyoon.a09capstoneproject.MainActivity;
import com.example.jinyoon.a09capstoneproject.R;

/**
 *Notification build when intent is received
 */

public class NotificationIntentService extends IntentService{
    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    private String LOG_TAG = getClass().getSimpleName();
    private static final String ACTION_NOTIFICATION_SERVICE = "ACTION_NOTIFICATION_SERVICE";
    private static final int NOTIFICATION_ID = 1;

    public NotificationIntentService() {
        super(NotificationIntentService.class.getSimpleName());
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        String action =intent.getAction();
        if(ACTION_NOTIFICATION_SERVICE.equals(action)){
            NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
            builder.setContentTitle("TEST NOTIFICATION")
                    .setAutoCancel(true)
                    .setContentText("TEST NOTIFICATIOB CONTEXT TEXT")
                    .setSmallIcon(R.drawable.ic_fridge_black);

            PendingIntent pendingIntent = PendingIntent.getActivity(this,
                    NOTIFICATION_ID,
                    new Intent(this, MainActivity.class),
                    PendingIntent.FLAG_UPDATE_CURRENT);
            builder.setContentIntent(pendingIntent);

            NotificationManager manager = (NotificationManager)this.getSystemService(Context.NOTIFICATION_SERVICE);
            manager.notify(NOTIFICATION_ID, builder.build());
        }
    }
}
