package com.example.jinyoon.a09capstoneproject.MainFragment;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.example.jinyoon.a09capstoneproject.Database.MyFridgeDataContract.*;
import com.example.jinyoon.a09capstoneproject.ItemTouchHelper.SimpleItemTouchHelperCallback;
import com.example.jinyoon.a09capstoneproject.MyApplication;
import com.example.jinyoon.a09capstoneproject.R;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import java.util.HashSet;
import java.util.Set;

/**
 * A placeholder fragment containing a simple view.
 */
public class FridgeFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>{

    private final String LOG_TAG = this.getClass().getSimpleName();
    private RecyclerView mRecyclerView;
    private Context mContext;
    private Cursor mCursor;
    private TextView mEmptyView;
    private FridgeRecyclerViewAdapter mCursorAdapter;
    private static final int CURSOR_LOADER_ID = 1;

    private String[] mProjection = {
            FridgeListEntry._ID,
            FridgeListEntry.COLUMN_GROCERY_NAME,
            FridgeListEntry.COLUMN_EXPIRATION
    };

    public FridgeFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

//        Log.e(LOG_TAG, "FRIDGE ONCREATEVIEW");

        mContext=getContext();
        View view = inflater.inflate(R.layout.fragment_fridge, container, false);

        mEmptyView = (TextView) view.findViewById(R.id.fridge_empty_textview);
        mEmptyView.setVisibility(View.GONE);

        if(mCursorAdapter ==null){
//            Log.e(LOG_TAG, "CRSOR ADAPTER NULL");
            mEmptyView.setVisibility(View.VISIBLE);
            mEmptyView.setText(getString(R.string.fridge_empty_msg));
        }

        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_fridge);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));

        mCursorAdapter = new FridgeRecyclerViewAdapter(mContext, null);
        mRecyclerView.setAdapter(mCursorAdapter);
        getLoaderManager().initLoader(CURSOR_LOADER_ID, null, this);

        ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback(mCursorAdapter, mRecyclerView);
        final ItemTouchHelper mItemTouchHelper = new ItemTouchHelper(callback);
        mItemTouchHelper.attachToRecyclerView(mRecyclerView);

        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.fridge_fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view1) {
                final MaterialDialog dialog = new MaterialDialog.Builder(mContext).title(getString(R.string.fridge_dialog_title))
                        .customView(R.layout.dialog_fridge, true)
                        .negativeText(getString(R.string.fridge_dialog_negative_text))
                        .positiveText(getString(R.string.fridge_dialog_positive_text))
                        .build();

                dialog.show();
                final View dialogView = dialog.getCustomView();

                View positive = dialog.getActionButton(DialogAction.POSITIVE);
                positive.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String itemName = String.valueOf(((EditText)dialogView.findViewById(R.id.fridge_dialog_name_input)).getText());
                        String dayValue = String.valueOf(((EditText)dialogView.findViewById(R.id.fridge_dialog_days_input)).getText());
                        Cursor c = getContext().getContentResolver().query(
                                FridgeListEntry.CONTENT_URI,
                                new String[]{FridgeListEntry.COLUMN_GROCERY_NAME},
                                FridgeListEntry.COLUMN_GROCERY_NAME +" = ? ",
                                new String[]{itemName},
                                null);

                        if(c!=null &&c.getCount()!=0){
                            Toast toast =
                                    Toast.makeText(mContext, getString(R.string.item_exist_msg), Toast.LENGTH_SHORT);
                            toast.setGravity(Gravity.CENTER, Gravity.CENTER, 0);
                            toast.show();
                        }else if(itemName.equals("")){
                            Toast toast =
                                    Toast.makeText(mContext, getString(R.string.no_input_msg), Toast.LENGTH_SHORT);
                            toast.setGravity(Gravity.CENTER, Gravity.CENTER, 0);
                            toast.show();

                        }else if(dayValue.equals("")){
                            Toast toast =
                                    Toast.makeText(mContext, getString(R.string.no_days_input_msg), Toast.LENGTH_SHORT);
                            toast.setGravity(Gravity.CENTER, Gravity.CENTER, 0);
                            toast.show();

                        }
                        else{
                            ContentValues cv = new ContentValues();

                            long currentTime = System.currentTimeMillis();
                            cv.put(FridgeListEntry.COLUMN_GROCERY_NAME, itemName);
                            cv.put(FridgeListEntry.COLUMN_EXPIRATION, Integer.parseInt(dayValue));
                            cv.put(FridgeListEntry.COLUMN_INPUTDATEINMIL,  String.valueOf(currentTime));

                            mContext.getContentResolver().insert(FridgeListEntry.CONTENT_URI, cv);
                            mContext.getContentResolver().notifyChange(FridgeListEntry.CONTENT_URI, null);

                        }
                        c.close();

                        dialog.dismiss();
                    }
                });
                View negative = dialog.getActionButton(DialogAction.NEGATIVE);
                negative.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
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
        super.onResume();
//        Log.e(LOG_TAG, "FRIDGE ONRESUME");
        getLoaderManager().restartLoader(CURSOR_LOADER_ID, null, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
//        Log.e(LOG_TAG, "FRIDGE ONCREATELOADER");

        return new CursorLoader(mContext,
                FridgeListEntry.CONTENT_URI,
                mProjection,
                null,
                null,
                null
        );
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
//        Log.e(LOG_TAG, "FRIDGE ONCLOADFINISHED");

        mCursorAdapter.swapCursor(cursor);
        mCursor =cursor;
        int adapterSize = mCursorAdapter.getItemCount();
//        Log.e(LOG_TAG, "FRIDGE LOADER SIZE"+String.valueOf(adapterSize));

        if(adapterSize!=0){
            mEmptyView.setVisibility(View.GONE);
        }else{
            mEmptyView.setVisibility(View.VISIBLE);
//            Log.e(LOG_TAG, "SET VISIBLITY VISIBLE");
            mEmptyView.setText(getString(R.string.fridge_empty_msg));
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mCursorAdapter.swapCursor(null);
    }
}
