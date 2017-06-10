package com.example.bipain.boe_restaurantapp.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.bipain.boe_restaurantapp.QueueOrder;
import com.example.bipain.boe_restaurantapp.model.Order;
import com.example.bipain.boe_restaurantapp.R;
import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Created by BiPain on 6/4/2017.
 */

public class OrderAdapter extends BaseAdapter {
    private Activity activity;
    private LinkedList<QueueOrder> data;
    private static LayoutInflater inflater = null;

    public OrderAdapter(Activity activity, LinkedList<QueueOrder> data) {
        this.activity = activity;
        this.data = data;
        inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void setData(LinkedList<QueueOrder> data) {
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
            view = inflater.inflate(R.layout.order_row, null);
        }
        TextView txtNumberDish = (TextView) view.findViewById(R.id.txtnumberDish);
        TextView txtTotal = (TextView) view.findViewById(R.id.txtTotal);
        TextView txtOrderId = (TextView) view.findViewById(R.id.txtOrderId);

        QueueOrder queueOrder = new QueueOrder();
        queueOrder = data.get(position);

        txtNumberDish.setText(String.valueOf(queueOrder.getNumberDish()));
        txtTotal.setText(String.valueOf(queueOrder.getTotal()));
        txtOrderId.setText(String.valueOf(queueOrder.getOrderId()));

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

    public void addData(QueueOrder queueOrder) {
        data.add(queueOrder);
        notifyDataSetChanged();
    }
}
