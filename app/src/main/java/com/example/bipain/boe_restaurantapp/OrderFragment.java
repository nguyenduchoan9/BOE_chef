package com.example.bipain.boe_restaurantapp;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class OrderFragment extends Fragment {

    private ListView lvOrder;
    private ListView lvDetail;
    private OrderAdapter orderAdapter;
    private OrderDetailAdapter orderDetailAdapter;
    private View view;
    private Button btAccepted;
    private Button btRejected;

    HashMap<Integer, ArrayList<DishInOrder>> orders;
    ArrayList<Order> orderArrayList;
    ArrayList<DishInOrder> dishInOrderArrayList;
    QueueDish queueDish;
    int selectedOrderId;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        queueDish = new QueueDish();

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

    public void setQueueDish() {
        ((TabManagerActivity) this.getActivity()).setQueueDish(queueDish);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.activity_order_fragment, container, false);
        } else {
            ViewGroup parent = (ViewGroup) view.getParent();
            parent.removeView(view);
        }
        lvOrder = (ListView) view.findViewById(R.id.lvOrder);
        lvDetail = (ListView) view.findViewById(R.id.lvDetail);
        lvOrder.setAdapter(orderAdapter);
        lvDetail.setAdapter(orderDetailAdapter);

        btAccepted = (Button) view.findViewById(R.id.btAccepted);
        btRejected = (Button) view.findViewById(R.id.btReject);
        return view;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        lvOrder.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView txtOrderId = (TextView) view.findViewById(R.id.txtOrderId);
                selectedOrderId = Integer.parseInt(txtOrderId.getText().toString());
                Toast.makeText(view.getContext(), "OrderId is: " + selectedOrderId, Toast.LENGTH_LONG).show();

                refreshLvDishInOrder(selectedOrderId);
            }
        });

        btAccepted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<DishInOrder> dishInOrders = orders.get(selectedOrderId);
                for (DishInOrder dish : dishInOrders) {
                    for (int i = 0; i < dish.getQuantity(); i++) {
                        DishInQueue dishInQueue = new DishInQueue();
                        dishInQueue.setOrderId(selectedOrderId);
                        dishInQueue.setDish(dish.getDish());
                        queueDish.addDishInQueue(dishInQueue);
                    }
                }
                removeOrder(selectedOrderId);
                setQueueDish();
            }
        });


        btRejected.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeOrder(selectedOrderId);
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

    public void removeOrder(int orderId) {
        orders.remove(orderId);
        setOrderArrayList();
        orderAdapter.setData(orderArrayList);
        lvOrder.setAdapter(orderAdapter);
        orderAdapter.notifyDataSetChanged();
        lvOrder.invalidateViews();

        refreshLvDishInOrder(orderId);
    }

    public void refreshLvDishInOrder(int orderId) {
        dishInOrderArrayList = orders.get(selectedOrderId);
        orderDetailAdapter.setData(dishInOrderArrayList);
        lvDetail.setAdapter(orderDetailAdapter);
        orderDetailAdapter.notifyDataSetChanged();
        lvDetail.invalidateViews();
    }
}
