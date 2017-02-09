package com.han.jinyoon.a09capstoneproject;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;

import com.han.jinyoon.a09capstoneproject.MainFragment.BasketFragment;
import com.han.jinyoon.a09capstoneproject.MainFragment.FridgeFragment;
import com.han.jinyoon.a09capstoneproject.MainFragment.RecipeFragment;

/**
 * PageAdapter for fragments
 */

public class PageAdapter extends FragmentPagerAdapter {

    private int[] imageResId = {
            R.drawable.ic_fridge_black_tab,
            R.drawable.ic_basket_black_tab,
            R.drawable.ic_recipe_black_tab
    };

    private Context context;

    public PageAdapter(FragmentManager fm) {
        super(fm);
    }

    public PageAdapter(Context ct, FragmentManager fm){
        super(fm);
        this.context = ct;
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;

        switch (position){
            case 0:
                fragment = new FridgeFragment();
                break;
            case 1:
                fragment = new BasketFragment();
                break;
            case 2:
                fragment = new RecipeFragment();
                break;
        }

        return fragment;
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        Drawable image = ContextCompat.getDrawable(context,imageResId[position]);
        image.setBounds(0,0,120, 120);
        SpannableString sb = new SpannableString(" ");
        ImageSpan imageSpan = new ImageSpan(image, ImageSpan.ALIGN_BOTTOM);
        sb.setSpan(imageSpan, 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        return sb;
    }
}
