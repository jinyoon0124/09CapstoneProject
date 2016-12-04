package com.example.jinyoon.a09capstoneproject.Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.example.jinyoon.a09capstoneproject.Database.MyFridgeDataContract.*;

/**
 * Created by Jin Yoon on 11/6/2016.
 */

public class MyFridgeDataHelper extends SQLiteOpenHelper{

    private static final int DATABASE_VERSION = 1;

    static final String DATABASE_NAME = "myfridge.db";

    public MyFridgeDataHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        final String SQL_CREATE_SHOPLIST_TABLE = "CREATE TABLE " +
                ShopListEntry.TABLE_NAME + " (" +
                ShopListEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                ShopListEntry.COLUMN_GROCERY_NAME + " TEXT UNIQUE NOT NULL, " +
                ShopListEntry.COLUMN_ORDERS + " INTEGER "+
                " );";

        final String SQL_CREATE_FRIDGELIST_TABLE = "CREATE TABLE "+
                FridgeListEntry.TABLE_NAME + " (" +
                FridgeListEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                FridgeListEntry.COLUMN_GROCERY_NAME + " TEXT UNIQUE NOT NULL, "+
                FridgeListEntry.COLUMN_EXPIRATION + " INTEGER NOT NULL, "+
                FridgeListEntry.COLUMN_INPUTDATEINMIL + " INTEGER NOT NULL "+
                " );";

        db.execSQL(SQL_CREATE_SHOPLIST_TABLE);
        db.execSQL(SQL_CREATE_FRIDGELIST_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + ShopListEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + FridgeListEntry.TABLE_NAME);
        onCreate(db);
    }
}
