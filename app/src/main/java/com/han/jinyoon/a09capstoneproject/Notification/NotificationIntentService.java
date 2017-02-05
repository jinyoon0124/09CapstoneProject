package com.han.jinyoon.a09capstoneproject.Notification;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.han.jinyoon.a09capstoneproject.Database.MyFridgeDataContract.*;
import com.han.jinyoon.a09capstoneproject.MainActivity;
import com.han.jinyoon.a09capstoneproject.R;
import com.han.jinyoon.a09capstoneproject.Utils;


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

            Log.e(LOG_TAG, "INsIDE NOTIFICAITON INTENT.. UPDATING THE DATE");
            Context context = getApplicationContext();
            Utils.updateDaysInFridge(context);
//
//            SQLiteDatabase db = new MyFridgeDataHelper(context).getReadableDatabase();
//            Cursor cursor =db.rawQuery("SELECT * FROM "+ FridgeListEntry.TABLE_NAME, null);
//
//            if(cursor.moveToFirst()){
//                while(!cursor.isAfterLast()){
//                    int id = cursor.getInt(cursor.getColumnIndex(FridgeListEntry._ID));
//
//                    long inputTimeinMil = Long.parseLong(cursor.getString(cursor.getColumnIndex(FridgeListEntry.COLUMN_INPUTDATEINMIL)));
//
////                    Log.e("!!!!!!!!!inputTime ", String.valueOf(inputTimeinMil));
//
//                    int newDays = (int) Math.ceil((long)(cursor.getInt(cursor.getColumnIndex(FridgeListEntry.COLUMN_EXPIRATION)))-(TimeUnit.MILLISECONDS.toDays(currentTimeinMil - inputTimeinMil)));
//
////                    Log.e("!!!!!!!!!!!newDay", String.valueOf(newDays));
//
//                    ContentValues cv = new ContentValues();
//                    if(newDays>0){
//                        cv.put(FridgeListEntry.COLUMN_EXPIRATION, newDays);
//                    }else{
//                        cv.put(FridgeListEntry.COLUMN_EXPIRATION, 0);
//                    }
//
//                    cv.put(FridgeListEntry.COLUMN_INPUTDATEINMIL, String.valueOf(currentTimeinMil));
//
//                    context.getContentResolver().update(
//                            FridgeListEntry.CONTENT_URI,
//                            cv,
//                            FridgeListEntry._ID + " = ?",
//                            new String[]{String.valueOf(id)}
//                    );
//                    context.getContentResolver().notifyChange(FridgeListEntry.CONTENT_URI, null);
//
//                    cursor.moveToNext();
//
//                }
//            }


            Cursor cursor = context.getContentResolver().query(
                    FridgeListEntry.CONTENT_URI,
                    null,
                    FridgeListEntry.COLUMN_EXPIRATION +" <= ? ",
                    new String[]{"1"},
                    null
            );

            if(cursor !=null && cursor.getCount()!=0){
                NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
                builder.setContentTitle(context.getString(R.string.app_name))
                        .setAutoCancel(true)
                        .setContentText(getString(R.string.notification_msg))
                        .setSmallIcon(R.drawable.ic_fridge_black);

                Intent mainActivityIntent = new Intent(this, MainActivity.class);
                mainActivityIntent.putExtra(this.getString(R.string.activity_from_notification_key), true);

                PendingIntent pendingIntent = PendingIntent.getActivity(this,
                        NOTIFICATION_ID,
                        mainActivityIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT);
                builder.setContentIntent(pendingIntent);

                NotificationManager manager = (NotificationManager)this.getSystemService(Context.NOTIFICATION_SERVICE);
                manager.notify(NOTIFICATION_ID, builder.build());
                cursor.close();

            }else{
                Log.e(LOG_TAG, "No item is about to expire");
            }
        }
    }
}
