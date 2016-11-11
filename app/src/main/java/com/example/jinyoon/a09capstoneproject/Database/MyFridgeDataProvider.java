package com.example.jinyoon.a09capstoneproject.Database;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.Nullable;

/**
 * Created by Jin Yoon on 11/7/2016.
 */

public class MyFridgeDataProvider extends ContentProvider {

    private static final int SHOPLIST = 100;
    private static final int SHOPLIST_ID = 101;
    private static final int FRIDGELIST = 200;
    private static final int FRIDGELIST_ID = 201;

    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static{
        sUriMatcher.addURI(MyFridgeDataContract.CONTENT_AUTHORITY, MyFridgeDataContract.PATH_SHOP, SHOPLIST);
        sUriMatcher.addURI(MyFridgeDataContract.CONTENT_AUTHORITY, MyFridgeDataContract.PATH_SHOP +"/#", SHOPLIST_ID);
        sUriMatcher.addURI(MyFridgeDataContract.CONTENT_AUTHORITY, MyFridgeDataContract.PATH_FRIDGE, FRIDGELIST);
        sUriMatcher.addURI(MyFridgeDataContract.CONTENT_AUTHORITY, MyFridgeDataContract.PATH_FRIDGE +"/#", FRIDGELIST_ID);

    }

    @Override
    public boolean onCreate() {
        return false;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        return null;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        final int match = sUriMatcher.match(uri);

        switch (match) {
            case SHOPLIST:
                return MyFridgeDataContract.ShopLIstEntry.CONTENT_TYPE;
            case SHOPLIST_ID:
                return MyFridgeDataContract.ShopLIstEntry.CONTENT_ITEM_TYPE;
            case FRIDGELIST:
                return MyFridgeDataContract.FridgeListEntry.CONTENT_TYPE;
            case FRIDGELIST_ID:
                return MyFridgeDataContract.FridgeListEntry.CONTENT_ITEM_TYPE;
            default:
                throw new UnsupportedOperationException("Unkown uri: " + uri);
        }


    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        return null;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }
}
