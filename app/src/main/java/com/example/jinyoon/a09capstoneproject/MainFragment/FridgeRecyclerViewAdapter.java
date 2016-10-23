package com.example.jinyoon.a09capstoneproject.MainFragment;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.jinyoon.a09capstoneproject.R;

/**
 * Created by Jin Yoon on 10/9/2016.
 */

public class FridgeRecyclerViewAdapter extends RecyclerView.Adapter<FridgeRecyclerViewAdapter.ViewHolder> {

    private Context mContext;
    private final String LOG_TAG = getClass().getSimpleName();

    public FridgeRecyclerViewAdapter(Context context) {
        this.mContext = context;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        private View mView;
        private TextView mItemNameTextView;
        private TextView mDayTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
            mItemNameTextView = (TextView) itemView.findViewById(R.id.item_name);
            mDayTextView = (TextView) itemView.findViewById(R.id.item_day);
        }
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_fridge, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.mView.setOnClickListener(new View.OnClickListener() {
            //Add OnClick Action later
            @Override
            public void onClick(View v) {
                Log.v(LOG_TAG, "RecyclerView item OnClick");
            }
        });
        //Add what items to be included later... (after implementing database)
        holder.mItemNameTextView.setText("Broccoli");
        holder.mDayTextView.setText("10 days");
    }

    @Override
    public int getItemCount() {
        return 5;
    }

}
