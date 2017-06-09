package com.example.bipain.boe_restaurantapp;

import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.LinkedList;

public class DishFragment extends Fragment {

    private ListView lvDishInQueue;
    private DishQueueAdapter dishQueueAdapter;
    QueueDish queueDish;
    ArrayList<DishInOrder> dishInOrders;

    View view;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        queueDish = new QueueDish();
        queueDish = ((TabManagerActivity) this.getActivity()).getQueueDish();
        dishInOrders = new ArrayList<>();
        setDishInOrders();
        dishQueueAdapter = new DishQueueAdapter(getActivity(), dishInOrders);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.activity_dish_fragment, container, false);
        } else {
            ViewGroup parent = (ViewGroup) view.getParent();
            parent.removeView(view);
        }
        lvDishInQueue = (ListView) view.findViewById(R.id.lvQueueDish);
        lvDishInQueue.setAdapter(dishQueueAdapter);

        return view;
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        lvDishInQueue.setOnContextClickListener(new View.OnContextClickListener() {
            @Override
            public boolean onContextClick(View v) {
                CheckBox ckbDishIsCooked = (CheckBox) v.findViewById(R.id.ckbQueueDish);
                if (ckbDishIsCooked.isChecked()) {
                    TextView txtDishId = (TextView) v.findViewById(R.id.txtDishId);
                    int distId = Integer.parseInt(txtDishId.getText().toString());
                    for (DishInQueue dishInQueue : queueDish.getQueues()) {
                        if (dishInQueue.getDish().getDishId() == distId) {
                            queueDish.getQueues().remove(dishInQueue);

                            refreshListViewDish();

                            return true;
                        }
                    }
                }
                return false;
            }
        });
    }

    public void setDishInOrders() {
        QueueDish temp = new QueueDish();
        temp = queueDish;
        for (DishInQueue dishInQueue : temp.getQueues()) {
            int i = 1;
            DishInOrder dishInOrder = new DishInOrder();
            dishInOrder.setDish(dishInQueue.getDish());
            for (DishInQueue diq : queueDish.getQueues()) {
                if (diq.getDish().getDishId() == dishInQueue.getDish().getDishId()) {
                    i += 1;
                    temp.getQueues().remove(diq);
                }
            }
            temp.getQueues().remove(dishInQueue);
            dishInOrder.setQuantity(i);

            dishInOrders.add(dishInOrder);
        }
    }

    public void refreshListViewDish() {
        setDishInOrders();
        dishQueueAdapter.setData(dishInOrders);
        lvDishInQueue.setAdapter(dishQueueAdapter);
        dishQueueAdapter.notifyDataSetChanged();
        lvDishInQueue.invalidateViews();
    }
}
