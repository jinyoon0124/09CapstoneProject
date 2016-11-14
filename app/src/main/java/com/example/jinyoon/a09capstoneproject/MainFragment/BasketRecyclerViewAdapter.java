package com.example.jinyoon.a09capstoneproject.MainFragment;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.example.jinyoon.a09capstoneproject.R;

/**
 * Created by Jin Yoon on 10/12/2016.
 */

public class BasketRecyclerViewAdapter extends CursorRecyclerViewAdapter<BasketRecyclerViewAdapter.ViewHolder> {

    private Context mContext;
    private Cursor mCursor;
    private final String LOG_TAG = getClass().getSimpleName();

    public BasketRecyclerViewAdapter(Context context, Cursor cursor) {
        super(context, cursor);
        this.mContext=context;
        this.mCursor = cursor;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        public View mView;
        public TextView mItemName;
        public CheckBox mItemCheckBox;

        public ViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
            mItemName = (TextView) itemView.findViewById(R.id.basket_item_name);
            mItemCheckBox = (CheckBox) itemView.findViewById(R.id.basket_checkBox);
        }
    }


    @Override
    public BasketRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_basket, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, Cursor cursor) {
//        viewHolder.mView.setOnClickListener(new View.OnClickListener() {
//            //Add OnClick Action later
//            @Override
//            public void onClick(View v) {
//                Log.v(LOG_TAG, "RecyclerView item OnClick");
//            }
//        });
//        //Add what items to be included later... (after implementing database)
//        viewHolder.mItemCheckBox.setChecked(true);
//        viewHolder.mItemName.setText("Eggs");
        viewHolder.mItemName.setText(cursor.getString(cursor.getColumnIndex("name")));
        if(cursor.getInt(cursor.getColumnIndex("checker"))==1){
            viewHolder.mItemCheckBox.setChecked(true);
        }else if(cursor.getInt(cursor.getColumnIndex("checker"))==0){
            viewHolder.mItemCheckBox.setChecked(false);
        }else{
            Log.e(LOG_TAG, "Checker variable not set");
        }

    }

    @Override
    public int getItemCount() {
        return super.getItemCount();
    }


}
