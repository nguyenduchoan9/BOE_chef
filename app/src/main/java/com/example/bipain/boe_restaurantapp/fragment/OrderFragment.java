package com.example.bipain.boe_restaurantapp.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.example.bipain.boe_restaurantapp.DishInOrder;
import com.example.bipain.boe_restaurantapp.DishInQueue;
import com.example.bipain.boe_restaurantapp.QueueDish;
import com.example.bipain.boe_restaurantapp.R;
import com.example.bipain.boe_restaurantapp.activities.TabManagerActivity;
import com.example.bipain.boe_restaurantapp.adapter.OrderAdapter;
import com.example.bipain.boe_restaurantapp.adapter.OrderDetailAdapter;
import com.example.bipain.boe_restaurantapp.model.Order;
import java.util.ArrayList;
import java.util.HashMap;
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
        View rootView = inflater.inflate(R.layout.activity_order_fragment, container, false);

        lvOrder = (ListView) rootView.findViewById(R.id.lvOrder);
        lvDetail = (ListView) rootView.findViewById(R.id.lvDetail);
        lvOrder.setAdapter(orderAdapter);
        lvDetail.setAdapter(orderDetailAdapter);

        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        lvOrder.setOnItemClickListener((parent, view1, position, id) -> {
            TextView txtOrderId = (TextView) view1.findViewById(R.id.txtOrderId);
            selectedOrderId = Integer.parseInt(txtOrderId.getText().toString());
            Toast.makeText(view1.getContext(), "OrderId is: " + selectedOrderId, Toast.LENGTH_LONG).show();

            refreshLvDishInOrder(selectedOrderId);
        });

        btAccepted = (Button) view.findViewById(R.id.btAccepted);
        btAccepted.setOnClickListener(v -> {
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
        });

        btRejected = (Button) view.findViewById(R.id.btReject);
        btRejected.setOnClickListener(v -> removeOrder(selectedOrderId));
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
