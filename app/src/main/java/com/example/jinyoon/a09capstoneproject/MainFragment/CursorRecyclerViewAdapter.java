package com.example.jinyoon.a09capstoneproject.MainFragment;

import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.database.DataSetObserver;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;

/**
 * Created by Jin Yoon on 11/13/16.
 * Credit to skyfishjy gist:
 * https://gist.github.com/skyfishjy/443b7448f59be978bc59
 * for the code structure
 */

public abstract class CursorRecyclerViewAdapter<VH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<VH> {
//    private Cursor mCursor;
//
//    public void swapCursor(final Cursor cursor){
//        this.mCursor = cursor;
//        this.notifyDataSetChanged();
//    }
//
//    public Cursor getItem(final int position){
//        if(this.mCursor!=null && !this.mCursor.isClosed()){
//            this.mCursor.moveToPosition(position);
//        }
//        return this.mCursor;
//    }
//
//    public Cursor getCursor(){
//        return this.mCursor;
//    }
//
//
//    @Override
//    public int getItemCount() {
//        return this.mCursor !=null
//                ? this.mCursor.getCount()
//                : 0;
//    }
//
//    @Override
//    public void onBindViewHolder(VH holder, int position) {
//        final Cursor cursor = this.getItem(position);
//        this.onBindViewHolder(holder, cursor);
//    }
//
//    public abstract void onBindViewHolder(final VH holder, final Cursor cursor);
//}


    private static final String LOG_TAG = CursorRecyclerViewAdapter.class.getSimpleName();
    private Cursor mCursor;
    private boolean dataIsValid;
    private int rowIdColumn;
    private DataSetObserver mDataSetObserver;
    private ContentObserver mContentObserver;

    public CursorRecyclerViewAdapter(Context context, Cursor cursor) {
        mCursor = cursor;
        dataIsValid = cursor != null;
        rowIdColumn = dataIsValid ? mCursor.getColumnIndex("_id") : -1;
        mDataSetObserver = new NotifyingDataSetObserver();
        if (dataIsValid) {
            mCursor.registerDataSetObserver(mDataSetObserver);
        }
    }

    public Cursor getCursor() {
        return mCursor;
    }

    @Override
    public int getItemCount() {
        if (dataIsValid && mCursor != null) {
            return mCursor.getCount();
        }
        return 0;
    }

    @Override
    public long getItemId(int position) {
        if (dataIsValid && mCursor != null && mCursor.moveToPosition(position)) {
            return mCursor.getLong(rowIdColumn);
        }
        return 0;
    }

    @Override
    public void setHasStableIds(boolean hasStableIds) {
        super.setHasStableIds(true);
    }

    public abstract void onBindViewHolder(VH viewHolder, Cursor cursor);

    @Override
    public void onBindViewHolder(VH viewHolder, int position) {
        if (!dataIsValid) {
            throw new IllegalStateException("This should only be called when Cursor is valid");
        }
        if (!mCursor.moveToPosition(position)) {
            throw new IllegalStateException("Could not move Cursor to position: " + position);
        }

        onBindViewHolder(viewHolder, mCursor);
    }

    public Cursor swapCursor(Cursor newCursor) {
        if (newCursor == mCursor) {
            return null;
        }
        final Cursor oldCursor = mCursor;
        if (oldCursor != null && mDataSetObserver != null) {
            oldCursor.unregisterDataSetObserver(mDataSetObserver);
        }
        mCursor = newCursor;
        if (mCursor != null) {
            if (mDataSetObserver != null) {
                mCursor.registerDataSetObserver(mDataSetObserver);
            }
            rowIdColumn = newCursor.getColumnIndexOrThrow("_id");
            dataIsValid = true;

            notifyDataSetChanged();
        } else {
            rowIdColumn = -1;
            dataIsValid = false;
            notifyDataSetChanged();
        }
        return oldCursor;
    }

    private class NotifyingDataSetObserver extends DataSetObserver {
        @Override
        public void onChanged() {
            super.onChanged();
            dataIsValid = true;
            notifyDataSetChanged();
        }

        @Override
        public void onInvalidated() {
            super.onInvalidated();
            dataIsValid = false;
            notifyDataSetChanged();
        }
    }
}

