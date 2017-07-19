package com.example.bipain.boe_restaurantapp.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.example.bipain.boe_restaurantapp.R;
import com.example.bipain.boe_restaurantapp.activities.SchedulerThread;
import com.example.bipain.boe_restaurantapp.activities.WaiterActivity;
import com.example.bipain.boe_restaurantapp.adapter.DishServeAdatper;
import com.example.bipain.boe_restaurantapp.model.DishNotification;
import com.example.bipain.boe_restaurantapp.model.GroupDishByTable;
import com.example.bipain.boe_restaurantapp.model.ServingDishGroup;
import com.example.bipain.boe_restaurantapp.model.StatusResponse;
import com.example.bipain.boe_restaurantapp.model.TableGroupServe;
import com.example.bipain.boe_restaurantapp.model.WaiterNotification;
import com.example.bipain.boe_restaurantapp.services.Services;
import com.example.bipain.boe_restaurantapp.utils.ToastUtils;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import rx.android.schedulers.AndroidSchedulers;
import rx.subjects.PublishSubject;

/**
 * A simple {@link Fragment} subclass.
 */
public class ServingFragment extends Fragment {

    private final static String GCM_TOKEN = "GCM_TOKEN";
    private Unbinder unbinder;
    private Handler myHandler;

    @BindView(R.id.tvTotal)
    TextView tvTotal;
    @BindView(R.id.rvDish)
    RecyclerView rvDish;
    DishServeAdatper mAdapter;
    Services services;
    @BindView(R.id.rlProcessing)
    RelativeLayout rlProcessing;

    private List<WaiterNotification> orginData;

    public ServingFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        services = getServices();
        mAdapter = new DishServeAdatper();
        mAdapter.setListner((pos, notify) -> {
            showProcessing();
            services.markOrderDetailServed(notify.getOrderDetailId()).enqueue(new Callback<StatusResponse>() {
                @Override
                public void onResponse(Call<StatusResponse> call, Response<StatusResponse> response) {
                    if (response.isSuccessful()) {
                        if (null != response.body()) {
                            setTotal();
                            getMySchedulerThread().addServedItem(notify);
                            mAdapter.removeData(notify);
                        }
                    }
                    hideProcessing();
                }

                @Override
                public void onFailure(Call<StatusResponse> call, Throwable t) {
                    hideProcessing();
                }
            });
        });
        myHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                WaiterNotification i = (WaiterNotification) msg.obj;
                if (i.isNotifyToWarning()) {
                    mAdapter.removeData(i);
                } else {
                    mAdapter.notifyLongTime(i);
                }
                setTotal();
//                Log.d(LOG_TAG, i.getDish().getDishName()
//                        + "-"
//                        + String.valueOf(i.getDish().getDishId()));
            }
        };

    }

    public Handler getMyHandler() {
        return myHandler;
    }

    private void setUpRecyclerView() {

        RecyclerView.LayoutManager manager = new LinearLayoutManager(getContext(),
                LinearLayoutManager.VERTICAL, false);
        DividerItemDecoration decoration = new DividerItemDecoration(getContext(),
                DividerItemDecoration.VERTICAL);
        rvDish.setLayoutManager(manager);
        rvDish.addItemDecoration(decoration);
        rvDish.setAdapter(mAdapter);
        rvDish.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                int a = ((LinearLayoutManager) manager).findFirstVisibleItemPosition();
                ((LinearLayoutManager) manager).findLastVisibleItemPosition();
            }
        });

        setTotal();

        scheduler = PublishSubject.create();
        scheduler
                .observeOn(AndroidSchedulers.mainThread())
                .takeUntil(endPoint)
                .subscribe(id -> ToastUtils.toastShortMassage(getContext(), "dmmmmmm"));
    }

    private void setTotal() {
        tvTotal.setText("Tổng số: " + String.valueOf(mAdapter.getItemCount()) + " dĩa");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_blank, container, false);
        unbinder = ButterKnife.bind(this, v);
        return v;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setUpRecyclerView();
    }

    @Override
    public void onStart() {
        super.onStart();
        setTotal();
//        for (WaiterNotification n : fakeData()) {
//            try {
//                Thread.sleep(500);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//            addDataHandlerAndAdapter(n);
//        }
    }

    public void addDataHandlerAndAdapter(WaiterNotification notification) {
        notification.initCountTime();
        mAdapter.addData(notification);
        getSchduler().addItem(notification);
        setTotal();
    }

    private SchedulerThread getSchduler() {
        return ((WaiterActivity) getActivity()).myScheduler;
    }

    private PublishSubject<Integer> scheduler;

    private PublishSubject<ServingFragment> endPoint = PublishSubject.create();

    @Override
    public void onStop() {
        super.onStop();
        endPoint.onNext(this);

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    private List<WaiterNotification> fakeData() {
        List<WaiterNotification> notifications = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            DishNotification dishNotification = new DishNotification(i, "acp colum");
            WaiterNotification waiterNotification = new WaiterNotification(4, dishNotification);
            notifications.add(waiterNotification);
        }
        return notifications;
    }

    private Services getServices() {
        return ((WaiterActivity) getActivity()).getServices();
    }

    private void showProcessing() {
        rlProcessing.setVisibility(View.VISIBLE);
    }

    private void hideProcessing() {
        rlProcessing.setVisibility(View.GONE);
    }

    private SchedulerThread getMySchedulerThread() {
        return ((WaiterActivity) getActivity()).getMyScheduler();
    }

    public void updateFromTable(List<TableGroupServe> listOD) {
        mAdapter.removeInDerictFromTable(listOD);
        setTotal();
    }

    private void groupDishByTable() {
        orginData;
        List<ServingDishGroup> adapterData = new ArrayList<>();
        List<Integer> dishIdList = new ArrayList<>();
        if (0 < orginData.size()) {
            do {
                Map<Integer, GroupDishByTable> tableQuantity = new HashMap<>();
                int currentDishId = -1;
                String dishName = null;
                for (WaiterNotification noti : orginData) {
                    int dishIdComp = noti.getDish().getDishId();
                    if (-1 != dishIdList.indexOf(dishIdComp)) {
                        if (-1 == currentDishId && null == dishName) {
                            currentDishId = dishIdComp;
                            dishName = noti.getDish().getDishName();
                            dishIdList.add(currentDishId);
                        }
                        if (dishIdComp == currentDishId) {
                            int key = noti.getTableNumber();
                            if (tableQuantity.containsKey(key)) {
                                // containing table number
                                GroupDishByTable value = tableQuantity.get(key);
                                value.setQuantity(value.getQuantity() + 1);
                                value.getUids().add(noti.getUid());
                                tableQuantity.put(key, value);
                            } else {
                                // not contain
                                GroupDishByTable value =
                                        new GroupDishByTable(noti.getOrderDetailId(), key, noti.getUid());
                                tableQuantity.put(key, value);
                            }
                        }
                    }
                }
            } while (true);
        }
    }
}
