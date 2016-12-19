package com.example.jinyoon.a09capstoneproject.MainFragment;


import android.content.Context;
import android.content.SharedPreferences;
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

import com.example.jinyoon.a09capstoneproject.BuildConfig;
import com.example.jinyoon.a09capstoneproject.R;
import com.example.jinyoon.a09capstoneproject.Retrofit.RecipeBody;
import com.example.jinyoon.a09capstoneproject.Retrofit.Recipes;
import com.example.jinyoon.a09capstoneproject.Retrofit.RecipeService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * A simple {@link Fragment} subclass.
 */
public class RecipeFragment extends Fragment {

    private final String LOG_TAG = this.getClass().getSimpleName();
    private RecyclerView mRecyclerView;
    private Context mContext;
    private TextView mEmptyView;

    private final String INGREDIENT_KEY = "ingredient";
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
        Log.e(LOG_TAG, "ON CREATE VIEW IS CALLED");
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_recipe, container, false);

        mEmptyView = (TextView) view.findViewById(R.id.recipe_empty_textview);
        mEmptyView.setVisibility(View.GONE);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_recipe);

        String queryString=getParamFromPreference();

        if(!queryString .equals("")){
            updateRecipe(queryString);
        }else{
            mRecipeRecyclerViewAdapter=null;
        }


        if(mRecipeRecyclerViewAdapter ==null || mRecipeRecyclerViewAdapter.getItemCount()==0){
            Log.e(LOG_TAG, "RECIPE ADAPTER NULL");
            mEmptyView.setVisibility(View.VISIBLE);
            mEmptyView.setText(getString(R.string.recipe_empty_msg));
        }


        return view;
    }
//
//    @Override
//    public void onResume() {
//
//        Log.e(LOG_TAG, "!!!!!!!!!!!ON RESUME CALLED");
//        String queryString= getParamFromPreference();
//        if(!queryString .equals("")) {
//            updateRecipe(queryString);
//        }
//        super.onResume();
//
//    }

    //GET Query param from shared preference
    private String getParamFromPreference(){

        SharedPreferences spf = mContext.getSharedPreferences(INGREDIENT_KEY, Context.MODE_APPEND);
        Set<String> querySet = spf.getStringSet(INGREDIENT_KEY, null);
        String queryString="";
        if(querySet!=null){
            for(String i : querySet){queryString +=","+i;}
        }
        Log.e("SHARED PREFERENCE", queryString);
        return queryString;
    }


    private void updateRecipe(String param){
        Log.e(LOG_TAG, "!!!!!!!!!!!UPDATE RECIPE CALLED");
        Log.e(LOG_TAG, param);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RecipeService.RecipeApi recipeApi = retrofit.create(RecipeService.RecipeApi.class);

        //Add parameters
        Map<String, String> query = new HashMap<>();
        query.put("key", BuildConfig.FOOD2FORK_API_KEY);
        query.put("q", param);

        call = recipeApi.getRecipe(query);

        Log.e(LOG_TAG,call.request().url().toString());

        call.enqueue(new Callback<RecipeBody>() {
            @Override
            public void onResponse(Call<RecipeBody> call, Response<RecipeBody> response) {
                Log.e(LOG_TAG, "!!!!!!!!!!!!! RETROFIT ON RESPONSE CALLED");
                mRecipeBody = response.body();

                mRecipeDetails= mRecipeBody.getRecipes();

                if(mRecipeDetails.size()==0){
                    Log.e(LOG_TAG, "!!! NO RECIPE ITEM IS RETRIEVED");

                }else{
                    Log.e(LOG_TAG, "There are some items in the recipe!");
                }

                mRecipeRecyclerViewAdapter = new RecipeRecyclerViewAdapter(mContext, mRecipeDetails);

                int columnCount= getResources().getInteger(R.integer.list_column_count);
                StaggeredGridLayoutManager sgim =
                        new StaggeredGridLayoutManager(columnCount, StaggeredGridLayoutManager.VERTICAL);
                mRecyclerView.setLayoutManager(sgim);

//                mRecyclerView.setLayoutManager(new GridLayoutManager(mContext, 2));
                mRecyclerView.setAdapter(mRecipeRecyclerViewAdapter);

                if(mRecipeRecyclerViewAdapter.getItemCount()!=0){
                    mEmptyView.setVisibility(View.GONE);
                }else{
                    mEmptyView.setVisibility(View.VISIBLE);
//            Log.e(LOG_TAG, "SET VISIBLITY VISIBLE");
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
