package com.example.jinyoon.a09capstoneproject.MainFragment;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.example.jinyoon.a09capstoneproject.ItemTouchHelper.ItemTouchHelperAdapter;
import com.example.jinyoon.a09capstoneproject.ItemTouchHelper.ItemTouchHelperViewHolder;
import com.example.jinyoon.a09capstoneproject.R;
import com.example.jinyoon.a09capstoneproject.Database.MyFridgeDataContract.*;

/**
 * Created by Jin Yoon on 10/9/2016.
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
    public void onItemDismissLeft(int position) {
        Cursor c = getCursor();
        c.moveToPosition(position);
        String name = c.getString(c.getColumnIndex(FridgeListEntry.COLUMN_GROCERY_NAME));

        mContext.getContentResolver().delete(
                FridgeListEntry.CONTENT_URI,
                "name = ?",
                new String[]{name}
        );
        this.notifyItemRemoved(position);
        Toast.makeText(mContext, "Item deleted", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onItemDismissRight(int position) {
        Cursor c = getCursor();
        c.moveToPosition(position);
        String name = c.getString(c.getColumnIndex(FridgeListEntry.COLUMN_GROCERY_NAME));

        mContext.getContentResolver().delete(
                FridgeListEntry.CONTENT_URI,
                "name = ?",
                new String[]{name}
        );
        this.notifyItemRemoved(position);
        Toast.makeText(mContext, "Item deleted", Toast.LENGTH_SHORT).show();
    }

    @Override
    public FridgeRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_fridge, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, Cursor cursor) {
        final String name = cursor.getString(cursor.getColumnIndex(FridgeListEntry.COLUMN_GROCERY_NAME));
        int days = cursor.getInt(cursor.getColumnIndex(FridgeListEntry.COLUMN_EXPIRATION));
        viewHolder.mItemName.setText(name);
        viewHolder.mDay.setText(mContext.getString(R.string.days_string, days));
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
                final String name = mCursor.getString(mCursor.getColumnIndex(FridgeListEntry.COLUMN_GROCERY_NAME));
                Toast.makeText(mContext, name, Toast.LENGTH_SHORT).show();

                new MaterialDialog.Builder(mContext).title("Edit Item")
                        .content("Edit Item name")
                        .inputType(InputType.TYPE_CLASS_TEXT)
                        .input("", name, new MaterialDialog.InputCallback() {
                            @Override
                            public void onInput(@NonNull MaterialDialog dialog, CharSequence input) {
                                Cursor c= mContext.getContentResolver().query(
                                        FridgeListEntry.CONTENT_URI,
                                        new String[]{FridgeListEntry.COLUMN_GROCERY_NAME},
                                        FridgeListEntry.COLUMN_GROCERY_NAME +" = ? ",
                                        new String[]{input.toString()},
                                        null
                                );
                                if(c!=null && c.getCount()!=0){
                                    Toast toast = Toast.makeText(mContext, mContext.getString(R.string.item_exist_msg), Toast.LENGTH_SHORT);
                                    toast.setGravity(Gravity.CENTER, Gravity.CENTER, 0);
                                    toast.show();
                                }else if(input.toString().equals("")){
                                    Toast toast = Toast.makeText(mContext, mContext.getString(R.string.no_input_msg), Toast.LENGTH_SHORT);
                                    toast.setGravity(Gravity.CENTER, Gravity.CENTER, 0);
                                    toast.show();
                                }else{
                                    ContentValues cv = new ContentValues();
                                    cv.put(FridgeListEntry.COLUMN_GROCERY_NAME, input.toString());
                                    mContext.getContentResolver().update(
                                            FridgeListEntry.CONTENT_URI,
                                            cv,
                                            "name = ? ",
                                            new String[]{name});
                                    mContext.getContentResolver().notifyChange(FridgeListEntry.CONTENT_URI, null);
                                }
                                c.close();
                            }
                        }).show();
            }else{
                Log.e(LOG_TAG, "Cursor doesn't exist");
            }


        }

    }

}
