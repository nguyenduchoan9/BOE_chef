package com.example.bipain.boe_restaurantapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TabManagerActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;

    HashMap<Integer, ArrayList<DishInOrder>> orders;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab_manager);


        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        viewPager = (ViewPager) findViewById(R.id.viewPager);
        setUpViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        setOrders();
    }

    public void setUpViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new OrderFragment(), "ORDER");
        adapter.addFragment(new MenuFragment(), "MENU");
        adapter.addFragment(new DishFragment(), "DISH");
        viewPager.setAdapter(adapter);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> fragmentList = new ArrayList<>();
        private final List<String> fragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return fragmentList.get(position);
        }

        @Override
        public int getCount() {
            return fragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            fragmentList.add(fragment);
            fragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return fragmentTitleList.get(position);
        }
    }

    public HashMap<Integer, ArrayList<DishInOrder>> getOrders() {
        return orders;
    }

    public void setOrders() {
        Dish dish1 = new Dish(1, "Vit Quay Bac Kinh");
        Dish dish2 = new Dish(2, "Ngheu xao sa ot");
        Dish dish3 = new Dish(3, "Oc quan xao dua");
        Dish dish4 = new Dish(4, "Be Thui Cau Mong");
        Dish dish5 = new Dish(5, "Banh trang cuon thit heo 2 dau da");
        Dish dish6 = new Dish(6, "De Nuong Nam Dinh");
        Dish dish7 = new Dish(7, "Ga deo le gion cay");
        Dish dish8 = new Dish(8, "Muc ham ruou sa ke");
        Dish dish9 = new Dish(9, "Vit Tiem thuoc bac");

        DishInOrder dishInOrder1 = new DishInOrder(1, dish1);
        DishInOrder dishInOrder2 = new DishInOrder(1, dish2);
        DishInOrder dishInOrder3 = new DishInOrder(3, dish4);
        DishInOrder dishInOrder4 = new DishInOrder(2, dish3);
        DishInOrder dishInOrder5 = new DishInOrder(1, dish5);
        DishInOrder dishInOrder6 = new DishInOrder(3, dish9);
        DishInOrder dishInOrder7 = new DishInOrder(1, dish8);
        DishInOrder dishInOrder8 = new DishInOrder(4, dish6);
        DishInOrder dishInOrder9 = new DishInOrder(5, dish7);

        ArrayList<DishInOrder> orderDetail1 = new ArrayList<>();
        orderDetail1.add(dishInOrder1);
        orderDetail1.add(dishInOrder2);

        ArrayList<DishInOrder> orderDetail2 = new ArrayList<>();
        orderDetail2.add(dishInOrder3);
        orderDetail2.add(dishInOrder4);
        orderDetail2.add(dishInOrder5);

        ArrayList<DishInOrder> orderDetail3 = new ArrayList<>();
        orderDetail3.add(dishInOrder6);

        ArrayList<DishInOrder> orderDetail4 = new ArrayList<>();
        orderDetail4.add(dishInOrder7);
        orderDetail4.add(dishInOrder8);
        orderDetail4.add(dishInOrder9);

        orders = new HashMap<>();
        orders.put(1, orderDetail1);
        orders.put(2, orderDetail2);
        orders.put(3, orderDetail3);
        orders.put(4, orderDetail4);
    }
}
