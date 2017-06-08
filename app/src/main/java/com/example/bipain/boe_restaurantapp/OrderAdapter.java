package com.example.bipain.boe_restaurantapp;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by BiPain on 6/4/2017.
 */

public class OrderAdapter extends BaseAdapter {
    private Activity activity;
    private ArrayList<Order> data;
    private static LayoutInflater inflater = null;

    public OrderAdapter(Activity activity, ArrayList<Order> data) {
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
        if (convertView == null) {
            view = inflater.inflate(R.layout.order_row, null);
        }
        TextView txtNumberDish = (TextView) view.findViewById(R.id.txtnumberDish);
        TextView txtTotal = (TextView) view.findViewById(R.id.txtTotal);
        TextView txtOrderId = (TextView) view.findViewById(R.id.txtOrderId);

        Order order = new Order();
        order = data.get(position);

        txtNumberDish.setText(String.valueOf(order.getNumberDish()));
        txtTotal.setText(String.valueOf(order.getTotal()));
        txtOrderId.setText(String.valueOf(order.getOrderId()));

        return view;
    }

    public int setImageTable(int table) {
        switch (table) {
            case 1:
                return R.drawable.number1;
            case 2:
                return R.drawable.number2;
            case 3:
                return R.drawable.number3;
            case 4:
                return R.drawable.number4;
            case 5:
                return R.drawable.number5;
            case 6:
                return R.drawable.number6;
            case 7:
                return R.drawable.number7;
            case 8:
                return R.drawable.number8;
            case 9:
                return R.drawable.number9;
            default:
                return 0;
        }
    }
}
