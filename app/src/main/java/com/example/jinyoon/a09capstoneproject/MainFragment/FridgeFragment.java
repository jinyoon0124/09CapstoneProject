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
import com.example.jinyoon.a09capstoneproject.R;

import java.util.HashSet;
import java.util.Set;

/**
 * A placeholder fragment containing a simple view.
 */
public class FridgeFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>{

    private final String LOG_TAG = this.getClass().getSimpleName();
    private RecyclerView mfRecyclerView;
    private Context mContext;
    private Cursor mCursor;
    private TextView mEmptyView;
    private FridgeRecyclerViewAdapter mCursorAdapter;
    private static final int CURSOR_LOADER_ID = 1;
    private final String INGREDIENT_KEY = "ingredient";
    private Set<String> query = new HashSet<>();

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

        mfRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_fridge);
        mfRecyclerView.setHasFixedSize(true);
        mfRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));

        mCursorAdapter = new FridgeRecyclerViewAdapter(mContext, null);
        mfRecyclerView.setAdapter(mCursorAdapter);
        getLoaderManager().initLoader(CURSOR_LOADER_ID, null, this);

        ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback(mCursorAdapter);
        final ItemTouchHelper mItemTouchHelper = new ItemTouchHelper(callback);
        mItemTouchHelper.attachToRecyclerView(mfRecyclerView);

        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.fridge_fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view1) {
                final MaterialDialog dialog = new MaterialDialog.Builder(mContext).title("Add item to fridge")
                        .customView(R.layout.dialog_fridge, true)
                        .negativeText("Cancel")
                        .positiveText("Ok")
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
//                                    SQLiteDatabase db = new MyFridgeDataHelper(mContext).getReadableDatabase();
//                                    c = db.rawQuery("SELECT * FROM "+ ShopListEntry.TABLE_NAME, null);
//                                    Log.e("!!!! INSIDE DIALOG!! C!", String.valueOf(c.getCount()));
//                                    int itemOrder = c.getCount();

                            ContentValues cv = new ContentValues();

                            long currentTime = System.currentTimeMillis();
                            cv.put(FridgeListEntry.COLUMN_GROCERY_NAME, itemName);
                            cv.put(FridgeListEntry.COLUMN_EXPIRATION, Integer.parseInt(dayValue));
                            cv.put(FridgeListEntry.COLUMN_INPUTDATEINMIL,  String.valueOf(currentTime));

//                            Log.e("....FridgeFragment", String.valueOf(currentTime));

                            mContext.getContentResolver().insert(FridgeListEntry.CONTENT_URI, cv);

                            mContext.getContentResolver().notifyChange(FridgeListEntry.CONTENT_URI, null);
//                            Toast.makeText(mContext, getString(R.string.item_added_msg, itemName), Toast.LENGTH_SHORT).show();

//////////////////////////////////////////////////////////////////
//                            SharedPreferences spf = mContext.getSharedPreferences(INGREDIENT_KEY, Context.MODE_APPEND);
//                            SharedPreferences.Editor ed = spf.edit();
//                            query = spf.getStringSet(INGREDIENT_KEY, new HashSet<String>());
//                            query.add(itemName);
//                            ed.putStringSet(INGREDIENT_KEY, query);
//                            ed.commit();
///////////////////////////////////////////////////////////////////

                        }

                        c.close();


//                        Toast.makeText(mContext, itemName + " : " + dayValue, Toast.LENGTH_SHORT).show();
//                        Toast.makeText(mContext.getApplicationContext(), String.valueOf(currentTime), Toast.LENGTH_SHORT).show();

//                        ///////TEST//////
//                        SharedPreferences spf = mContext.getSharedPreferences(INGREDIENT_KEY, Context.MODE_APPEND);
//                        Set<String> test = spf.getStringSet(INGREDIENT_KEY, null);
//
//                        String testString="";
//                        for(String i : test){
//                            testString +=i;
//                        }
//                        Toast.makeText(mContext, testString, Toast.LENGTH_SHORT).show();
                        ////////

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
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                new MaterialDialog.Builder(mContext).title("Add Item to Fridge")
//                        .inputType(InputType.TYPE_CLASS_TEXT)
//                        .input("Eggs", "", new MaterialDialog.InputCallback() {
//                            @Override
//                            public void onInput(MaterialDialog dialog, CharSequence input) {
//
////                                Cursor c = getContext().getContentResolver().query(
////                                        ShopListEntry.CONTENT_URI,
////                                        new String[]{ShopListEntry.COLUMN_GROCERY_NAME},
////                                        ShopListEntry.COLUMN_GROCERY_NAME +" = ? ",
////                                        new String[]{input.toString()},
////                                        null);
////                                if(c!=null &&c.getCount()!=0){
////                                    Toast toast =
////                                            Toast.makeText(mContext, getString(R.string.item_exist_msg), Toast.LENGTH_SHORT);
////                                    toast.setGravity(Gravity.CENTER, Gravity.CENTER, 0);
////                                    toast.show();
////                                }else if(input.toString().equals("")){
////                                    Toast toast =
////                                            Toast.makeText(mContext, getString(R.string.no_input_msg), Toast.LENGTH_SHORT);
////                                    toast.setGravity(Gravity.CENTER, Gravity.CENTER, 0);
////                                    toast.show();
////
////                                }else{
////                                    SQLiteDatabase db = new MyFridgeDataHelper(mContext).getReadableDatabase();
////                                    c = db.rawQuery("SELECT * FROM "+ ShopListEntry.TABLE_NAME, null);
//////                                    Log.e("!!!! INSIDE DIALOG!! C!", String.valueOf(c.getCount()));
//////                                    int itemOrder = c.getCount();
////
////                                    ContentValues cv = new ContentValues();
////                                    cv.put(ShopListEntry.COLUMN_GROCERY_NAME, input.toString());
//////                                    cv.put(ShopListEntry.COLUMN_ORDERS, itemOrder);
////
////                                    mContext.getContentResolver().insert(ShopListEntry.CONTENT_URI, cv);
////                                    onItemChanged();
////                                }
////
////                                c.close();
//                            }
//                        }).show();
//
//            }
//        });

        return view;
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

        CursorLoader loader = new CursorLoader(mContext,
                FridgeListEntry.CONTENT_URI,
                mProjection,
                null,
                null,
                null
        );
        return loader;
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
