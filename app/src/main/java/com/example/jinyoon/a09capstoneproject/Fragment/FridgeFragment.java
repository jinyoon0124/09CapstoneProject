package com.example.jinyoon.a09capstoneproject.Fragment;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.jinyoon.a09capstoneproject.R;

/**
 * A placeholder fragment containing a simple view.
 */
public class FridgeFragment extends Fragment {

    public FridgeFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_fridge, container, false);
        TextView textView= (TextView) view.findViewById(R.id.fridge_test_text);
        return view;
    }
}
