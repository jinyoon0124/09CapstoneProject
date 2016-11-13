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

public class BasketRecyclerViewAdapter extends RecyclerView.Adapter<BasketRecyclerViewAdapter.ViewHolder> {

    private Context mContext;
    private final String LOG_TAG = getClass().getSimpleName();
    private Cursor mCursor;

    public BasketRecyclerViewAdapter(Context context) {
        this.mContext = context;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        private View mView;
        private TextView mItemName;
        private CheckBox mItemCheckBox;

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
    public void onBindViewHolder(BasketRecyclerViewAdapter.ViewHolder holder, int position) {
        holder.mView.setOnClickListener(new View.OnClickListener() {
            //Add OnClick Action later
            @Override
            public void onClick(View v) {
                Log.v(LOG_TAG, "RecyclerView item OnClick");
            }
        });
        //Add what items to be included later... (after implementing database)
        holder.mItemCheckBox.setChecked(true);
        holder.mItemName.setText("Eggs");

    }

    @Override
    public int getItemCount() {
        return 5;
    }

}
