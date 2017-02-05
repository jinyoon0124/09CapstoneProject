package com.han.jinyoon.a09capstoneproject.Database;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by Jin Yoon on 11/6/2016.
 */

public final class MyFridgeDataContract {

    public static final String CONTENT_AUTHORITY = "com.example.jinyoon.a09capstoneproject";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_SHOP = "shop";
    public static final String PATH_FRIDGE = "fridge";


    public static final class ShopListEntry implements BaseColumns{

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_SHOP).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE+"/"+CONTENT_AUTHORITY+"/"+PATH_SHOP;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE+"/"+CONTENT_AUTHORITY+"/"+PATH_SHOP;

        public static final String TABLE_NAME = "shoplist";
        public static final String COLUMN_GROCERY_NAME = "name";
        public static final String COLUMN_ORDERS = "orders";

        //Add any method related to database here
        public static Uri buildShopListUri(long id){
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
        public static Uri buildShopListUriwithName(String name){
            return CONTENT_URI.buildUpon().appendPath(name).build();
        }

    }

    public static final class FridgeListEntry implements BaseColumns{

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_FRIDGE).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE+"/"+CONTENT_AUTHORITY+"/"+PATH_FRIDGE;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE+"/"+CONTENT_AUTHORITY+"/"+PATH_FRIDGE;

        public static final String TABLE_NAME = "fridgelist";
        public static final String COLUMN_GROCERY_NAME = "name";
        public static final String COLUMN_EXPIRATION = "expiration";
        public static final String COLUMN_INPUTDATEINMIL = "timestamp";

        //Add any method related to database here
        public static Uri buildFridgeListUri(long id){
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
        public static Uri buildFridgeListUriwithName(String name){
            return CONTENT_URI.buildUpon().appendPath(name).build();
        }

    }


}
