package com.example.bipain.boe_restaurantapp.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.bipain.boe_restaurantapp.R;
import com.example.bipain.boe_restaurantapp.activities.WaiterActivity;
import com.example.bipain.boe_restaurantapp.adapter.DishServeAdatper;
import com.example.bipain.boe_restaurantapp.adapter.WarningAdapter;
import com.example.bipain.boe_restaurantapp.model.StatusResponse;
import com.example.bipain.boe_restaurantapp.model.TableGroupServe;
import com.example.bipain.boe_restaurantapp.model.WaiterNotification;
import com.example.bipain.boe_restaurantapp.services.Services;
import com.example.bipain.boe_restaurantapp.utils.ToastUtils;
import java.util.List;
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
            services.markOrderDetailServed(notify.getOrderDetailId()).enqueue(new Callback<StatusResponse>() {
                @Override
                public void onResponse(Call<StatusResponse> call, Response<StatusResponse> response) {
                    if (response.isSuccessful()) {
                        if (null != response.body()) {
                            mAdapter.removeData(notify);
                            setTotal();
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
                    mAdapter.addData(i);
                }
                setTotal();
//                Log.d(LOG_TAG, i.getDish().getDishName()
//                        + "-"
//                        + String.valueOf(i.getDish().getDishId()));
            }
        };
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
        tvTotal.setText("Tổng số: " + String.valueOf(mAdapter.getItemCount()) + " dĩa");
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

    public void updateFromTable(List<TableGroupServe> listOD){
        mAdapter.removeInDerictFromTable(listOD);
        setTotal();
    }
}
