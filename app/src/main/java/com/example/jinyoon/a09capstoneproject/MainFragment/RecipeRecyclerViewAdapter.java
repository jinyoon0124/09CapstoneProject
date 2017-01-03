package com.example.jinyoon.a09capstoneproject.MainFragment;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.jinyoon.a09capstoneproject.R;
import com.example.jinyoon.a09capstoneproject.RecipeDetailActivity;
import com.example.jinyoon.a09capstoneproject.Retrofit.Recipes;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Jin Yoon on 10/22/2016
 */

public class RecipeRecyclerViewAdapter extends RecyclerView.Adapter<RecipeRecyclerViewAdapter.ViewHolder> {

    private Context mContext;
    private List<Recipes> mRecipeDetails;
    private final String LOG_TAG = getClass().getSimpleName();

    public RecipeRecyclerViewAdapter(Context context, List<Recipes> details) {
        this.mContext = context;
        this.mRecipeDetails = details;
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
        Recipes recipes = mRecipeDetails.get(position);
        final String sourceUrl = recipes.getSourceUrl();

        holder.mView.setOnClickListener(new View.OnClickListener() {
            //Add OnClick Action later
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, RecipeDetailActivity.class);
                intent.putExtra(mContext.getString(R.string.publisher_url_key), sourceUrl);
                mContext.startActivity(intent);
                Log.v(LOG_TAG, "RecyclerView item OnClick");
            }
        });


        holder.mTitleView.setText(recipes.getTitle());

        Picasso.with(mContext)
                .load(recipes.getImageUrl())
                .into(holder.mThumbnailView);

    }

    @Override
    public int getItemCount() {
        if(mRecipeDetails!=null){
            return mRecipeDetails.size();
        }else{
            return 0;
        }
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
}


