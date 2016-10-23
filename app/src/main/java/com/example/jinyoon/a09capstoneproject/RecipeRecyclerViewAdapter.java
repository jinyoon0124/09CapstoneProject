package com.example.jinyoon.a09capstoneproject;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

/**
 * Created by Jin Yoon on 10/22/2016.
 */

public class RecipeRecyclerViewAdapter extends RecyclerView.Adapter<RecipeRecyclerViewAdapter.ViewHolder> {

    private Context mContext;
    private final String LOG_TAG = getClass().getSimpleName();

    public RecipeRecyclerViewAdapter(Context context) {
        this.mContext = context;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private View mView;
        private ImageView mThumbnailView;
        private TextView mTitleView;

        public ViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
            mThumbnailView = (ImageView) itemView.findViewById(R.id.recipe_thumbnail);
            mTitleView = (TextView) itemView.findViewById(R.id.recipe_title);

        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_recipe, parent, false);

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

        holder.mTitleView.setText("Egg Roll Roll Roll Roll");
        Picasso.with(mContext)
                .load(R.drawable.recipe_test_thumbnail)

                .into(holder.mThumbnailView);

    }

    @Override
    public int getItemCount() {
        return 5;
    }

}


