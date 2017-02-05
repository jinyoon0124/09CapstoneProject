package com.han.jinyoon.a09capstoneproject.Widget;

import android.content.Intent;
import android.database.Cursor;
import android.os.Binder;
import android.widget.AdapterView;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.han.jinyoon.a09capstoneproject.Database.MyFridgeDataContract;
import com.han.jinyoon.a09capstoneproject.R;

/**
 * Remote View Service for Widget UPdate
 */

public class BasketWidgetRemoteViewService extends RemoteViewsService {
    private final String LOG_TAG = BasketWidgetRemoteViewService.class.getSimpleName();

    private String[] mProjection = {
            MyFridgeDataContract.ShopListEntry._ID,
            MyFridgeDataContract.ShopListEntry.COLUMN_GROCERY_NAME,
            MyFridgeDataContract.ShopListEntry.COLUMN_ORDERS
    };


    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new RemoteViewsFactory() {
            private Cursor data = null;

            @Override
            public void onCreate() {

            }

            @Override
            public void onDataSetChanged() {
                if(data !=null){
                    data.close();
                }

                final long identityToken = Binder.clearCallingIdentity();

                data = getContentResolver().query(
                        MyFridgeDataContract.ShopListEntry.CONTENT_URI,
                        mProjection,
                        null,
                        null,
                        null);

                Binder.restoreCallingIdentity(identityToken);

            }

            @Override
            public void onDestroy() {
                if(data !=null){
                    data.close();
                    data=null;
                }
            }

            @Override
            public int getCount() {
                return data==null? 0: data.getCount();
            }

            @Override
            public RemoteViews getViewAt(int position) {
                if(position == AdapterView.INVALID_POSITION ||
                        data == null || !data.moveToPosition(position)){
                    return null;
                }
                RemoteViews views = new RemoteViews(getPackageName(), R.layout.widget_basket_list_item);
                String basketItemName = data.getString(data.getColumnIndex(MyFridgeDataContract.ShopListEntry.COLUMN_GROCERY_NAME));
//                Log.e(LOG_TAG, "!!!!!!!ADDING WIDGET" + basketItemName);
                views.setTextViewText(R.id.widget_basket_item_name, basketItemName);

                final Intent fillInIntent = new Intent();
                fillInIntent.setData(MyFridgeDataContract.ShopListEntry.CONTENT_URI);
                views.setOnClickFillInIntent(R.id.widget_list_item, fillInIntent);

                return views;
            }

            @Override
            public RemoteViews getLoadingView() {
                return new RemoteViews(getPackageName(), R.layout.widget_basket_list_item);
            }

            @Override
            public int getViewTypeCount() {
                return 1;
            }

            @Override
            public long getItemId(int position) {
                if(data.moveToPosition(position))
                    return data.getLong(data.getColumnIndex(MyFridgeDataContract.ShopListEntry._ID));
                return position;
            }

            @Override
            public boolean hasStableIds() {
                return true;
            }
        };
    }
}
