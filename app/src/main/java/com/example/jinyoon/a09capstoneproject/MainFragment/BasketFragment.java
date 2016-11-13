package com.example.jinyoon.a09capstoneproject.MainFragment;


import android.content.ContentValues;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.InputType;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.example.jinyoon.a09capstoneproject.Database.MyFridgeDataContract.*;
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

        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.basket_fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new MaterialDialog.Builder(getContext()).title("Add Item to basket")
                        .content("Test content")
                        .inputType(InputType.TYPE_CLASS_TEXT)
                        .input("Test Guide", "", new MaterialDialog.InputCallback() {
                            @Override
                            public void onInput(MaterialDialog dialog, CharSequence input) {
                                Cursor c = getContext().getContentResolver().query(
                                        ShopLIstEntry.CONTENT_URI,
                                        new String[]{ShopLIstEntry.COLUMN_GROCERY_NAME},
                                        ShopLIstEntry.COLUMN_GROCERY_NAME +" = ? ",
                                        new String[]{input.toString()},
                                        null);
                                if(c.getCount()!=0){
                                    Toast toast =
                                        Toast.makeText(getContext(), getString(R.string.item_exist_msg), Toast.LENGTH_LONG);
                                    toast.setGravity(Gravity.CENTER, Gravity.CENTER, 0);
                                    toast.show();
                                    return;
                                }else{
                                    ContentValues cv = new ContentValues();
                                    cv.put(ShopLIstEntry.COLUMN_CHECKER, 0);
                                    cv.put(ShopLIstEntry.COLUMN_GROCERY_NAME, input.toString());
                                    getContext().getContentResolver().insert(ShopLIstEntry.CONTENT_URI, cv);
                                }


                            }
                        }).show();

            }
        });



        return view;
    }

}
