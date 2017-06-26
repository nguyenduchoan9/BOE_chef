package com.example.bipain.boe_restaurantapp.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
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
import com.example.bipain.boe_restaurantapp.model.StatusResponse;
import com.example.bipain.boe_restaurantapp.services.Services;
import com.example.bipain.boe_restaurantapp.utils.Constant;

import java.util.ArrayList;
import java.util.LinkedList;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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
        //setOrderArrayList();
        orderAdapter = new OrderAdapter(getActivity(), orders);

        if(0 < orders.size()){
            dishInOrderArrayList = orders.getFirst().getOrderDetail();
            orderDetailAdapter = new OrderDetailAdapter(getActivity(), dishInOrderArrayList);
        }else{
            orderDetailAdapter = new OrderDetailAdapter(getActivity(), new ArrayList<>());
        }

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
            removeOrder(0);
        });

        btRejected = (Button) view.findViewById(R.id.btReject);
        btRejected.setOnClickListener(v -> removeOrder(1));
    }

    public void removeOrder(int branch) {
        Services services = getServices();
        if (0 == branch) {
            services.markOrderAccept(selectedOrderId).enqueue(new Callback<StatusResponse>() {
                @Override
                public void onResponse(Call<StatusResponse> call, Response<StatusResponse> response) {
                    if (response.isSuccessful()) {
                        if (null != response.body()) {
                            refreshView();
                        }
                    }
                }

                @Override
                public void onFailure(Call<StatusResponse> call, Throwable t) {

                }
            });
        } else if (1 == branch) {
            services.markOrderReject(selectedOrderId).enqueue(new Callback<StatusResponse>() {
                @Override
                public void onResponse(Call<StatusResponse> call, Response<StatusResponse> response) {
                    if (response.isSuccessful()) {
                        if (null != response.body()) {
                            refreshView();
                        }
                    }
                }

                @Override
                public void onFailure(Call<StatusResponse> call, Throwable t) {

                }
            });
        }

    }

    private void refreshView() {
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

        if(null != orders && orders.size() > 0){
            dishInOrderArrayList = orders.getFirst().getOrderDetail();
            orderDetailAdapter.setData(dishInOrderArrayList);
        }else {
            orderDetailAdapter.setData(new ArrayList<>());
        }
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

    public void addOrder(QueueOrder queueOrder) {
        orderAdapter.addData(queueOrder);
    }

    public Services getServices() {
        return ((TabManagerActivity) getActivity()).getServices();
    }


    @Override
    public void onStart() {
        super.onStart();
        Log.d(Constant.LOG_TAG, "Order-onstart");
        setPos();
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(Constant.LOG_TAG, "Order-onResume");
    }

    private void setPos(){
        ((TabManagerActivity)getActivity()).setFragmentPos(1);
    }

    public void onKeySearchChange(String term){

    }
}
