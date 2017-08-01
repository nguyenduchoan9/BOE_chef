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
import com.example.bipain.boe_restaurantapp.adapter.WarningAdapter;
import com.example.bipain.boe_restaurantapp.model.GroupDishByTable;
import com.example.bipain.boe_restaurantapp.model.ServingDishGroup;
import com.example.bipain.boe_restaurantapp.model.StatusResponse;
import com.example.bipain.boe_restaurantapp.model.TableGroupServe;
import com.example.bipain.boe_restaurantapp.model.WaiterNotification;
import com.example.bipain.boe_restaurantapp.services.Services;
import com.example.bipain.boe_restaurantapp.utils.RetrofitUtils;
import com.example.bipain.boe_restaurantapp.utils.ToastUtils;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
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
public class WarningFragment extends Fragment {

    private Unbinder unbinder;
    private Handler myHandler;

    @BindView(R.id.tvTotal)
    TextView tvTotal;
    @BindView(R.id.rvDish)
    RecyclerView rvDish;
    @BindView(R.id.rlProcessing)
    RelativeLayout rlProcessing;

    WarningAdapter mAdapter;
    Services services;
    private LinkedList<WaiterNotification> orginData = new LinkedList<>();

    public WarningFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        services = getServices();
        mAdapter = new WarningAdapter();
        mAdapter.setListner((pos, notify) -> {
            showProcessing();
            getMySchedulerThread().blockThread();
            markOrderDetailServed(notify);
        });
        myHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                getMySchedulerThread().blockThread();
                WaiterNotification i = (WaiterNotification) msg.obj;
                if (i.isNotifyToWarning()) {
                    orginData.add(i);
                    groupDishByTable();
                }
                setTotal();
                getMySchedulerThread().releaseThread();
//                Log.d(LOG_TAG, i.getDish().getDishName()
//                        + "-"
//                        + String.valueOf(i.getDish().getDishId()));
            }
        };
    }

    private void markOrderDetailServed(ServingDishGroup notify) {
        if (RetrofitUtils.checkNetworkAndServer(getContext())) {
            services.markOrderDetailServed(toServeParams(notify)).enqueue(new Callback<StatusResponse>() {
                @Override
                public void onResponse(Call<StatusResponse> call, Response<StatusResponse> response) {
                    if (response.isSuccessful()) {
                        if (null != response.body()) {
                            updateServeOrigin(notify);
                            setTotal();
//                            getMySchedulerThread().addServedItem(notify);
//                            mAdapter.removeData(notify);
                        }
                    } else {
                        if (response.code() == 500) {
                            ToastUtils.toastLongMassage(getContext(), getString(R.string.text_response_error_not_process));
                        } else {
                            ToastUtils.toastLongMassage(getContext(), getString(R.string.text_response_error_msg));
                        }
                    }
                    hideProcessing();
                    getMySchedulerThread().releaseThread();
                }

                @Override
                public void onFailure(Call<StatusResponse> call, Throwable t) {
                    hideProcessing();
                    getMySchedulerThread().releaseThread();
                    ToastUtils.toastLongMassage(getContext(), getString(R.string.text_response_error_connection));
                }
            });
        } else {
            hideProcessing();
//            markOrderDetailServed(notify);
        }
    }

    private String toServeParams(ServingDishGroup group) {
        StringBuilder builder = new StringBuilder();
        for (GroupDishByTable serve : group.getGroupDishByTableList()) {
            builder.append(serve.getOrderDetailId())
                    .append("_")
                    .append(serve.getQuantity())
                    .append(";");
        }

        return builder.deleteCharAt(builder.length() - 1).toString();
    }

    private SchedulerThread getMySchedulerThread() {
        return ((WaiterActivity) getActivity()).getMyScheduler();
    }

    private void updateServeOrigin(ServingDishGroup group) {
        List<WaiterNotification> removeData = new ArrayList<>();
        boolean isChange = false;
        for (WaiterNotification noti : orginData) {
            if (group.isContainerOrderDetailId(noti.getOrderDetailId())) {
                removeData.add(noti);
                if (!isChange) isChange = true;
            }
        }
        if (isChange) {
            orginData.removeAll(removeData);
            groupDishByTable();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_warning2, container, false);
        unbinder = ButterKnife.bind(this, v);
        return v;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setUpRecyclerView();
    }

    private void setUpRecyclerView() {

        RecyclerView.LayoutManager manager = new LinearLayoutManager(getContext(),
                LinearLayoutManager.VERTICAL, false);
        DividerItemDecoration decoration = new DividerItemDecoration(getContext(),
                DividerItemDecoration.VERTICAL);
        rvDish.setLayoutManager(manager);
        rvDish.addItemDecoration(decoration);
        rvDish.setAdapter(mAdapter);

        setTotal();

    }

    private void setTotal() {
        if (null != orginData) {
            tvTotal.setText(getString(R.string.text_total)
                    + String.valueOf(orginData.size())
                    + (orginData.size() <= 1 ? getString(R.string.text_dish) : getString(R.string.text_dishes)));
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    public Handler getMyHandler() {
        return myHandler;
    }

    private void showProcessing() {
        rlProcessing.setVisibility(View.VISIBLE);
    }

    private void hideProcessing() {
        rlProcessing.setVisibility(View.GONE);
    }

    private Services getServices() {
        return ((WaiterActivity) getActivity()).getServices();
    }

    public void updateFromTable(List<TableGroupServe> listOD) {
        getMySchedulerThread().blockThread();
        Log.d("Hoang", "updateFromTable: before " + orginData.size());
        if (removeInDerictFromTable(listOD)) {
            Log.d("Hoang", "updateFromTable: inside " + orginData.size());
            groupDishByTable();
        }
        setTotal();
        getMySchedulerThread().releaseThread();
    }

    private boolean removeInDerictFromTable(List<TableGroupServe> listOD) {
        boolean obserChange = false;
        if (null != listOD) {
            if (listOD.size() > 0) {
                Log.d("Hoang", "remove: list size " + listOD.size());
                for (int i = 0; i < listOD.size(); i++) {
                    int timeLoop = listOD.get(i).quantityCountInFragment;
                    boolean flagContinue = true;
                    while (false != flagContinue) {
                        int od = listOD.get(i).orderDetailId;
                        int postInAdapter = findPostByOrderDetailId(od);
                        Log.d("Hoang", "remove: No. " + i
                                + "--" + "OD " + od + "--" + "posAd " + postInAdapter);
                        if (-1 != postInAdapter) {
                            listOD.get(i).quantityCountInFragment -= 1;
                            orginData.remove(postInAdapter);
                            if (!obserChange) obserChange = true;
                        } else {
                            timeLoop -= 1;
                            if (timeLoop <= 0)
                                flagContinue = false;
                        }
                    }
                }
            }
        }
        return obserChange;
    }

    private int findPostByOrderDetailId(int od) {
        for (int i = 0; i < orginData.size(); i++) {
            WaiterNotification needItem = orginData.get(i);
            if (od == needItem.getOrderDetailId()) {
                return i;
            }
        }
        return -1;

    }

    private void groupDishByTable() {
        List<ServingDishGroup> adapterData = new ArrayList<>();
        List<WaiterNotification> notGroup = new ArrayList<>();
        List<WaiterNotification> processGroup = new ArrayList<>();
        if (0 < orginData.size()) {
            do {
                processGroup = notGroup.size() == 0 ? orginData : notGroup;
                notGroup = new ArrayList<>();
                Map<Integer, GroupDishByTable> tableQuantity = new HashMap<>();
                int currentDishId = -1;
                String dishName = null;
                for (WaiterNotification noti : processGroup) {
                    int dishIdComp = noti.getDish().getDishId();
                    if (-1 == currentDishId && null == dishName) {
                        currentDishId = dishIdComp;
                        dishName = noti.getDish().getDishName();
                    }
                    if (dishIdComp == currentDishId) {
                        int key = noti.getTableNumber();
                        if (tableQuantity.containsKey(key)) {
                            // containing table number
                            GroupDishByTable value = tableQuantity.get(key);
                            value.setQuantity(value.getQuantity() + 1);
                            value.getUids().add(noti.getUid());
                            if (noti.isNotifyToWarning()) {
                                value.setWarning(true);
                            }
                            tableQuantity.put(key, value);
                        } else {
                            // not contain
                            GroupDishByTable value =
                                    new GroupDishByTable(noti.getOrderDetailId(), key, noti.getUid());
                            if (noti.isNotifyToWarning()) {
                                value.setWarning(true);
                            }
                            tableQuantity.put(key, value);
                        }
                    } else {
                        notGroup.add(noti);
                    }
                }
                List<GroupDishByTable> grouped = new ArrayList<>(tableQuantity.values());
                Collections.sort(grouped);
                ServingDishGroup serve = new ServingDishGroup(currentDishId, dishName, grouped);
                adapterData.add(serve);
            } while (notGroup.size() != 0);
            Collections.sort(adapterData, (o1, o2) -> o1.getDishName().compareTo(o2.getDishName()));
            mAdapter.setData(adapterData);
        } else {
            mAdapter.setData(new ArrayList<>());
        }
    }
}
