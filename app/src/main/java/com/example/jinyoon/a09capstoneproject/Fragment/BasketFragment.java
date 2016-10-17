package com.example.jinyoon.a09capstoneproject.Fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.TextViewCompat;
import android.support.v7.widget.AppCompatCheckedTextView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckedTextView;
import android.widget.TextView;

import com.example.jinyoon.a09capstoneproject.BasketRecyclerViewAdapter;
import com.example.jinyoon.a09capstoneproject.FridgeRecyclerViewAdapter;
import com.example.jinyoon.a09capstoneproject.R;

import org.w3c.dom.Text;

/**
 * A simple {@link Fragment} subclass.
 */
public class BasketFragment extends Fragment {

    private RecyclerView mRecyclerView;

    public BasketFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_basket, container, false);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_basket);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setAdapter(new BasketRecyclerViewAdapter(getContext()));

        return view;
    }

}
