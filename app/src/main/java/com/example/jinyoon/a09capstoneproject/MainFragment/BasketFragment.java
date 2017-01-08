package com.example.jinyoon.a09capstoneproject.MainFragment;


import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.example.jinyoon.a09capstoneproject.Database.MyFridgeDataContract.*;
import com.example.jinyoon.a09capstoneproject.Database.MyFridgeDataHelper;
import com.example.jinyoon.a09capstoneproject.ItemTouchHelper.SimpleItemTouchHelperCallback;
import com.example.jinyoon.a09capstoneproject.MyApplication;
import com.example.jinyoon.a09capstoneproject.R;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

/**
 * A simple {@link Fragment} subclass.
 */
public class BasketFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>{

    private final String LOG_TAG = this.getClass().getSimpleName();
    private Context mContext;
    private Cursor mCursor;
    private TextView mEmptyView;
    private BasketRecyclerViewAdapter mCursorAdapter;
    private static final int CURSOR_LOADER_ID = 0;
    private static final int TOUCH_HELPER_ID = 1;
    private String[] mProjection = {
            ShopListEntry._ID,
            ShopListEntry.COLUMN_GROCERY_NAME,
            ShopListEntry.COLUMN_ORDERS
    };
    public static final String ACTION_DATA_UPDATED = "com.example.jinyoon.a09capstoneproject.ACTION_DATA_UPDATED";

    private RecyclerView mRecyclerView;

    public BasketFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Log.e(LOG_TAG, "BASKET ONCREATEVIEW");


        // Inflate the layout for this fragment
        mContext=getContext();
        View view = inflater.inflate(R.layout.fragment_basket, container, false);

        //EmptyView Handling
        mEmptyView = (TextView) view.findViewById(R.id.basket_empty_textview);
        mEmptyView.setVisibility(View.GONE);

        if(mCursorAdapter==null){
//            Log.e(LOG_TAG, "BASKET CURSOR ADAPTER NULL");

            mEmptyView.setVisibility(View.VISIBLE);
            mEmptyView.setText(getString(R.string.basket_empty_msg));
        }

        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_basket);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));

        mCursorAdapter = new BasketRecyclerViewAdapter(mContext, null);
        mRecyclerView.setAdapter(mCursorAdapter);
        getLoaderManager().initLoader(CURSOR_LOADER_ID,null,this);


        //Item Touch Helper Implementation
        ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback(mCursorAdapter, mRecyclerView);
        ItemTouchHelper mItemTouchHelper = new ItemTouchHelper(callback);
        mItemTouchHelper.attachToRecyclerView(mRecyclerView);


        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.basket_fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new MaterialDialog.Builder(getContext()).title(getString(R.string.basket_dialog_title))
                        .inputType(InputType.TYPE_CLASS_TEXT)
                        .input(getString(R.string.basket_dialog_item_hint), "", new MaterialDialog.InputCallback() {
                            @Override
                            public void onInput(@NonNull MaterialDialog dialog, CharSequence input) {
                                Cursor c = getContext().getContentResolver().query(
                                        ShopListEntry.CONTENT_URI,
                                        new String[]{ShopListEntry.COLUMN_GROCERY_NAME},
                                        ShopListEntry.COLUMN_GROCERY_NAME +" = ? ",
                                        new String[]{input.toString()},
                                        null);
                                if(c!=null &&c.getCount()!=0){
                                    Toast toast =
                                        Toast.makeText(mContext, getString(R.string.item_exist_msg), Toast.LENGTH_SHORT);
                                    toast.setGravity(Gravity.CENTER, Gravity.CENTER, 0);
                                    toast.show();
                                }else if(input.toString().equals("")){
                                    Toast toast =
                                            Toast.makeText(mContext, getString(R.string.no_input_msg), Toast.LENGTH_SHORT);
                                    toast.setGravity(Gravity.CENTER, Gravity.CENTER, 0);
                                    toast.show();

                                }else{
//                                    SQLiteDatabase db = new MyFridgeDataHelper(mContext).getReadableDatabase();
//                                    c = db.rawQuery("SELECT * FROM "+ ShopListEntry.TABLE_NAME, null);
//                                    Log.e("!!!! INSIDE DIALOG!! C!", String.valueOf(c.getCount()));
//                                    int itemOrder = c.getCount();

                                    ContentValues cv = new ContentValues();
                                    cv.put(ShopListEntry.COLUMN_GROCERY_NAME, input.toString());
//                                    cv.put(ShopListEntry.COLUMN_ORDERS, itemOrder);

                                    mContext.getContentResolver().insert(ShopListEntry.CONTENT_URI, cv);

                                    mContext.getContentResolver().notifyChange(ShopListEntry.CONTENT_URI, null);
                                    Toast.makeText(mContext, getString(R.string.item_added_msg, input.toString()), Toast.LENGTH_SHORT).show();
                                }

                        c.close();
                            }
                        }).negativeText(R.string.basket_dialog_negative_text)
                        .show();

            }
        });
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        //Get tracker.
        Tracker tracker = ((MyApplication) getActivity().getApplication()).getTracker();

        //Set screen name
        tracker.setScreenName(LOG_TAG);
        //Send a screen view.
        tracker.send(new HitBuilders.ScreenViewBuilder().build());
    }

    @Override
    public void onResume() {
        Log.e(LOG_TAG, "BASKET ONRESUME");

        super.onResume();
        getLoaderManager().restartLoader(CURSOR_LOADER_ID, null, this);
    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Log.e(LOG_TAG, "BASKET ONCREATELOADER");
//        CursorLoader loader = new CursorLoader(mContext,
//                ShopListEntry.CONTENT_URI,
//                mProjection,
//                null,
//                null,
//                ShopListEntry.COLUMN_ORDERS+ " ASC"
//                );
        return new CursorLoader(mContext,
                ShopListEntry.CONTENT_URI,
                mProjection,
                null,
                null,
                ShopListEntry.COLUMN_ORDERS+ " ASC"
        );

    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        Log.e(LOG_TAG, "BASKET ONLOADERFINISHED");

        updateWidgets();

        mCursorAdapter.swapCursor(cursor);
        mCursor=cursor;
        int adapterSize = mCursorAdapter.getItemCount();

        if(adapterSize!=0){
            mEmptyView.setVisibility(View.GONE);
        }else{
            mEmptyView.setVisibility(View.VISIBLE);
            mEmptyView.setText(getString(R.string.basket_empty_msg));
//            Log.e(LOG_TAG, "SET VISIBLITY VISIBLE");

        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

        mCursorAdapter.swapCursor(null);
    }

    public void updateWidgets(){
        Intent dataUpdatedIntent = new Intent();
        dataUpdatedIntent.setAction(ACTION_DATA_UPDATED);
        mContext.sendBroadcast(dataUpdatedIntent);
        Log.e(LOG_TAG, "WIDGET UPDATE METHOD CALLED");
    }

}
