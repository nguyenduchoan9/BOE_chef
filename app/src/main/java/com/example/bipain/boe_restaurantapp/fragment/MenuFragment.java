package com.example.bipain.boe_restaurantapp.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.bipain.boe_restaurantapp.Category;
import com.example.bipain.boe_restaurantapp.CategoryAdapter;
import com.example.bipain.boe_restaurantapp.DishAdapter;
import com.example.bipain.boe_restaurantapp.R;
import com.example.bipain.boe_restaurantapp.activities.TabManagerActivity;
import com.example.bipain.boe_restaurantapp.model.Dish;
import com.example.bipain.boe_restaurantapp.utils.ToastUtils;

import java.util.ArrayList;

public class MenuFragment extends Fragment {

    private ListView lvCategory;
    private ListView lvDishInMenu;

    DishAdapter dishAdapter;
    CategoryAdapter categoryAdapter;
    ArrayList<Dish> dishArrayList;
    ArrayList<Category> categoryArrayList;
    ArrayList<Dish> selectedDishes;
    View preView = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        selectedDishes = new ArrayList<>();
        categoryArrayList = new ArrayList<Category>();
        dishArrayList = new ArrayList<Dish>();
        categoryArrayList = ((TabManagerActivity) this.getActivity()).getCategories();
        dishArrayList = ((TabManagerActivity) this.getActivity()).getDishes();

        setDishAdapter(categoryArrayList.get(0).getId());
        dishAdapter = new DishAdapter(getActivity(), selectedDishes);
        categoryAdapter = new CategoryAdapter(getActivity(), categoryArrayList);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.activity_menu_fragment, container, false);

        lvCategory = (ListView) view.findViewById(R.id.lvCategory);
        lvDishInMenu = (ListView) view.findViewById(R.id.lvDishMenu);
        lvCategory.setAdapter(categoryAdapter);
        lvDishInMenu.setAdapter(dishAdapter);

        lvCategory.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView txtCategoryId = (TextView) view.findViewById(R.id.txtCategoryId);
                int categoryId = Integer.parseInt(txtCategoryId.getText().toString());

                if (preView != null) {
                    TextView txtCategoryName = (TextView) preView.findViewById(R.id.txtCategoryName);
                    txtCategoryName.setTextColor(Color.BLACK);
                    preView.setBackgroundColor(Color.WHITE);
                }
                preView = view;
                setDishAdapter(categoryId);
                dishAdapter.setData(selectedDishes);
                dishAdapter.notifyDataSetChanged();

                TextView txtCategoryName = (TextView) view.findViewById(R.id.txtCategoryName);
                txtCategoryName.setTextColor(Color.BLACK);
                view.setBackgroundColor(Color.WHITE);
            }
        });

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        dishAdapter.setListener(new DishAdapter.DishAdaprerListener() {
            @Override
            public void onCheckBoxListener(int dishId) {
                ToastUtils.toastShortMassage(getContext(), "DishID: " + dishId);
            }
        });
    }

    public void setDishAdapter(int categoryId) {
        selectedDishes.clear();
        for (Dish dish : dishArrayList) {
            if (dish.getCategoryId() == categoryId) {
                selectedDishes.add(dish);
            }
        }
    }
}
