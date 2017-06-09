package com.example.bipain.boe_restaurantapp.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.bipain.boe_restaurantapp.model.Dish;
import com.example.bipain.boe_restaurantapp.R;
import java.util.ArrayList;

/**
 * Created by BiPain on 6/5/2017.
 */

public class DishMenuAdapter extends BaseAdapter {
    private Activity activity;
    private ArrayList<Dish> data;
    private static LayoutInflater inflater;

    public DishMenuAdapter(Activity activity, ArrayList<Dish> data) {
        this.activity = activity;
        this.data = data;
        inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            view = inflater.inflate(R.layout.menu_dish_row, null);
        }
        TextView txtDishName = (TextView) view.findViewById(R.id.txtDishName);

        Dish dish = new Dish();
        dish = data.get(position);

        txtDishName.setText(dish.getName());

        return view;
    }
}
