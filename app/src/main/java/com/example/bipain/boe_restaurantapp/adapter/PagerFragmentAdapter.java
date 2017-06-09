package com.example.bipain.boe_restaurantapp.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import com.example.bipain.boe_restaurantapp.fragment.DishFragment;
import com.example.bipain.boe_restaurantapp.fragment.MenuFragment;
import com.example.bipain.boe_restaurantapp.fragment.OrderFragment;

/**
 * Created by hoang on 08/06/2017.
 */

public class PagerFragmentAdapter extends FragmentPagerAdapter {
    String[] title = {"order", "menu", "dish"};

    public PagerFragmentAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        if (0 == position) {
            return new OrderFragment();
        } else if (1 == position) {
            return new MenuFragment();
        } else if (2 == position) {
            return new DishFragment();
        }
        return null;
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return title[position].toUpperCase();
    }
}
