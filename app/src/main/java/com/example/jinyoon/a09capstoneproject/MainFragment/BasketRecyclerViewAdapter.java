package com.example.jinyoon.a09capstoneproject.MainFragment;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.UiThread;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.example.jinyoon.a09capstoneproject.Database.MyFridgeDataHelper;
import com.example.jinyoon.a09capstoneproject.ItemTouchHelper.ItemTouchHelperAdapter;
import com.example.jinyoon.a09capstoneproject.ItemTouchHelper.ItemTouchHelperViewHolder;
import com.example.jinyoon.a09capstoneproject.R;
import com.example.jinyoon.a09capstoneproject.Database.MyFridgeDataContract.*;

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
//
//    @Override
//    public void onItemMove(int fromPosition, int toPosition) {
//
//        Cursor c = getCursor();
//        c.moveToPosition(fromPosition);
//        int fromOrder = c.getInt(c.getColumnIndex(ShopLIstEntry.COLUMN_ORDERS));
//
//        c.moveToPosition(toPosition);
//        int toOrder = c.getInt(c.getColumnIndex(ShopLIstEntry.COLUMN_ORDERS));
//
//        Log.e("!!!FORMIDIDID", String.valueOf(fromPosition) +"  "+ String.valueOf(fromOrder));
//        Log.e("!!!!TOIDIDID", String.valueOf(toPosition) + "   "+ String.valueOf(toOrder));
//
//        ContentValues cv = new ContentValues();
//        cv.put(ShopLIstEntry.COLUMN_ORDERS, toPosition);
//        mContext.getContentResolver().update(
//                ShopLIstEntry.CONTENT_URI,
//                cv,
//                "orders = ?",
//                new String[]{String.valueOf(fromPosition)}
//        );
//
//        if(toPosition< fromPosition){
//            for(int i =fromPosition; i>toPosition; i--){
//                c.moveToPosition(i);
//                int k = c.getInt(c.getColumnIndex(ShopLIstEntry.COLUMN_ORDERS));
//                String name = c.getString(c.getColumnIndex(ShopLIstEntry.COLUMN_GROCERY_NAME));
//                cv.put(ShopLIstEntry.COLUMN_ORDERS, k-1);
//                mContext.getContentResolver().update(
//                        ShopLIstEntry.CONTENT_URI,
//                        cv,
//                        "name = ?",
//                        new String[]{name}
//                );
//            }
//        }else if(fromPosition < toPosition){
//            for(int i = fromPosition; i<toPosition; i++){
//                c.moveToPosition(i);
//                int k = c.getInt(c.getColumnIndex(ShopLIstEntry.COLUMN_ORDERS));
//                String name = c.getString(c.getColumnIndex(ShopLIstEntry.COLUMN_GROCERY_NAME));
//                cv.put(ShopLIstEntry.COLUMN_ORDERS, k+1);
//                mContext.getContentResolver().update(
//                        ShopLIstEntry.CONTENT_URI,
//                        cv,
//                        "name = ?",
//                        new String[]{name}
//                );
//            }
//        }
//////////////////////////////////////////////////////////
//        notifyItemMoved(fromPosition,toPosition);

