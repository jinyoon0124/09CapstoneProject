package com.example.jinyoon.a09capstoneproject.MainFragment;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.support.annotation.UiThread;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.example.jinyoon.a09capstoneproject.ItemTouchHelper.ItemTouchHelperAdapter;
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
    private final String LOG_TAG = getClass().getSimpleName();

    public BasketRecyclerViewAdapter(Context context, Cursor cursor) {
        super(context, cursor);
        this.mContext = context;
        this.mCursor = cursor;
    }

    @Override
    public void onItemMove(int fromPosition, int toPosition) {
        Cursor c = getCursor();
        c.moveToPosition(fromPosition);
        String fromName = c.getString(c.getColumnIndex(ShopLIstEntry.COLUMN_GROCERY_NAME));
        String fromId = Long.toString(c.getColumnIndex(ShopLIstEntry._ID));
        c.moveToPosition(toPosition);
        String toName = c.getString(c.getColumnIndex(ShopLIstEntry.COLUMN_GROCERY_NAME));
        String toId = Long.toString(c.getColumnIndex(ShopLIstEntry._ID));

        ContentValues cv = new ContentValues();
        cv.put(ShopLIstEntry.COLUMN_GROCERY_NAME, fromName);

        mContext.getContentResolver().update(
                ShopLIstEntry.CONTENT_URI,
                cv,
                "id = ?",
                new String[]{toId}
        );

        cv.put(ShopLIstEntry.COLUMN_GROCERY_NAME, toName);

        mContext.getContentResolver().update(
                ShopLIstEntry.CONTENT_URI,
                cv,
                "id = ?",
                new String[]{fromId}
        );

        notifyDataSetChanged();
    }

    @Override
    public void onItemDismiss(int position) {
        Cursor c = getCursor();
        c.moveToPosition(position);
        String name = c.getString(c.getColumnIndex(ShopLIstEntry.COLUMN_GROCERY_NAME));
        mContext.getContentResolver().delete(
                ShopLIstEntry.buildShopListUriwithName(name),
                null,
                null
                );
        notifyItemRemoved(position);
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        public View mView;
        public TextView mItemName;
        public CheckBox mItemCheckBox;

        public ViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
            mItemName = (TextView) itemView.findViewById(R.id.basket_item_name);
            mItemCheckBox = (CheckBox) itemView.findViewById(R.id.basket_checkBox);
        }

//        @Override
//        public void onItemSelected() {
//            itemView.setBackgroundColor(Color.LTGRAY);
//        }
//
//        @Override
//        public void onItemClear() {
//            itemView.setBackgroundColor(0);
//        }
    }


    @Override
    public BasketRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_basket, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final Cursor cursor) {
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
//        viewHolder.mItemCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                mContext.getContentResolver().delete(
//                        ShopLIstEntry.CONTENT_URI,
//                        ShopLIstEntry.COLUMN_GROCERY_NAME + " = ? ",
//                        new String[]{name}
//                );
//                new MaterialDialog.Builder(mContext).title("Days to Expiration")
//                        .content("How long will this grocery item last?")
//                        .inputType(InputType.TYPE_CLASS_NUMBER)
//                        .input("Test Guide", "", new MaterialDialog.InputCallback() {
//                            @Override
//                            public void onInput(MaterialDialog dialog, CharSequence input) {
//                                Log.e("!!!EXPIRATION MSG", "MSG ENTERED");
//                                Toast.makeText(mContext, name + " " + input.toString(), Toast.LENGTH_LONG).show();
//                            }
//
//                        }).show();
//            }
//        });
//        FragmentManager fm = ((AppCompatActivity) mContext).getSupportFragmentManager();
//        fm.

        if (cursor.getInt(cursor.getColumnIndex(ShopLIstEntry.COLUMN_CHECKER)) == 1) {
            viewHolder.mItemCheckBox.setChecked(true);
        } else if (cursor.getInt(cursor.getColumnIndex(ShopLIstEntry.COLUMN_CHECKER)) == 0) {
            viewHolder.mItemCheckBox.setChecked(false);
        } else {
            Log.e(LOG_TAG, "Checker variable not set");
        }

    }

    @Override
    public int getItemCount() {
        return super.getItemCount();
    }

}


