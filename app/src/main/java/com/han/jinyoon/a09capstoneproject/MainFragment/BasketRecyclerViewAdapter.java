package com.han.jinyoon.a09capstoneproject.MainFragment;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.han.jinyoon.a09capstoneproject.ItemTouchHelper.ItemTouchHelperAdapter;
import com.han.jinyoon.a09capstoneproject.ItemTouchHelper.ItemTouchHelperViewHolder;
import com.han.jinyoon.a09capstoneproject.R;
import com.han.jinyoon.a09capstoneproject.Database.MyFridgeDataContract.*;

/**
 * Created by Jin Yoon on 10/12/2016.
 * Credit to skyfishjy gist:
 * https://gist.github.com/skyfishjy/443b7448f59be978bc59
 * for the code structure
 */

public class BasketRecyclerViewAdapter extends CursorRecyclerViewAdapter<BasketRecyclerViewAdapter.ViewHolder>
        implements ItemTouchHelperAdapter{

    private Context mContext;
    private Cursor mCursor;

    private final String LOG_TAG = getClass().getSimpleName();
//    private BasketRecyclerViewAdapter mThisAdapter;

    public BasketRecyclerViewAdapter(Context context, Cursor cursor) {
        super(context, cursor);
        this.mContext = context;
        this.mCursor = cursor;
    }

    @Override
    public void onItemDismissLeft(final int position, final RecyclerView rv) {
        //Move Item to Fridge list when swiped to left
        Cursor c = getCursor();
        c.moveToPosition(position);
        final String name = c.getString(c.getColumnIndex(ShopListEntry.COLUMN_GROCERY_NAME));
        c = mContext.getContentResolver().query(
                FridgeListEntry.CONTENT_URI,
                new String[]{FridgeListEntry.COLUMN_GROCERY_NAME},
                FridgeListEntry.COLUMN_GROCERY_NAME +" = ? ",
                new String[]{name},
                null);

        if(c==null || c.getCount()!=0){
            Toast toast =
                    Toast.makeText(mContext, mContext.getString(R.string.item_exist_msg), Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, Gravity.CENTER, 0);
            toast.show();
            notifyDataSetChanged();
        }else{
//            Log.e("!!!!!!!!!!!!!!!", String.valueOf(c.getCount()));
            new MaterialDialog.Builder(mContext)
                    .title(mContext.getString(R.string.basket_dialog_expiration_title))
                    .content(mContext.getString(R.string.basket_dialog_expiration_content))
                    .inputType(InputType.TYPE_CLASS_NUMBER)
                    .input("", "", new MaterialDialog.InputCallback() {
                        @Override
                        public void onInput(@NonNull MaterialDialog dialog, CharSequence input) {

                            if(input.toString().equals("")){
                                Toast toast =
                                        Toast.makeText(mContext, mContext.getString(R.string.no_days_input_msg), Toast.LENGTH_SHORT);
                                toast.setGravity(Gravity.CENTER, Gravity.CENTER, 0);
                                toast.show();

                            }else{
                                ContentValues cv = new ContentValues();
                                cv.put(FridgeListEntry.COLUMN_GROCERY_NAME, name);
                                cv.put(FridgeListEntry.COLUMN_EXPIRATION, Integer.parseInt(input.toString()));
                                cv.put(FridgeListEntry.COLUMN_INPUTDATEINMIL, String.valueOf(System.currentTimeMillis()));
                                mContext.getContentResolver().insert(
                                        FridgeListEntry.CONTENT_URI,
                                        cv
                                );
//                                mContext.getContentResolver().notifyChange(FridgeListEntry.CONTENT_URI, null);


                                mContext.getContentResolver().delete(
                                        ShopListEntry.CONTENT_URI,
                                        "name = ?",
                                        new String[]{name}
                                );
                                notifyItemRemoved(position);
                                Snackbar.make(rv, mContext.getString(R.string.item_move_to_fridge_msg), Snackbar.LENGTH_SHORT).show();

                            }

                        }
                    }).show();
        }
        c.close();
//        Toast.makeText(mContext, "Item moved to left", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onItemDismissRight(int position, RecyclerView rv) {
    //Remove Item when an item is swiped to right
        Cursor c = getCursor();
        c.moveToPosition(position);
        String name = c.getString(c.getColumnIndex(ShopListEntry.COLUMN_GROCERY_NAME));

        mContext.getContentResolver().delete(
                ShopListEntry.CONTENT_URI,
                "name = ?",
                new String[]{name}
        );
        mContext.getContentResolver().notifyChange(ShopListEntry.CONTENT_URI, null);
//        Toast.makeText(mContext, "Item moved to right", Toast.LENGTH_SHORT).show();
        Snackbar.make(rv, mContext.getString(R.string.remove_msg), Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public BasketRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_basket, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, Cursor cursor) {
        final String name = cursor.getString(cursor.getColumnIndex(ShopListEntry.COLUMN_GROCERY_NAME));
        viewHolder.mItemName.setText(name);
    }

    @Override
    public int getItemCount() {
        return super.getItemCount();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder
            implements ItemTouchHelperViewHolder, View.OnClickListener{
        private View mView;
        private TextView mItemName;
        private String LOG_TAG = this.getClass().getSimpleName();

        public ViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
            mItemName = (TextView) itemView.findViewById(R.id.basket_item_name);

            mView.setOnClickListener(this);
        }

        //Called when an item is selected
        @Override
        public void onItemSelected() {
            mView.setBackgroundColor(Color.LTGRAY);
        }

        //Called when selection of item is released
        @Override
        public void onItemClear() {
            mView.setBackgroundColor(Color.WHITE);
        }

        @Override
        public void onClick(final View v) {
            final int position = getAdapterPosition();
            final Context mContext = v.getContext();
            Cursor mCursor = mContext.getContentResolver().query(
                    ShopListEntry.CONTENT_URI,
                    null,
                    null,
                    null,
                    null
            );

            if(mCursor !=null){
                mCursor.moveToPosition(position);
                final String name = mCursor.getString(mCursor.getColumnIndex(ShopListEntry.COLUMN_GROCERY_NAME));
//                Toast.makeText(mContext, name, Toast.LENGTH_SHORT).show();

                new MaterialDialog.Builder(mContext).title(mContext.getString(R.string.basket_dialog_edit_tile))
                        .content(mContext.getString(R.string.basket_dialog_edit_content))
                        .inputType(InputType.TYPE_CLASS_TEXT)
                        .input("", name, new MaterialDialog.InputCallback() {
                            @Override
                            public void onInput(@NonNull MaterialDialog dialog, CharSequence input) {
                                Cursor c= mContext.getContentResolver().query(
                                        ShopListEntry.CONTENT_URI,
                                        new String[]{ShopListEntry.COLUMN_GROCERY_NAME},
                                        ShopListEntry.COLUMN_GROCERY_NAME +" = ? ",
                                        new String[]{input.toString()},
                                        null
                                );
                                if(c!=null && c.getCount()>1){
                                    Toast toast = Toast.makeText(mContext, mContext.getString(R.string.item_exist_msg), Toast.LENGTH_SHORT);
                                    toast.setGravity(Gravity.CENTER, Gravity.CENTER, 0);
                                    toast.show();
                                }else if(input.toString().equals("")){
                                    Toast toast = Toast.makeText(mContext, mContext.getString(R.string.no_input_msg), Toast.LENGTH_SHORT);
                                    toast.setGravity(Gravity.CENTER, Gravity.CENTER, 0);
                                    toast.show();
                                }else{
                                    ContentValues cv = new ContentValues();
                                    cv.put(ShopListEntry.COLUMN_GROCERY_NAME, input.toString());
                                    mContext.getContentResolver().update(
                                            ShopListEntry.CONTENT_URI,
                                            cv,
                                            "name = ? ",
                                            new String[]{name});
//                                    mContext.getContentResolver().notifyChange(ShopListEntry.CONTENT_URI, null);
                                    Toast.makeText(mContext, mContext.getString(R.string.item_modified_msg), Toast.LENGTH_SHORT).show();
                                }
                                c.close();
                            }
                        }).show();
                mCursor.close();
            }
            else{
                Log.e(LOG_TAG, "Cursor doesn't exist");
            }


        }
    }

}


