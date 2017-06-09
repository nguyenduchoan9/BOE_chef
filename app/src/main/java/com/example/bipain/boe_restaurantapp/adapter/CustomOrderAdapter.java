package com.example.bipain.boe_restaurantapp.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.bipain.boe_restaurantapp.model.Order;
import com.example.bipain.boe_restaurantapp.R;
import java.util.List;

/**
 * Created by BiPain on 6/4/2017.
 */

public class CustomOrderAdapter extends ArrayAdapter {
    List<Order> orderList;
    Context context;

    public CustomOrderAdapter(Activity context, List<Order> orderList) {
        super(context, R.layout.order_row, orderList);
        this.orderList = orderList;
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row = layoutInflater.inflate(R.layout.order_row, null, true);
        TextView txtNumberDish = (TextView) row.findViewById(R.id.txtnumberDish);
        TextView txtTotal = (TextView) row.findViewById(R.id.txtTotal);

        Order order = new Order();
        order = orderList.get(position);

        txtNumberDish.setText(String.valueOf(order.getNumberDish()));
        txtTotal.setText(String.valueOf(order.getTotal()));

        return row;
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
