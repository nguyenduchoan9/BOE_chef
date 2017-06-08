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
 * Created by BiPain on 6/5/2017.
 */

public class OrderDetailAdapter extends BaseAdapter {
    private Activity activity;
    private ArrayList<DishInOrder> data;
    private static LayoutInflater layoutInflater = null;

    public OrderDetailAdapter(Activity activity, ArrayList<DishInOrder> data) {
        this.activity = activity;
        this.data = data;
        layoutInflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void setData(ArrayList<DishInOrder> data) {
        this.data = data;
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
        if (convertView == null) {
            view = layoutInflater.inflate(R.layout.detail_row, null);
        }
        TextView txtDishName = (TextView) view.findViewById(R.id.txtDishName);
        TextView txtQuantity = (TextView) view.findViewById(R.id.txtQuantity);
        DishInOrder dish = new DishInOrder();
        dish = data.get(position);

        txtDishName.setText(dish.getDish().getName());
        txtQuantity.setText(String.valueOf(dish.getQuantity()));

        return view;
    }
}
