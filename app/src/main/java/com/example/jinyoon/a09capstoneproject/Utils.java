package com.example.jinyoon.a09capstoneproject;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.jinyoon.a09capstoneproject.Database.MyFridgeDataContract;
import com.example.jinyoon.a09capstoneproject.Database.MyFridgeDataHelper;

import java.util.concurrent.TimeUnit;

/**
 * Utility
 */

public class Utils {

    public static void updateDaysInFridge(Context context){

//        Log.e("UTILS", "update Days in Fridge Called");
        long currentTimeInMil = System.currentTimeMillis();
        SQLiteDatabase db = new MyFridgeDataHelper(context).getReadableDatabase();

        Cursor cursor =db.rawQuery("SELECT * FROM "+ MyFridgeDataContract.FridgeListEntry.TABLE_NAME, null);

        if(cursor.moveToFirst()){
            while(!cursor.isAfterLast()){
                int id = cursor.getInt(cursor.getColumnIndex(MyFridgeDataContract.FridgeListEntry._ID));
                //Test//
//                String name = cursor.getString(cursor.getColumnIndex(MyFridgeDataContract.FridgeListEntry.COLUMN_GROCERY_NAME));

                long inputTimeinMil = Long.parseLong(cursor.getString(cursor.getColumnIndex(MyFridgeDataContract.FridgeListEntry.COLUMN_INPUTDATEINMIL)));

                int newDays = (int) Math.ceil((long)(cursor.getInt(cursor.getColumnIndex(MyFridgeDataContract.FridgeListEntry.COLUMN_EXPIRATION)))-(TimeUnit.MILLISECONDS.toDays(currentTimeInMil - inputTimeinMil)));

//                Log.e("UTILS ", name+" "+String.valueOf(newDays));

                ContentValues cv = new ContentValues();
                if(newDays>0){
                    cv.put(MyFridgeDataContract.FridgeListEntry.COLUMN_EXPIRATION, newDays);
                }else{
                    cv.put(MyFridgeDataContract.FridgeListEntry.COLUMN_EXPIRATION, 0);
                }

                cv.put(MyFridgeDataContract.FridgeListEntry.COLUMN_INPUTDATEINMIL, String.valueOf(currentTimeInMil));

                context.getContentResolver().update(
                        MyFridgeDataContract.FridgeListEntry.CONTENT_URI,
                        cv,
                        MyFridgeDataContract.FridgeListEntry._ID + " = ?",
                        new String[]{String.valueOf(id)}
                );
                context.getContentResolver().notifyChange(MyFridgeDataContract.FridgeListEntry.CONTENT_URI, null);

                cursor.moveToNext();
            }
            cursor.close();
        }

    }

}
