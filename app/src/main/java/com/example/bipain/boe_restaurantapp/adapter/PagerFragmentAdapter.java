package com.example.bipain.boe_restaurantapp.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import com.example.bipain.boe_restaurantapp.fragment.DishFragment;
import com.example.bipain.boe_restaurantapp.fragment.MenuFragment;
import com.example.bipain.boe_restaurantapp.fragment.OrderFragment;
import java.util.ArrayList;

/**
 * Created by hoang on 08/06/2017.
 */

public class PagerFragmentAdapter extends FragmentPagerAdapter {
    String[] title = {"order", "menu", "dish"};

    public PagerFragmentAdapter(FragmentManager fm) {
        super(fm);
        fragments = new ArrayList<>();
        fragments.add(new OrderFragment());
        fragments.add(new MenuFragment());
        fragments.add(new DishFragment());
    }

    ArrayList<Fragment> fragments;

    @Override
    public Fragment getItem(int position) {
        if (0 == position) {
            return fragments.get(position);
        } else if (1 == position) {
            return fragments.get(position);
        } else if (2 == position) {
            return fragments.get(position);
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

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }
}
