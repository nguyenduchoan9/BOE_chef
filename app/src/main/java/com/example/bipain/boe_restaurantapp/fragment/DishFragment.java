package com.example.bipain.boe_restaurantapp.fragment;

import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.bipain.boe_restaurantapp.DishInOrder;
import com.example.bipain.boe_restaurantapp.DishInQueue;
import com.example.bipain.boe_restaurantapp.DishQueueAdapter;
import com.example.bipain.boe_restaurantapp.R;
import com.example.bipain.boe_restaurantapp.activities.TabManagerActivity;
import com.example.bipain.boe_restaurantapp.utils.ToastUtils;

import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.LinkedList;

public class DishFragment extends Fragment {

    private ListView lvDishInQueue;
    private DishQueueAdapter dishQueueAdapter;
    private LinkedList<DishInQueue> queueDish;
    private ArrayList<DishInOrder> dishInOrders;

    View view;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        queueDish = new LinkedList<>();
        queueDish = ((TabManagerActivity) this.getActivity()).getQueueDish();
        dishInOrders = new ArrayList<>();
        setDishInOrders();
        dishQueueAdapter = new DishQueueAdapter(getActivity(), dishInOrders);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_dish_fragment, container, false);

        lvDishInQueue = (ListView) view.findViewById(R.id.lvQueueDish);
        lvDishInQueue.setAdapter(dishQueueAdapter);

        return view;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        dishQueueAdapter.setListener(new DishQueueAdapter.DishQueueAdapterListener() {
            @Override
            public void onDoneClick(int dishId) {
                removeDishIsCooked(dishId);
                ToastUtils.toastShortMassage(getContext(), "DishId: " + dishId);
            }
        });
    }

    public void setDishInOrders() {
        for (DishInQueue dishInQueue : queueDish) {
            int quantity = 0;
            DishInOrder dishInOrder = new DishInOrder();
            dishInOrder.setDish(dishInQueue.getDish());
            boolean isExited = false;
            for (DishInOrder dish : dishInOrders) {
                if (dish.getDish().getDishId() == dishInOrder.getDish().getDishId()) {
                    isExited = true;
                    break;
                }
            }
            if (!isExited) {
                for (DishInQueue dish : queueDish) {
                    if (dish.getDish().getDishId() == dishInOrder.getDish().getDishId()) {
                        quantity += 1;
                    }
                }
                dishInOrder.setQuantity(quantity);
                dishInOrders.add(dishInOrder);
            }
        }
    }

    public void refreshListViewDish() {
        dishInOrders.clear();
        setDishInOrders();
//        lvDishInQueue.setAdapter(dishQueueAdapter);
        dishQueueAdapter.setData(dishInOrders);
        dishQueueAdapter.notifyDataSetChanged();
//        lvDishInQueue.invalidateViews();
    }

    public void removeDishIsCooked(int dishId) {
        for (DishInQueue dishInQueue : queueDish) {
            if (dishInQueue.getDish().getDishId() == dishId) {
                queueDish.remove(dishInQueue);
                break;
            }
        }
        refreshListViewDish();
    }
}
