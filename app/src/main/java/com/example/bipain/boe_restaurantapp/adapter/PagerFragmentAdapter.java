package com.example.bipain.boe_restaurantapp.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import com.example.bipain.boe_restaurantapp.R;
import com.example.bipain.boe_restaurantapp.fragment.DishFragment;
import com.example.bipain.boe_restaurantapp.fragment.MaterialFragment;
import com.example.bipain.boe_restaurantapp.fragment.OrderFragment;
import java.util.ArrayList;

/**
 * Created by hoang on 08/06/2017.
 */

public class PagerFragmentAdapter extends FragmentPagerAdapter {
    int[] title = {R.string.text_tab_dish, R.string.text_tab_material};
    private Context mContext;

    public PagerFragmentAdapter(FragmentManager fm, Context context) {
        super(fm);
        fragments = new ArrayList<>();
        fragments.add(new DishFragment());
        fragments.add(new MaterialFragment());
        this.mContext = context;
    }

    ArrayList<Fragment> fragments;

    @Override
    public Fragment getItem(int position) {
        if (0 == position) {
            return fragments.get(position);
        } else if (1 == position) {
            return fragments.get(position);
        }
        return null;
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mContext.getString(title[position]).toUpperCase();
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }
}
