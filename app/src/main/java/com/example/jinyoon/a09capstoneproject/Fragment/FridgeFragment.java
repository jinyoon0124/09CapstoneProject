package com.example.jinyoon.a09capstoneproject.Fragment;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.jinyoon.a09capstoneproject.FridgeRecyclerViewAdapter;
import com.example.jinyoon.a09capstoneproject.R;

/**
 * A placeholder fragment containing a simple view.
 */
public class FridgeFragment extends Fragment {

    private RecyclerView mRecyclerView;

    public FridgeFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_fridge, container, false);
//        TextView textView= (TextView) view.findViewById(R.id.fridge_test_text);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_fridge);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setAdapter(new FridgeRecyclerViewAdapter(getContext()));
        return view;
    }
}
