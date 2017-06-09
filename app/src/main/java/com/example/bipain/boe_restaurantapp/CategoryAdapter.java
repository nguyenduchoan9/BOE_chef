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

public class CategoryAdapter extends BaseAdapter {
    private Activity activity;
    private ArrayList<Category> data;
    private static LayoutInflater inflater = null;

    public CategoryAdapter(Activity activity, ArrayList<Category> data) {
        this.activity = activity;
        this.data = data;
        inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void setData(ArrayList<Category> data) {
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
            view = inflater.inflate(R.layout.category_row, null);
        }
        TextView txtCategoryName = (TextView) view.findViewById(R.id.txtCategoryName);
        TextView txtCategoryId = (TextView) view.findViewById(R.id.txtCategoryId);

        Category category = new Category();
        category = data.get(position);

        txtCategoryId.setText(String.valueOf(category.getId()));
        txtCategoryName.setText(category.getName());

        return view;
    }
}
