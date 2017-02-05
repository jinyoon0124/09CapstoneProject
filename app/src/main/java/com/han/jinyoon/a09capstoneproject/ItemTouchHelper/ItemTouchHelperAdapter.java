package com.han.jinyoon.a09capstoneproject.ItemTouchHelper;

import android.support.v7.widget.RecyclerView;

/**
 * Created by Jin Yoon on 10/23/2016.
 */

public interface ItemTouchHelperAdapter {
//    void onItemMove(int fromPosition, int toPosition);
    void onItemDismissLeft(int position, RecyclerView rv);
    void onItemDismissRight(int position, RecyclerView rv);


}
