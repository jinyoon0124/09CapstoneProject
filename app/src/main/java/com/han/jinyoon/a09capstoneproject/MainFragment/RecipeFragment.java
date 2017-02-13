package com.han.jinyoon.a09capstoneproject.MainFragment;


import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.Layout;
import android.util.ArraySet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.han.jinyoon.a09capstoneproject.BuildConfig;
import com.han.jinyoon.a09capstoneproject.MyApplication;
import com.han.jinyoon.a09capstoneproject.R;
import com.han.jinyoon.a09capstoneproject.Retrofit.RecipeBody;
import com.han.jinyoon.a09capstoneproject.Retrofit.Recipes;
import com.han.jinyoon.a09capstoneproject.Retrofit.RecipeService;
import com.han.jinyoon.a09capstoneproject.Database.MyFridgeDataContract.*;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
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
    private ImageView mEmptyImageView;

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
        mEmptyImageView = (ImageView) view.findViewById(R.id.recipe_empty_imageview);

        mEmptyView.setVisibility(View.GONE);
        mEmptyImageView.setVisibility(View.GONE);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_recipe);

        SharedPreferences spf = mContext.getSharedPreferences(getString(R.string.sharedpref_param_key), Context.MODE_PRIVATE);
        String queryString = spf.getString(getString(R.string.sharedpref_param_name_key), "");

        Log.e(LOG_TAG, "QUERYSTRING: "+queryString);
        if(!queryString.equals("")){
            updateRecipe(queryString);
        }else{
            mRecipeRecyclerViewAdapter=null;
        }

        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.recipe_fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectIngredient();
            }
        });

        if(mRecipeRecyclerViewAdapter ==null || mRecipeRecyclerViewAdapter.getItemCount()==0){
            mEmptyView.setVisibility(View.VISIBLE);
            mEmptyImageView.setVisibility(View.VISIBLE);
            mEmptyView.setText(getString(R.string.recipe_empty_msg));
        }
        return view;
    }


    private void selectIngredient(){
        ArrayList<String> ingredientList = getIngredientFromFridge();
        String[] indicesString ={};
        //Retrieve SharedPref
        SharedPreferences pref = mContext.getSharedPreferences(getString(R.string.sharedpref_param_key), Context.MODE_PRIVATE);
        Set<String> preSelectedIndices = pref.getStringSet(getString(R.string.sharedpref_param_index_key), null);

        if(preSelectedIndices!=null){
            indicesString = preSelectedIndices.toArray(new String[preSelectedIndices.size()]);
        }

        Integer[] indices= new Integer[indicesString.length];
        for(int i = 0; i<indicesString.length; i++){
            indices[i]=Integer.parseInt(indicesString[i]);
        }

        new MaterialDialog.Builder(mContext)
                .title(R.string.recipe_dialog_title)
                .items(ingredientList)
                .itemsCallbackMultiChoice(indices, new MaterialDialog.ListCallbackMultiChoice() {
                    @Override
                    public boolean onSelection(MaterialDialog dialog, Integer[] which, CharSequence[] text) {
                        String paramSelected="";

                        if(text.length==0){
                            paramSelected="";
                        }else {
                            for (CharSequence s : text) {
                                paramSelected += s + ",";
                            }
                        }
//                        Log.e(LOG_TAG, "SELECTED: "+ paramSelected);

                        //Change indices to string to store in Shared Pref
                        HashSet<String> selectedIndices = new HashSet<>();

                        if(which.length==0){
                            mRecipeRecyclerViewAdapter=null;
                        }else{
                            for(int i : which){
                            selectedIndices.add(String.valueOf(i));
//                            Log.e(LOG_TAG, "SELECTED INDICES: "+ String.valueOf(i));
                        }
                        }

                        //Add selected index into Shared Preference so that the selected items remain selected next time
                        SharedPreferences pref = mContext.getSharedPreferences(getString(R.string.sharedpref_param_key), Context.MODE_PRIVATE);
                        SharedPreferences.Editor ed = pref.edit();
                        ed.clear();
                        ed.putStringSet(getString(R.string.sharedpref_param_index_key), selectedIndices);
                        ed.putString(getString(R.string.sharedpref_param_name_key), paramSelected);
                        ed.commit();

                        if(paramSelected.equals("")){
                            Toast.makeText(mContext, mContext.getString(R.string.recipe_no_param), Toast.LENGTH_SHORT).show();
                            mRecipeRecyclerViewAdapter=null;

                            FragmentTransaction ft = getFragmentManager().beginTransaction();
                            ft.detach(RecipeFragment.this).attach(RecipeFragment.this).commit();
                        }else{
                            updateRecipe(paramSelected);
                        }

                        return true;
                    }
                }).positiveText(R.string.recipe_positive_text)
                .show();
    }


    private ArrayList<String> getIngredientFromFridge(){
//        Log.e(LOG_TAG, "INSIDE GET PARAM FROM FRIDGE");
        Cursor cursor = mContext.getContentResolver().query(
                FridgeListEntry.CONTENT_URI,
                null,
                null,
                null,
                null
        );

        ArrayList<String> ingredientsList = new ArrayList<>();

        if(cursor!=null){
            if(cursor.moveToFirst()){
                do{
                    ingredientsList.add(cursor.getString(cursor.getColumnIndex(FridgeListEntry.COLUMN_GROCERY_NAME)));
                }while (cursor.moveToNext());
            }
            cursor.close();

        }else{
            Log.e(LOG_TAG, "CURSOR NULL");
        }

//        Log.e(LOG_TAG, "++++++++++++++++"+ param);
        return ingredientsList;
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

        Log.e(LOG_TAG,call.request().url().toString());

        call.enqueue(new Callback<RecipeBody>() {
            @Override
            public void onResponse(Call<RecipeBody> call, Response<RecipeBody> response) {
                mRecipeBody = response.body();
//                Log.e(LOG_TAG, "ERROR!!!! " + response.errorBody());

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
                    mEmptyImageView.setVisibility(View.GONE);
                }else{
                    mEmptyView.setVisibility(View.VISIBLE);
                    mEmptyImageView.setVisibility(View.VISIBLE);
                    mEmptyView.setText(getString(R.string.recipe_empty_msg));
                }
            }

            @Override
            public void onFailure(retrofit2.Call<RecipeBody> call, Throwable t) {
                Log.e(LOG_TAG, "RecipeBody call failed");
                Toast.makeText(mContext, mContext.getString(R.string.no_connection_msg), Toast.LENGTH_SHORT).show();

            }
        });

    }

}
