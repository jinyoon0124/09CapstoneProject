package com.example.jinyoon.a09capstoneproject.MainFragment;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.support.annotation.UiThread;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.util.Log;
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

    private final String mSelectedItemKey = "SelectedItem";
    private final String mSelectedItemNameKey = "SelectedItemName";
    private final String mSelectedItemCheckerKey = "SelectedItemChecker";
    private Context mContext;
    private Cursor mCursor;
    private static final int CURSOR_LOADER_ID = 0;

    private final String LOG_TAG = getClass().getSimpleName();
    private BasketRecyclerViewAdapter mThisAdapter;
    private Fragment mFragment;

    public BasketRecyclerViewAdapter(Context context, Cursor cursor, Fragment fg) {
        super(context, cursor);
        this.mContext = context;
        this.mCursor = cursor;
        this.mFragment = fg;

    }
//
//    @Override
//    public void onItemMove(int fromPosition, int toPosition) {
//
//        Cursor c = getCursor();
//        c.moveToPosition(fromPosition);
//        String fromName = c.getString(c.getColumnIndex(ShopLIstEntry.COLUMN_GROCERY_NAME));
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
//
//    public void updateUI(){
//        FragmentTransaction ft = mFragment.getFragmentManager().beginTransaction();
//        ft.detach(mFragment).attach(mFragment).commit();
//
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
        mThisAdapter = new BasketRecyclerViewAdapter(mContext,mCursor,mFragment);

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
            implements ItemTouchHelperViewHolder{
        private View mView;
        private TextView mItemName;
        public CheckBox mItemCheckBox;
        private ImageView mIconView;


        public ViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
            mItemName = (TextView) itemView.findViewById(R.id.basket_item_name);
            mIconView = (ImageView) itemView.findViewById(R.id.reorder_icon);
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
    }

}


