package com.example.bipain.boe_restaurantapp.fragment;

import android.graphics.Color;
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
import com.example.bipain.boe_restaurantapp.QueueOrder;
import com.example.bipain.boe_restaurantapp.R;
import com.example.bipain.boe_restaurantapp.activities.TabManagerActivity;
import com.example.bipain.boe_restaurantapp.adapter.OrderAdapter;
import com.example.bipain.boe_restaurantapp.adapter.OrderDetailAdapter;

import java.util.ArrayList;
import java.util.LinkedList;

public class OrderFragment extends Fragment {

    private ListView lvOrder;
    private ListView lvDetail;
    private OrderAdapter orderAdapter;
    private OrderDetailAdapter orderDetailAdapter;
    private Button btAccepted;
    private Button btRejected;

    private LinkedList<QueueOrder> orders;
    private ArrayList<DishInOrder> dishInOrderArrayList;
    private int selectedOrderId;
    private View preView = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        orders = new LinkedList<>();
        dishInOrderArrayList = new ArrayList<>();


        orders = ((TabManagerActivity) this.getActivity()).getOrders();
        //setOrderArrayList();
        orderAdapter = new OrderAdapter(getActivity(), orders);

        dishInOrderArrayList = orders.getFirst().getOrderDetail();
        orderDetailAdapter = new OrderDetailAdapter(getActivity(), dishInOrderArrayList);
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

        lvOrder.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView txtOrderId = (TextView) view.findViewById(R.id.txtOrderId);
                selectedOrderId = Integer.parseInt(txtOrderId.getText().toString());
                Toast.makeText(getActivity(), "OrderId:" + selectedOrderId, Toast.LENGTH_SHORT).show();

                if (preView != null) {
                    TextView txtTitleNumberDish = (TextView) preView.findViewById(R.id.txtTitleNumberDish);
                    TextView txtnumberDish = (TextView) preView.findViewById(R.id.txtnumberDish);
                    TextView txtTitleTotal = (TextView) preView.findViewById(R.id.txtTitleTotal);
                    TextView txtTotal = (TextView) preView.findViewById(R.id.txtTotal);

                    preView.setBackgroundColor(Color.WHITE);
                    txtnumberDish.setTextColor(Color.BLACK);
                    txtTitleNumberDish.setTextColor(Color.BLACK);
                    txtTitleTotal.setTextColor(Color.BLACK);
                    txtTotal.setTextColor(Color.BLACK);
                }
                preView = view;

                refreshLvDishInOrder();

                TextView txtTitleNumberDish = (TextView) view.findViewById(R.id.txtTitleNumberDish);
                TextView txtnumberDish = (TextView) view.findViewById(R.id.txtnumberDish);
                TextView txtTitleTotal = (TextView) view.findViewById(R.id.txtTitleTotal);
                TextView txtTotal = (TextView) view.findViewById(R.id.txtTotal);

                view.setBackgroundColor(Color.BLACK);
                txtnumberDish.setTextColor(Color.WHITE);
                txtTitleNumberDish.setTextColor(Color.WHITE);
                txtTitleTotal.setTextColor(Color.WHITE);
                txtTotal.setTextColor(Color.WHITE);
            }
        });


        btAccepted = (Button) view.findViewById(R.id.btAccepted);
        btAccepted.setOnClickListener(v -> {
            removeOrder();
        });

        btRejected = (Button) view.findViewById(R.id.btReject);
        btRejected.setOnClickListener(v -> removeOrder());
    }

    public void removeOrder() {

        for (QueueOrder queueOrder : orders) {
            if (queueOrder.getOrderId() == selectedOrderId) {
                orders.remove(queueOrder);
                break;
            }
        }
        orderAdapter.setData(orders);
        lvOrder.setAdapter(orderAdapter);
        orderAdapter.notifyDataSetChanged();
        lvOrder.invalidateViews();

        dishInOrderArrayList = orders.getFirst().getOrderDetail();
        orderDetailAdapter.setData(dishInOrderArrayList);
        lvDetail.setAdapter(orderDetailAdapter);
        orderDetailAdapter.notifyDataSetChanged();
        lvDetail.invalidateViews();
    }

    public void refreshLvDishInOrder() {
        for (QueueOrder queueOrder : orders) {
            if (queueOrder.getOrderId() == selectedOrderId) {
                dishInOrderArrayList = queueOrder.getOrderDetail();
            }
        }
        orderDetailAdapter.setData(dishInOrderArrayList);
        lvDetail.setAdapter(orderDetailAdapter);
        orderDetailAdapter.notifyDataSetChanged();
        lvDetail.invalidateViews();
    }
}