////        String fromId = Long.toString(c.getLong(c.getColumnIndex(ShopLIstEntry._ID)));
////        Log.e("!!!FORMIDIDID", fromId +"  "+ fromName);
//        c.moveToPosition(toPosition);
//        String toName = c.getString(c.getColumnIndex(ShopLIstEntry.COLUMN_GROCERY_NAME));
////        String toId = Long.toString(c.getLong(c.getColumnIndex(ShopLIstEntry._ID)));
////        Log.e("!!!!TOIDIDID", toId + "   "+ toName);
//
//        ContentValues cv1 = new ContentValues();
//        cv1.put(ShopLIstEntry.COLUMN_GROCERY_NAME, fromName);
//
//        mContext.getContentResolver().update(
//                ShopLIstEntry.CONTENT_URI,
//                cv1,
//                "name = ?",
//                new String[]{toName}
//        );
//
//        ContentValues cv2 = new ContentValues();
//        cv2.put(ShopLIstEntry.COLUMN_GROCERY_NAME, toName);
//
//        mContext.getContentResolver().update(
//                ShopLIstEntry.CONTENT_URI,
//                cv2,
//                "name = ?",
//                new String[]{fromName}
//        );
//
//        mThisAdapter.notifyDataSetChanged();
//    }

    @Override
    public void onItemDismissLeft(int position) {
        //Move Item to Fridge list when swiped to left

        //TODO: Modify the following code to get dialog to get expiration days and move to Fridge List
        Cursor c = getCursor();
        c.moveToPosition(position);
        String name = c.getString(c.getColumnIndex(ShopLIstEntry.COLUMN_GROCERY_NAME));

        mContext.getContentResolver().delete(
                ShopLIstEntry.CONTENT_URI,
                "name = ?",
                new String[]{name}
                );
        this.notifyItemRemoved(position);
        Toast.makeText(mContext, "Item moved to left", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onItemDismissRight(int position) {
    //Remove Item when an item is swiped to right
        Cursor c = getCursor();
        c.moveToPosition(position);
        String name = c.getString(c.getColumnIndex(ShopLIstEntry.COLUMN_GROCERY_NAME));

        mContext.getContentResolver().delete(
                ShopLIstEntry.CONTENT_URI,
                "name = ?",
                new String[]{name}
        );
        this.notifyItemRemoved(position);
        Toast.makeText(mContext, "Item moved to right", Toast.LENGTH_SHORT).show();
    }

    @Override
    public BasketRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_basket, parent, false);
//        mThisAdapter = new BasketRecyclerViewAdapter(mContext,mCursor);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, Cursor cursor) {
//        viewHolder.mView.setOnClickListener(new View.OnClickListener() {
//            //Add OnClick Action later
//            @Override
//            public void onClick(View v) {
//                Log.v(LOG_TAG, "RecyclerView item OnClick");
//            }
//        });
//        //Add what items to be included later... (after implementing database)
//        viewHolder.mItemCheckBox.setChecked(true);
//        viewHolder.mItemName.setText("Eggs");
        final String name = cursor.getString(cursor.getColumnIndex(ShopLIstEntry.COLUMN_GROCERY_NAME));
        viewHolder.mItemName.setText(name);

    }

    @Override
    public int getItemCount() {
//        SQLiteDatabase db = new MyFridgeDataHelper(mContext).getReadableDatabase();
//        int cnt = (int) DatabaseUtils.queryNumEntries(db, ShopLIstEntry.TABLE_NAME);
//        db.close();
//
//
//        return (mCursor==null? super.getItemCount(): cnt );

        return super.getItemCount();
    }





    public static class ViewHolder extends RecyclerView.ViewHolder
            implements ItemTouchHelperViewHolder, View.OnClickListener{
        private View mView;
        private TextView mItemName;
        private ImageView mIconView;
        private String LOG_TAG = this.getClass().getSimpleName();

        public ViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
            mItemName = (TextView) itemView.findViewById(R.id.basket_item_name);
            mIconView = (ImageView) itemView.findViewById(R.id.reorder_icon);

            mView.setOnClickListener(this);
//            mItemCheckBox = (CheckBox) itemView.findViewById(R.id.basket_checkBox);
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
                    ShopLIstEntry.CONTENT_URI,
                    null,
                    null,
                    null,
                    null
            );

            if(mCursor !=null){
                mCursor.moveToPosition(position);
                final String name = mCursor.getString(mCursor.getColumnIndex(ShopLIstEntry.COLUMN_GROCERY_NAME));
                Toast.makeText(mContext, name, Toast.LENGTH_SHORT).show();

                new MaterialDialog.Builder(mContext).title("Edit Item")
                        .content("Edit Item name")
                        .inputType(InputType.TYPE_CLASS_TEXT)
                        .input("", name, new MaterialDialog.InputCallback() {
                            @Override
                            public void onInput(@NonNull MaterialDialog dialog, CharSequence input) {
                                Cursor c= mContext.getContentResolver().query(
                                        ShopLIstEntry.CONTENT_URI,
                                        new String[]{ShopLIstEntry.COLUMN_GROCERY_NAME},
                                        ShopLIstEntry.COLUMN_GROCERY_NAME +" = ? ",
                                        new String[]{input.toString()},
                                        null
                                );
                                if(c!=null && c.getCount()!=0){
                                    Toast toast = Toast.makeText(mContext, mContext.getString(R.string.item_exist_msg), Toast.LENGTH_SHORT);
                                    toast.setGravity(Gravity.CENTER, Gravity.CENTER, 0);
                                    toast.show();
                                }else if(input.toString().equals("")){
                                    Toast toast = Toast.makeText(mContext, mContext.getString(R.string.no_input_msg), Toast.LENGTH_SHORT);
                                    toast.setGravity(Gravity.CENTER, Gravity.CENTER, 0);
                                    toast.show();
                                }else{
                                    ContentValues cv = new ContentValues();
                                    cv.put(ShopLIstEntry.COLUMN_GROCERY_NAME, input.toString());
                                    mContext.getContentResolver().update(
                                            ShopLIstEntry.CONTENT_URI,
                                            cv,
                                            "name = ? ",
                                            new String[]{name});
                                    mContext.getContentResolver().notifyChange(ShopLIstEntry.CONTENT_URI, null);
                                }
                                c.close();
                            }
                        }).show();
            }else{
                Log.e(LOG_TAG, "Cursor doesn't exist");
            }


        }
    }

}


