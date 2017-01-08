package com.example.jinyoon.a09capstoneproject.Notification;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.util.TimeUtils;
import android.util.Log;
import android.widget.Toast;

import com.example.jinyoon.a09capstoneproject.Database.MyFridgeDataContract.*;
import com.example.jinyoon.a09capstoneproject.Database.MyFridgeDataHelper;
import com.example.jinyoon.a09capstoneproject.MainActivity;
import com.example.jinyoon.a09capstoneproject.R;

import java.util.concurrent.TimeUnit;


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
            //update all expiration based on current time... make sure to round up  numbers
            long currentTimeinMil = System.currentTimeMillis();


            Context context = getApplicationContext();

            SQLiteDatabase db = new MyFridgeDataHelper(context).getReadableDatabase();
            Cursor cursor =db.rawQuery("SELECT * FROM "+ FridgeListEntry.TABLE_NAME, null);

            if(cursor.moveToFirst()){
                while(!cursor.isAfterLast()){
                    int id = cursor.getInt(cursor.getColumnIndex(FridgeListEntry._ID));

                    long inputTimeinMil = Long.parseLong(cursor.getString(cursor.getColumnIndex(FridgeListEntry.COLUMN_INPUTDATEINMIL)));

                    Log.e("!!!!!!!!!inputTime ", String.valueOf(inputTimeinMil));

                    int newDays = (int) Math.ceil((long)(cursor.getInt(cursor.getColumnIndex(FridgeListEntry.COLUMN_EXPIRATION)))-(TimeUnit.MILLISECONDS.toDays(currentTimeinMil - inputTimeinMil)));

                    Log.e("!!!!!!!!!!!newDay", String.valueOf(newDays));

                    ContentValues cv = new ContentValues();
                    if(newDays>0){
                        cv.put(FridgeListEntry.COLUMN_EXPIRATION, newDays);
                    }else{
                        cv.put(FridgeListEntry.COLUMN_EXPIRATION, 0);
                    }

                    cv.put(FridgeListEntry.COLUMN_INPUTDATEINMIL, String.valueOf(currentTimeinMil));

                    context.getContentResolver().update(
                            FridgeListEntry.CONTENT_URI,
                            cv,
                            FridgeListEntry._ID + " = ?",
                            new String[]{String.valueOf(id)}
                    );
                    context.getContentResolver().notifyChange(FridgeListEntry.CONTENT_URI, null);

                    cursor.moveToNext();

                }
            }


            cursor = context.getContentResolver().query(
                    FridgeListEntry.CONTENT_URI,
                    null,
                    FridgeListEntry.COLUMN_EXPIRATION +" <= ? ",
                    new String[]{"1"},
                    null
            );

            if(cursor ==null || cursor.getCount()!=0){
                NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
                builder.setContentTitle(context.getString(R.string.app_name))
                        .setAutoCancel(true)
                        .setContentText(getString(R.string.notification_msg))
                        .setSmallIcon(R.drawable.ic_fridge_black);

                PendingIntent pendingIntent = PendingIntent.getActivity(this,
                        NOTIFICATION_ID,
                        new Intent(this, MainActivity.class),
                        PendingIntent.FLAG_UPDATE_CURRENT);
                builder.setContentIntent(pendingIntent);

                NotificationManager manager = (NotificationManager)this.getSystemService(Context.NOTIFICATION_SERVICE);
                manager.notify(NOTIFICATION_ID, builder.build());
            }else{
                Log.e(LOG_TAG, "No item is about to expire");
            }


            cursor.close();
        }
    }
}
