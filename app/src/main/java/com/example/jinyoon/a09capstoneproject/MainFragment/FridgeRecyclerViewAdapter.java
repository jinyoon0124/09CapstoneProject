package com.example.jinyoon.a09capstoneproject.MainFragment;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.example.jinyoon.a09capstoneproject.Database.MyFridgeDataContract;
import com.example.jinyoon.a09capstoneproject.ItemTouchHelper.ItemTouchHelperAdapter;
import com.example.jinyoon.a09capstoneproject.ItemTouchHelper.ItemTouchHelperViewHolder;
import com.example.jinyoon.a09capstoneproject.R;
import com.example.jinyoon.a09capstoneproject.Database.MyFridgeDataContract.*;

import java.util.Set;

/**
 * FridgeFragment Recycler View Adapter
 */

public class FridgeRecyclerViewAdapter extends CursorRecyclerViewAdapter<FridgeRecyclerViewAdapter.ViewHolder>
        implements ItemTouchHelperAdapter{

    private Context mContext;
    private Cursor mCurosr;
    private final String LOG_TAG = getClass().getSimpleName();

    public FridgeRecyclerViewAdapter(Context context, Cursor cursor) {
        super(context, cursor);
        this.mContext = context;
        this.mCurosr = cursor;
    }

    @Override
    public void onItemDismissLeft(int position, RecyclerView rv) {
        Cursor c = getCursor();
        c.moveToPosition(position);
        String name = c.getString(c.getColumnIndex(FridgeListEntry.COLUMN_GROCERY_NAME));

        mContext.getContentResolver().delete(
                FridgeListEntry.CONTENT_URI,
                "name = ?",
                new String[]{name}
        );
//        notifyItemRemoved(position);
        mContext.getContentResolver().notifyChange(FridgeListEntry.CONTENT_URI, null);
//        notifyDataSetChanged();
        Snackbar.make(rv, mContext.getString(R.string.remove_msg), Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void onItemDismissRight(int position, RecyclerView rv) {
        Cursor c = getCursor();
        c.moveToPosition(position);
        String name = c.getString(c.getColumnIndex(FridgeListEntry.COLUMN_GROCERY_NAME));

        mContext.getContentResolver().delete(
                FridgeListEntry.CONTENT_URI,
                "name = ?",
                new String[]{name}
        );
//        notifyItemRemoved(position);
        mContext.getContentResolver().notifyChange(FridgeListEntry.CONTENT_URI, null);
//        notifyDataSetChanged();
         Snackbar.make(rv, mContext.getString(R.string.remove_msg), Snackbar.LENGTH_SHORT).show();

    }

    @Override
    public FridgeRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.e(LOG_TAG, "ON CREATE VIEWHOLDER CALLED");

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_fridge, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final Cursor cursor) {

        String name =cursor.getString(cursor.getColumnIndex(FridgeListEntry.COLUMN_GROCERY_NAME));
        int days = cursor.getInt(cursor.getColumnIndex(FridgeListEntry.COLUMN_EXPIRATION));

        viewHolder.mItemName.setText(name);
        viewHolder.mDay.setText(mContext.getString(R.string.days_string, days));

        if(days<=1){
//           viewHolder.mItemName.setBackgroundColor(mContext.getColor(R.color.colorPrimaryLight));
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                viewHolder.mView.setBackgroundColor(mContext.getColor(R.color.colorRedDark));
                viewHolder.mItemName.setTextColor(mContext.getColor(R.color.colorRedDark));
                viewHolder.mDay.setTextColor(mContext.getColor(R.color.colorRedDark));
            }
        }else{
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                viewHolder.mItemName.setTextColor(mContext.getColor(R.color.colorSecondaryText));
                viewHolder.mDay.setTextColor(mContext.getColor(R.color.colorSecondaryText));

            }
        }
    }


    @Override
    public int getItemCount() {
        return super.getItemCount();
    }



    public static class ViewHolder extends RecyclerView.ViewHolder
            implements ItemTouchHelperViewHolder, View.OnClickListener{
        private View mView;
        private TextView mItemName;
        private TextView mDay;
        private final String LOG_TAG = this.getClass().getSimpleName();

        public ViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
            mItemName = (TextView) itemView.findViewById(R.id.item_name);
            mDay = (TextView) itemView.findViewById(R.id.item_day);

            mView.setOnClickListener(this);
        }

        @Override
        public void onItemSelected() {
            mView.setBackgroundColor(Color.LTGRAY);
        }

        @Override
        public void onItemClear() {
            mView.setBackgroundColor(Color.WHITE);
        }

        @Override
        public void onClick(View v) {
            final int position = getAdapterPosition();
            final Context mContext = v.getContext();
            Cursor mCursor = mContext.getContentResolver().query(
                    FridgeListEntry.CONTENT_URI,
                    null,
                    null,
                    null,
                    null
            );

            if(mCursor !=null){
                mCursor.moveToPosition(position);
                final String oldName = mCursor.getString(mCursor.getColumnIndex(FridgeListEntry.COLUMN_GROCERY_NAME));
                final int oldDay = mCursor.getInt(mCursor.getColumnIndex(FridgeListEntry.COLUMN_EXPIRATION));
//                Toast.makeText(mContext, oldName, Toast.LENGTH_SHORT).show();

                final MaterialDialog dialog = new MaterialDialog.Builder(mContext).title(mContext.getString(R.string.fridge_dialog_edit_title))
                        .customView(R.layout.dialog_fridge, true)
                        .negativeText(mContext.getString(R.string.fridge_dialog_negative_text))
                        .positiveText(mContext.getString(R.string.fridge_dialog_positive_text))
                        .build();

                dialog.show();
                final View dialogView = dialog.getCustomView();
                final EditText itemNameEditText = (EditText)dialogView.findViewById(R.id.fridge_dialog_name_input);
                final EditText dayValueEditText = (EditText)dialogView.findViewById(R.id.fridge_dialog_days_input);
//                itemNameEditText.setHint(oldName);
//                dayValueEditText.setHint(String.valueOf(oldDay));
                itemNameEditText.setText(oldName);
                dayValueEditText.setText(String.valueOf(oldDay));

                View positive = dialog.getActionButton(DialogAction.POSITIVE);
                positive.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String itemName = String.valueOf(itemNameEditText.getText());
                        String dayValue = String.valueOf(dayValueEditText.getText());

                        Cursor c = mContext.getContentResolver().query(
                                FridgeListEntry.CONTENT_URI,
                                new String[]{FridgeListEntry.COLUMN_GROCERY_NAME},
                                FridgeListEntry.COLUMN_GROCERY_NAME +" = ? ",
                                new String[]{itemName},
                                null);

                        if(c!=null && c.getCount()>1){
                            Toast toast =
                                    Toast.makeText(mContext, mContext.getString(R.string.item_exist_msg), Toast.LENGTH_SHORT);
                            toast.setGravity(Gravity.CENTER, Gravity.CENTER, 0);
                            toast.show();
                        }else if(itemName.equals("")){
                            Toast toast =
                                    Toast.makeText(mContext, mContext.getString(R.string.no_input_msg), Toast.LENGTH_SHORT);
                            toast.setGravity(Gravity.CENTER, Gravity.CENTER, 0);
                            toast.show();

                        }else if(dayValue==null){
                            Toast toast =
                                    Toast.makeText(mContext, mContext.getString(R.string.no_days_input_msg), Toast.LENGTH_SHORT);
                            toast.setGravity(Gravity.CENTER, Gravity.CENTER, 0);
                            toast.show();

                        }
                        else{
//                                    SQLiteDatabase db = new MyFridgeDataHelper(mContext).getReadableDatabase();
//                                    c = db.rawQuery("SELECT * FROM "+ ShopListEntry.TABLE_NAME, null);
//                                    Log.e("!!!! INSIDE DIALOG!! C!", String.valueOf(c.getCount()));
//                                    int itemOrder = c.getCount();
                            ContentValues cv = new ContentValues();
                            cv.put(FridgeListEntry.COLUMN_GROCERY_NAME, itemName);
                            cv.put(FridgeListEntry.COLUMN_EXPIRATION, Integer.parseInt(dayValue));
                            cv.put(FridgeListEntry.COLUMN_INPUTDATEINMIL, System.currentTimeMillis());

//                            mContext.getContentResolver().insert(FridgeListEntry.CONTENT_URI, cv);
                            mContext.getContentResolver().update(
                                    FridgeListEntry.CONTENT_URI,
                                    cv,
                                    "name = ?",
                                    new String[]{oldName}
                            );

                            mContext.getContentResolver().notifyChange(FridgeListEntry.CONTENT_URI, null);
//                            /////SHARED PREFERENCE
//                            SharedPreferences spf = mContext.getSharedPreferences(INGREDIENT_KEY, Context.MODE_APPEND);
//                            Set<String> query = spf.getStringSet(INGREDIENT_KEY, null);
//                            query.remove(oldName);
//                            query.add(itemName);
//                            SharedPreferences.Editor ed = spf.edit();
//                            ed.putStringSet(INGREDIENT_KEY, query);
//                            ed.commit();
                            Toast.makeText(mContext, mContext.getString(R.string.item_modified_msg), Toast.LENGTH_SHORT).show();
                        }

                        c.close();
//                        Toast.makeText(mContext, itemName + " : " + dayValue, Toast.LENGTH_SHORT).show();


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

                mCursor.close();

            }else{
                Log.e(LOG_TAG, "Cursor doesn't exist");
            }


        }

    }

}
