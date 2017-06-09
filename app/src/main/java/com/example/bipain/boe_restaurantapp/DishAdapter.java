package com.example.bipain.boe_restaurantapp;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by BiPain on 6/8/2017.
 */

public class DishAdapter extends BaseAdapter {
    private Activity activity;
    private ArrayList<Dish> data;
    private static LayoutInflater inflater = null;

    public DishAdapter(Activity activity, ArrayList<Dish> data) {
        this.activity = activity;
        this.data = data;
        inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void setData(ArrayList<Dish> data) {
        this.data = data;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (convertView == null) {
            view = inflater.inflate(R.layout.category_row, null);
        }

        TextView txtCategoryId = (TextView) view.findViewById(R.id.txtCategoryId);
        TextView txtCategoryName = (TextView) view.findViewById(R.id.txtCategoryName);

        Dish dish = new Dish();
        dish = data.get(position);
        txtCategoryId.setText(String.valueOf(dish.getCategoryId()));
        txtCategoryName.setText(dish.getName());

        return view;
    }
}
