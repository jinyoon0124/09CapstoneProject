package com.han.jinyoon.a09capstoneproject.ItemTouchHelper;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;

import com.han.jinyoon.a09capstoneproject.MainFragment.BasketRecyclerViewAdapter;
import com.han.jinyoon.a09capstoneproject.MainFragment.FridgeRecyclerViewAdapter;
import com.han.jinyoon.a09capstoneproject.R;

/**
 * Created by Jin Yoon on 10/23/2016.
 */

public class SimpleItemTouchHelperCallback extends ItemTouchHelper.Callback {
    private final ItemTouchHelperAdapter mAdapter;
    private final RecyclerView rv;
    public static final float ALPHA_FULL = 1.0f;

    public SimpleItemTouchHelperCallback(ItemTouchHelperAdapter adapter, RecyclerView rv) {
        this.mAdapter = adapter;
        this.rv = rv;
    }

    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        int dragFlags, swipeFlags;
        dragFlags= ItemTouchHelper.UP | ItemTouchHelper.DOWN;
        if(mAdapter instanceof BasketRecyclerViewAdapter){
            swipeFlags = ItemTouchHelper.RIGHT | ItemTouchHelper.LEFT;
        }else{
            swipeFlags = ItemTouchHelper.RIGHT;
        }

        return makeMovementFlags(dragFlags, swipeFlags);
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
//        mAdapter.onItemMove(viewHolder.getAdapterPosition(), target.getAdapterPosition());
        return false;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        if(direction == ItemTouchHelper.LEFT){

            mAdapter.onItemDismissLeft(viewHolder.getAdapterPosition(), rv);
        }else if(direction == ItemTouchHelper.RIGHT){
            mAdapter.onItemDismissRight(viewHolder.getAdapterPosition(), rv);
        }
    }

    @Override
    public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        Bitmap icon;
        Paint p = new Paint();
        if(mAdapter instanceof BasketRecyclerViewAdapter) {
            if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
                View itemView = viewHolder.itemView;
                float height = (float) itemView.getBottom() - (float) itemView.getTop();
                float width = height / 3;
                if (dX > 0) {
                    //Item swiped to right >> Delete
//                    p.setColor(Color.parseColor("#D32F2F"));
                    p.setColor(ContextCompat.getColor(itemView.getContext(), R.color.colorRedDark));

                    RectF background = new RectF(
                            (float) itemView.getLeft(),
                            (float) itemView.getTop(),
                            dX,
                            (float) itemView.getBottom());
                    c.drawRect(background, p);
                    icon = BitmapFactory.decodeResource(itemView.getContext().getResources(), R.drawable.ic_delete_white);
                    RectF icon_dest = new RectF(
                            (float) itemView.getLeft() + width,
                            (float) itemView.getTop() + width,
                            (float) itemView.getLeft() + 2 * width,
                            (float) itemView.getBottom() - width);
                    c.drawBitmap(icon, null, icon_dest, p);

                } else {
                    //item swiped to left >> Add to Fridge list
//                    p.setColor(Color.parseColor("#388E3C"));
                    p.setColor(ContextCompat.getColor(itemView.getContext(), R.color.colorPrimaryDark));

                    RectF background = new RectF(
                            (float) itemView.getRight() + dX,
                            (float) itemView.getTop(),
                            (float) itemView.getRight(),
                            (float) itemView.getBottom());
                    c.drawRect(background, p);
                    icon = BitmapFactory.decodeResource(itemView.getContext().getResources(), R.drawable.ic_fridge_white);
                    RectF icon_dest = new RectF(
                            (float) itemView.getRight() - 2 * width,
                            (float) itemView.getTop() + width,
                            (float) itemView.getRight() - width,
                            (float) itemView.getBottom() - width);
                    c.drawBitmap(icon, null, icon_dest, p);
                }
            }
        }else if(mAdapter instanceof FridgeRecyclerViewAdapter){
            if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
                View itemView = viewHolder.itemView;
                float height = (float) itemView.getBottom() - (float) itemView.getTop();
                float width = height / 3;
                if (dX > 0) {
                    //Item swiped to right >> Delete
//                    p.setColor(Color.parseColor("#D32F2F"));
                    p.setColor(ContextCompat.getColor(itemView.getContext(), R.color.colorRedDark));

                    RectF background = new RectF(
                            (float) itemView.getLeft(),
                            (float) itemView.getTop(),
                            dX,
                            (float) itemView.getBottom());
                    c.drawRect(background, p);
                    icon = BitmapFactory.decodeResource(itemView.getContext().getResources(), R.drawable.ic_delete_white);
                    RectF icon_dest = new RectF(
                            (float) itemView.getLeft() + width,
                            (float) itemView.getTop() + width,
                            (float) itemView.getLeft() + 2 * width,
                            (float) itemView.getBottom() - width);
                    c.drawBitmap(icon, null, icon_dest, p);

                } else {
                    //item swiped to left >> Delete
//                    p.setColor(Color.parseColor("#D32F2F"));
                    p.setColor(ContextCompat.getColor(itemView.getContext(), R.color.colorRedDark));

                    RectF background = new RectF(
                            (float) itemView.getRight() + dX,
                            (float) itemView.getTop(),
                            (float) itemView.getRight(),
                            (float) itemView.getBottom());
                    c.drawRect(background, p);
                    icon = BitmapFactory.decodeResource(itemView.getContext().getResources(), R.drawable.ic_delete_white);
                    RectF icon_dest = new RectF(
                            (float) itemView.getRight() - 2 * width,
                            (float) itemView.getTop() + width,
                            (float) itemView.getRight() - width,
                            (float) itemView.getBottom() - width);
                    c.drawBitmap(icon, null, icon_dest, p);
                }
            }
        }
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);

    }

    @Override
    public boolean isLongPressDragEnabled() {
        return true;
    }

    @Override
    public boolean isItemViewSwipeEnabled() {
        return true;
    }

    @Override
    public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
        if(actionState != ItemTouchHelper.ACTION_STATE_IDLE){
            if(viewHolder instanceof ItemTouchHelperViewHolder){
                ItemTouchHelperViewHolder itemViewHolder = (ItemTouchHelperViewHolder) viewHolder;
                itemViewHolder.onItemSelected();
            }
        }
        super.onSelectedChanged(viewHolder, actionState);
    }

    @Override
    public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        if(viewHolder instanceof ItemTouchHelperViewHolder){
            ItemTouchHelperViewHolder itemViewHolder = (ItemTouchHelperViewHolder) viewHolder;
            itemViewHolder.onItemClear();
        }
        super.clearView(recyclerView, viewHolder);
    }
}
