package com.example.bipain.boe_restaurantapp;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;

public class MenuFragment extends Fragment {

    private ListView lvCategory;
    private ListView lvDishInMenu;

    DishAdapter dishAdapter;
    CategoryAdapter categoryAdapter;
    ArrayList<Dish> dishArrayList;
    ArrayList<Category> categoryArrayList;
    View view;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        categoryArrayList = new ArrayList<Category>();
        dishArrayList = new ArrayList<Dish>();
        categoryArrayList = ((TabManagerActivity) this.getActivity()).getCategories();
        dishArrayList = ((TabManagerActivity) this.getActivity()).getDishes();

        dishAdapter = new DishAdapter(getActivity(), dishArrayList);
        categoryAdapter = new CategoryAdapter(getActivity(), categoryArrayList);

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.activity_menu_fragment, container, false);
        } else {
            ViewGroup parent = (ViewGroup) view.getParent();

            parent.removeView(view);
        }
        lvCategory.setAdapter(categoryAdapter);
        lvDishInMenu.setAdapter(dishAdapter);

        return view;
    }
}
