package com.han.jinyoon.a09capstoneproject;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.han.jinyoon.a09capstoneproject.Database.MyFridgeDataContract;
import com.han.jinyoon.a09capstoneproject.Database.MyFridgeDataHelper;

import java.util.concurrent.TimeUnit;

/**
 * Utility
 */

public class Utils {

    private static final String LOG_TAG = Utils.class.getClass().getSimpleName();


    public static void updateDaysInFridge(Context context){

//        Log.e(LOG_TAG, "update Days in Fridge Called");
        long currentTimeInMil = System.currentTimeMillis();
        SQLiteDatabase db = new MyFridgeDataHelper(context).getReadableDatabase();

        Cursor cursor =db.rawQuery("SELECT * FROM "+ MyFridgeDataContract.FridgeListEntry.TABLE_NAME, null);

        if(cursor.moveToFirst()){
            while(!cursor.isAfterLast()){
                int id = cursor.getInt(cursor.getColumnIndex(MyFridgeDataContract.FridgeListEntry._ID));
                //Test//
//                String name = cursor.getString(cursor.getColumnIndex(MyFridgeDataContract.FridgeListEntry.COLUMN_GROCERY_NAME));

                long inputTimeinMil = Long.parseLong(cursor.getString(cursor.getColumnIndex(MyFridgeDataContract.FridgeListEntry.COLUMN_INPUTDATEINMIL)));

                int oldDays = cursor.getInt(cursor.getColumnIndex(MyFridgeDataContract.FridgeListEntry.COLUMN_EXPIRATION));
                int newDays = (int) Math.ceil((long)(oldDays)-(TimeUnit.MILLISECONDS.toDays(currentTimeInMil - inputTimeinMil)));

//                Log.e(LOG_TAG, name+"=== OLD DAYS::: "+String.valueOf(oldDays)+"  NEW DAYS::: "+String.valueOf(newDays));
//                Log.e(LOG_TAG, name+"=== INPUT TIME::: "+String.valueOf(inputTimeinMil)+"  CURRENT TIME::: "+String.valueOf(currentTimeInMil));

                ContentValues cv = new ContentValues();
//                if(newDays>0){
//                    cv.put(MyFridgeDataContract.FridgeListEntry.COLUMN_EXPIRATION, newDays);
//                }else{
//                    cv.put(MyFridgeDataContract.FridgeListEntry.COLUMN_EXPIRATION, 0);
//                }
//                cv.put(MyFridgeDataContract.FridgeListEntry.COLUMN_INPUTDATEINMIL, String.valueOf(currentTimeInMil));

                if(newDays <= 0){
                    //Not showing (-) values in Days to Expiration
//                    Log.e(LOG_TAG, "NEW DAYS <= 0 ");
                    cv.put(MyFridgeDataContract.FridgeListEntry.COLUMN_EXPIRATION, 0);
                    cv.put(MyFridgeDataContract.FridgeListEntry.COLUMN_INPUTDATEINMIL, String.valueOf(currentTimeInMil));
                }else{
                    if(oldDays == newDays){
//                        Log.e(LOG_TAG, "NOTHING TO UPDATE");
                        cv.put(MyFridgeDataContract.FridgeListEntry.COLUMN_EXPIRATION, oldDays);
                        cv.put(MyFridgeDataContract.FridgeListEntry.COLUMN_INPUTDATEINMIL, String.valueOf(inputTimeinMil));
                    }else if(oldDays > newDays) {
                        //when new days is smaller, update days to expiration and input time
//                        Log.e(LOG_TAG, "OLD DAYS > NEW DAYS");
                        cv.put(MyFridgeDataContract.FridgeListEntry.COLUMN_EXPIRATION, newDays);
                        cv.put(MyFridgeDataContract.FridgeListEntry.COLUMN_INPUTDATEINMIL, String.valueOf(currentTimeInMil));
                    }else if(oldDays < newDays){
//                        Log.e(LOG_TAG, "OLD DAYS < NEW DAYS");
                        cv.put(MyFridgeDataContract.FridgeListEntry.COLUMN_EXPIRATION, newDays);
                        cv.put(MyFridgeDataContract.FridgeListEntry.COLUMN_INPUTDATEINMIL, String.valueOf(currentTimeInMil));
                    }
                }

                context.getContentResolver().update(
                        MyFridgeDataContract.FridgeListEntry.CONTENT_URI,
                        cv,
                        MyFridgeDataContract.FridgeListEntry._ID + " = ?",
                        new String[]{String.valueOf(id)}
                );
//                context.getContentResolver().notifyChange(MyFridgeDataContract.FridgeListEntry.CONTENT_URI, null);

                cursor.moveToNext();
            }
            cursor.close();
        }

    }

}
