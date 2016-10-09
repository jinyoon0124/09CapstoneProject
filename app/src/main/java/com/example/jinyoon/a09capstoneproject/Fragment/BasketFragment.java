package com.example.jinyoon.a09capstoneproject.Fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.TextViewCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.jinyoon.a09capstoneproject.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class BasketFragment extends Fragment {


    public BasketFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_basket, container, false);
        TextView textView = (TextView) view.findViewById(R.id.basket_test_text);

        return view;
    }

}
