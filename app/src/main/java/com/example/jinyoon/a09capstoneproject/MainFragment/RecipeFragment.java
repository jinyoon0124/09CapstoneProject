package com.example.jinyoon.a09capstoneproject.MainFragment;


import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jinyoon.a09capstoneproject.BuildConfig;
import com.example.jinyoon.a09capstoneproject.Database.MyFridgeDataContract;
import com.example.jinyoon.a09capstoneproject.Database.MyFridgeDataHelper;
import com.example.jinyoon.a09capstoneproject.MainActivity;
import com.example.jinyoon.a09capstoneproject.MyApplication;
import com.example.jinyoon.a09capstoneproject.R;
import com.example.jinyoon.a09capstoneproject.Retrofit.RecipeBody;
import com.example.jinyoon.a09capstoneproject.Retrofit.Recipes;
import com.example.jinyoon.a09capstoneproject.Retrofit.RecipeService;
import com.example.jinyoon.a09capstoneproject.Database.MyFridgeDataContract.*;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.R.attr.columnCount;

/**
 * A simple {@link Fragment} subclass.
 */
public class RecipeFragment extends Fragment {

    private final String LOG_TAG = this.getClass().getSimpleName();
    private RecyclerView mRecyclerView;
    private Context mContext;
    private TextView mEmptyView;

    private final String BASE_URL = "http://food2fork.com";
    private Call<RecipeBody> call;
    private List<Recipes> mRecipeDetails;
    private RecipeBody mRecipeBody;
    private RecipeRecyclerViewAdapter mRecipeRecyclerViewAdapter;

    public RecipeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mContext = getContext();
//        Log.e(LOG_TAG, "ON CREATE VIEW IS CALLED");
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_recipe, container, false);

        mEmptyView = (TextView) view.findViewById(R.id.recipe_empty_textview);
        mEmptyView.setVisibility(View.GONE);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_recipe);

//        String queryString=getParamFromPreference();
        String queryString = getParamFromFridge();

        if(!queryString .equals("")){
            updateRecipe(queryString);
//            Toast.makeText(mContext, queryString, Toast.LENGTH_SHORT).show();
        }else{
            mRecipeRecyclerViewAdapter=null;
        }


        if(mRecipeRecyclerViewAdapter ==null || mRecipeRecyclerViewAdapter.getItemCount()==0){
//            Log.e(LOG_TAG, "RECIPE ADAPTER NULL");
            mEmptyView.setVisibility(View.VISIBLE);
            mEmptyView.setText(getString(R.string.recipe_empty_msg));
        }


        return view;
    }


    private String getParamFromFridge(){
//        Log.e(LOG_TAG, "INSIDE GET PARAM FROM FRIDGE");
        Cursor cursor = mContext.getContentResolver().query(
                FridgeListEntry.CONTENT_URI,
                null,
                null,
                null,
                null
        );

        String param ="";

        if(cursor!=null){
            if(cursor.moveToFirst()){
                do{
                    param +=
                            cursor.getString(cursor.getColumnIndex(FridgeListEntry.COLUMN_GROCERY_NAME)) +",";
                }while (cursor.moveToNext());
            }
            cursor.close();

        }else{
            Log.e(LOG_TAG, "CURSOR NULL");
        }

//        Log.e(LOG_TAG, "++++++++++++++++"+ param);
        return param;
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

    private void updateRecipe(String param){
//        Log.e(LOG_TAG, "!!!!!!!!!!!UPDATE RECIPE CALLED");
//        Log.e(LOG_TAG, param);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RecipeService.RecipeApi recipeApi = retrofit.create(RecipeService.RecipeApi.class);

        //Add parameters
        Map<String, String> query = new HashMap<>();
        query.put(getString(R.string.recipe_query_api_key_param), BuildConfig.FOOD2FORK_API_KEY);
        query.put(getString(R.string.recipe_query_ingredient_param), param);

        call = recipeApi.getRecipe(query);

//        Log.e(LOG_TAG,call.request().url().toString());

        call.enqueue(new Callback<RecipeBody>() {
            @Override
            public void onResponse(Call<RecipeBody> call, Response<RecipeBody> response) {
                mRecipeBody = response.body();
                Log.e(LOG_TAG, "ERROR!!!! " + response.errorBody());

                mRecipeDetails= mRecipeBody.getRecipes();
                mRecipeRecyclerViewAdapter = new RecipeRecyclerViewAdapter(mContext, mRecipeDetails);

                int columnCount=0;
                if(getActivity()!=null){
                    if(getActivity().getResources().getConfiguration().orientation==1){
                        columnCount= getResources().getInteger(R.integer.list_column_count_portrait);
                    }else{
                        columnCount= getResources().getInteger(R.integer.list_column_count_horizontal);
                    }
                }

//                int columnCount= getResources().getInteger(R.integer.list_column_count_portrait);

                StaggeredGridLayoutManager sgim =
                        new StaggeredGridLayoutManager(columnCount, StaggeredGridLayoutManager.VERTICAL);
                mRecyclerView.setLayoutManager(sgim);

                mRecyclerView.setAdapter(mRecipeRecyclerViewAdapter);

                if(mRecipeRecyclerViewAdapter.getItemCount()!=0){
                    mEmptyView.setVisibility(View.GONE);
                }else{
                    mEmptyView.setVisibility(View.VISIBLE);
                    mEmptyView.setText(getString(R.string.recipe_empty_msg));
                }
            }

            @Override
            public void onFailure(retrofit2.Call<RecipeBody> call, Throwable t) {
                Log.e(LOG_TAG, "RecipeBody call failed");

            }
        });

    }

}
