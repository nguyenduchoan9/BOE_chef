package com.example.bipain.boe_restaurantapp.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import com.example.bipain.boe_restaurantapp.Category;
import com.example.bipain.boe_restaurantapp.CategoryAdapter;
import com.example.bipain.boe_restaurantapp.DishAdapter;
import com.example.bipain.boe_restaurantapp.R;
import com.example.bipain.boe_restaurantapp.activities.TabManagerActivity;
import com.example.bipain.boe_restaurantapp.model.Dish;
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

        view = inflater.inflate(R.layout.activity_menu_fragment, container, false);

        lvCategory = (ListView) view.findViewById(R.id.lvCategory);
        lvDishInMenu = (ListView) view.findViewById(R.id.lvDishMenu);
        lvCategory.setAdapter(categoryAdapter);
        lvDishInMenu.setAdapter(dishAdapter);

        return view;
    }
}
