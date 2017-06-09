package com.example.bipain.boe_restaurantapp.fragment;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bipain.boe_restaurantapp.DishInOrder;
import com.example.bipain.boe_restaurantapp.model.Order;
import com.example.bipain.boe_restaurantapp.adapter.OrderAdapter;
import com.example.bipain.boe_restaurantapp.adapter.OrderDetailAdapter;
import com.example.bipain.boe_restaurantapp.R;
import com.example.bipain.boe_restaurantapp.activities.TabManagerActivity;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class OrderFragment extends Fragment {

    private ListView listOrder;
    private ListView listDetail;
    private OrderAdapter orderAdapter;
    private OrderDetailAdapter orderDetailAdapter;

    HashMap<Integer, ArrayList<DishInOrder>> orders;
    ArrayList<Order> orderArrayList;
    ArrayList<DishInOrder> dishInOrderArrayList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (orders == null) {
            orders = new HashMap<>();
        }
        orders = ((TabManagerActivity) this.getActivity()).getOrders();
        setOrderArrayList();
        orderAdapter = new OrderAdapter(getActivity(), orderArrayList);

        if (dishInOrderArrayList == null) {
            dishInOrderArrayList = new ArrayList<>();
        }
        dishInOrderArrayList = orders.get(orderArrayList.get(0).getOrderId());
        orderDetailAdapter = new OrderDetailAdapter(getActivity(), dishInOrderArrayList);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_order_fragment, container, false);

        listOrder = (ListView) rootView.findViewById(R.id.lvOrder);
        listDetail = (ListView) rootView.findViewById(R.id.lvDetail);
        listOrder.setAdapter(orderAdapter);
        listDetail.setAdapter(orderDetailAdapter);

        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        listOrder.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView txtOrderId = (TextView) view.findViewById(R.id.txtOrderId);
                int orderId = Integer.parseInt(txtOrderId.getText().toString());
                Toast.makeText(view.getContext(), "OrderId is: " + orderId, Toast.LENGTH_LONG).show();
                dishInOrderArrayList = orders.get(orderId);
                orderDetailAdapter.setData(dishInOrderArrayList);
                listDetail.setAdapter(orderDetailAdapter);
                orderDetailAdapter.notifyDataSetChanged();
                listDetail.invalidateViews();
            }
        });
    }

    public void setOrderArrayList() {
        if (orderArrayList == null) {
            orderArrayList = new ArrayList<>();
        }
        for (Map.Entry<Integer, ArrayList<DishInOrder>> entry : orders.entrySet()) {
            int orderId = entry.getKey();
            int numberDish = 0;
            int total = 0;
            ArrayList<DishInOrder> dishInOrders = entry.getValue();
            for (DishInOrder dishInOrder : dishInOrders) {
                numberDish += 1;
                total += dishInOrder.getQuantity();
            }
            Order order = new Order(numberDish, total, orderId);
            orderArrayList.add(order);
        }
    }
}
