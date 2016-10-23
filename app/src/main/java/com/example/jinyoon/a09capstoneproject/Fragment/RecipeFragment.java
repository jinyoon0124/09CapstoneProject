package com.example.jinyoon.a09capstoneproject.Fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.jinyoon.a09capstoneproject.R;
import com.example.jinyoon.a09capstoneproject.RecipeRecyclerViewAdapter;

/**
 * A simple {@link Fragment} subclass.
 */
public class RecipeFragment extends Fragment {

    private RecyclerView mRecyclerView;

    public RecipeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_recipe, container, false);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_recipe);
        mRecyclerView.setAdapter(new RecipeRecyclerViewAdapter(getContext()));
        int columnCount= getResources().getInteger(R.integer.list_column_count);
        StaggeredGridLayoutManager sgim =
                new StaggeredGridLayoutManager(columnCount, StaggeredGridLayoutManager.VERTICAL);
//        mRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        mRecyclerView.setLayoutManager(sgim);

        return view;
    }

}
