package com.example.jinyoon.a09capstoneproject.Database;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

/**
 * Created by Jin Yoon on 11/7/2016.
 */

public class MyFridgeDataProvider extends ContentProvider {

    private MyFridgeDataHelper mDbHelper;
    private static final int SHOPLIST = 100;
    private static final int SHOPLIST_NAME = 101;
    private static final int FRIDGELIST = 200;
    private static final int FRIDGELIST_NAME = 201;

    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static{
        sUriMatcher.addURI(MyFridgeDataContract.CONTENT_AUTHORITY, MyFridgeDataContract.PATH_SHOP, SHOPLIST);
        sUriMatcher.addURI(MyFridgeDataContract.CONTENT_AUTHORITY, MyFridgeDataContract.PATH_SHOP +"/*", SHOPLIST_NAME);
        sUriMatcher.addURI(MyFridgeDataContract.CONTENT_AUTHORITY, MyFridgeDataContract.PATH_FRIDGE, FRIDGELIST);
        sUriMatcher.addURI(MyFridgeDataContract.CONTENT_AUTHORITY, MyFridgeDataContract.PATH_FRIDGE +"/*", FRIDGELIST_NAME);

    }

    private static final String sShopItemWithNameSelection =
            MyFridgeDataContract.ShopListEntry.TABLE_NAME+
                    "."+ MyFridgeDataContract.ShopListEntry.COLUMN_GROCERY_NAME + " = ? ";
    private static final String sFridgeItemWithNameSelection =
            MyFridgeDataContract.FridgeListEntry.TABLE_NAME+
                    "."+ MyFridgeDataContract.FridgeListEntry.COLUMN_GROCERY_NAME + " = ? ";

    @Override
    public boolean onCreate() {
        mDbHelper = new MyFridgeDataHelper(getContext());
        return true;
    }

    @Override
    public String getType(Uri uri) {
        final int match = sUriMatcher.match(uri);

        switch (match) {
            case SHOPLIST:
                return MyFridgeDataContract.ShopListEntry.CONTENT_TYPE;
            case SHOPLIST_NAME:
                return MyFridgeDataContract.ShopListEntry.CONTENT_ITEM_TYPE;
            case FRIDGELIST:
                return MyFridgeDataContract.FridgeListEntry.CONTENT_TYPE;
            case FRIDGELIST_NAME:
                return MyFridgeDataContract.FridgeListEntry.CONTENT_ITEM_TYPE;
            default:
                throw new UnsupportedOperationException("Unkown uri: " + uri);
        }


    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        Cursor retCursor;
        switch (sUriMatcher.match(uri)){
            case SHOPLIST_NAME:{
                String name = uri.getPathSegments().get(1);
                selection = sShopItemWithNameSelection;
                selectionArgs = new String[]{name};
                retCursor = mDbHelper.getReadableDatabase().query(
                        MyFridgeDataContract.ShopListEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            }
            case FRIDGELIST_NAME:{
                String name = uri.getPathSegments().get(1);
                selection = sFridgeItemWithNameSelection;
                selectionArgs = new String[]{name};
                retCursor = mDbHelper.getReadableDatabase().query(
                        MyFridgeDataContract.FridgeListEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            }

            case SHOPLIST:{
                retCursor = mDbHelper.getReadableDatabase().query(
                        MyFridgeDataContract.ShopListEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            }

            case FRIDGELIST:{
                retCursor = mDbHelper.getReadableDatabase().query(
                        MyFridgeDataContract.FridgeListEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: "+ uri);

        }
        //DONT FORGET THIS LINE EVER!!!! TO LISTEN CHANGE IN DATABASE!!!
        retCursor.setNotificationUri(getContext().getContentResolver(), uri);
        return  retCursor;

    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        Uri retUri;
        final int match = sUriMatcher.match(uri);
        final SQLiteDatabase db = mDbHelper.getWritableDatabase();

        switch (match){
            case SHOPLIST:{
                long _id = db.insert(MyFridgeDataContract.ShopListEntry.TABLE_NAME, null, values);
                if(_id>0){
                    retUri = MyFridgeDataContract.ShopListEntry.buildShopListUri(_id);
                }
                else
                    throw new SQLException("Failed to insert row into "+ uri);
                break;
            }
            case FRIDGELIST:{
                long _id = db.insert(MyFridgeDataContract.FridgeListEntry.TABLE_NAME, null, values);
                if(_id>0){
                    retUri = MyFridgeDataContract.FridgeListEntry.buildFridgeListUri(_id);
                }
                else
                    throw new SQLException("Failed to insert row into "+uri);
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);

        return retUri;

    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mDbHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int rowDeleted;
        if(null== selection ) selection ="1";
        switch (match){
            case SHOPLIST:{
                rowDeleted = db.delete(
                        MyFridgeDataContract.ShopListEntry.TABLE_NAME, selection, selectionArgs);
                break;
            }
            case FRIDGELIST:{
                rowDeleted = db.delete(
                        MyFridgeDataContract.FridgeListEntry.TABLE_NAME, selection, selectionArgs);
                break;
            }
            default:
                throw new UnsupportedOperationException("Unkonw url: "+uri);

        }
        if(rowDeleted !=0){
            getContext().getContentResolver().notifyChange(uri,null);
        }

        return rowDeleted;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        int rowUpdated;
        final SQLiteDatabase db = mDbHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);

        switch (match){
            case SHOPLIST:{
                rowUpdated = db.update(MyFridgeDataContract.ShopListEntry.TABLE_NAME, values, selection, selectionArgs);
                break;
            }
            case FRIDGELIST:{
                rowUpdated = db.update(MyFridgeDataContract.FridgeListEntry.TABLE_NAME, values, selection, selectionArgs);
                break;
            }
            default:
                throw new UnsupportedOperationException("Unkown uri : "+ uri);
        }

        if(rowUpdated!=0){
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return rowUpdated;
    }


}
