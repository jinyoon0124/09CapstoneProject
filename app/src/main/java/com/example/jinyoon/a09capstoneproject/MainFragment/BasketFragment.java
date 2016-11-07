package com.example.jinyoon.a09capstoneproject.MainFragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.jinyoon.a09capstoneproject.ItemTouchHelper.SimpleItemTouchHelperCallback;
import com.example.jinyoon.a09capstoneproject.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class BasketFragment extends Fragment {

    private RecyclerView mRecyclerView;
//    private ItemTouchHelper mItemTouchHelper;
//    private BasketRecyclerViewAdapter mTestAdapter;

    public BasketFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_basket, container, false);
//
//        ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback(mTestAdapter);
//        mItemTouchHelper = new ItemTouchHelper(callback);
//        mItemTouchHelper.attachToRecyclerView(mRecyclerView);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_basket);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setAdapter(new BasketRecyclerViewAdapter(getContext()));

        return view;
    }

}
