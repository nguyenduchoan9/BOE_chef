package com.example.bipain.boe_restaurantapp;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.example.bipain.boe_restaurantapp.model.Dish;

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
        ViewHolder viewHolder = null;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.dish_menu_row, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        Dish dish = new Dish();
        dish = data.get(position);
        viewHolder.txtDishId.setText(String.valueOf(dish.getCategoryId()));
        viewHolder.txtDishName.setText(dish.getName());

        Dish finalDish = dish;
        viewHolder.ckbDishIsAble.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onCheckBoxListener(finalDish.getDishId());
            }
        });
        return convertView;
    }

    class ViewHolder {
        public TextView txtDishId;
        public TextView txtDishName;
        public CheckBox ckbDishIsAble;

        private void intialView(View view) {
            txtDishId = (TextView) view.findViewById(R.id.txtDishId);
            txtDishName = (TextView) view.findViewById(R.id.txtDishName);
            ckbDishIsAble = (CheckBox) view.findViewById(R.id.ckbDishIsAble);
        }

        public ViewHolder(View view) {
            intialView(view);
        }
    }

    public interface DishAdaprerListener {
        void onCheckBoxListener(int dishId);
    }

    private DishAdaprerListener listener;

    public void setListener(DishAdaprerListener listener) {
        this.listener = listener;
    }
}
