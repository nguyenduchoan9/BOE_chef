package com.example.bipain.boe_restaurantapp.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import com.example.bipain.boe_restaurantapp.R;
import com.example.bipain.boe_restaurantapp.activities.WaiterActivity;
import com.example.bipain.boe_restaurantapp.adapter.TableAdapter;
import com.example.bipain.boe_restaurantapp.model.DishTableInfo;
import com.example.bipain.boe_restaurantapp.model.StatusResponse;
import com.example.bipain.boe_restaurantapp.model.TableGroupServe;
import com.example.bipain.boe_restaurantapp.model.TableModel;
import com.example.bipain.boe_restaurantapp.services.Services;
import com.example.bipain.boe_restaurantapp.utils.RetrofitUtils;
import com.example.bipain.boe_restaurantapp.utils.ToastUtils;
import java.util.ArrayList;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class TableFragment extends Fragment {
    private Unbinder unbinder;
    TableAdapter mAdapter;
    @BindView(R.id.rvListTable)
    RecyclerView rvList;

    @BindView(R.id.rlProcessing)
    RelativeLayout rlProcessing;
    Services services;

    public TableFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        services = getServices();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_table, container, false);
        unbinder = ButterKnife.bind(this, v);
        return v;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setUpRecyclerView();
    }

    private void setUpRecyclerView() {
        mAdapter = new TableAdapter(getContext());
        mAdapter.setListener((pos, orderDetailIdList, tableModel) -> {
            showProcessing();
            markListOrderDetailServed(pos, orderDetailIdList, tableModel);
        });
        RecyclerView.LayoutManager manager = new LinearLayoutManager(getContext(),
                LinearLayoutManager.VERTICAL, false);
        DividerItemDecoration decoration = new DividerItemDecoration(getContext(),
                DividerItemDecoration.VERTICAL);
        rvList.setLayoutManager(manager);
        rvList.addItemDecoration(decoration);
        rvList.setAdapter(mAdapter);
    }

    private void markListOrderDetailServed(int pos, String orderDetailIdList, TableModel tableModel) {
        if (RetrofitUtils.checkNetworkAndServer(getContext())) {
            services.markListOrderDetailServed(orderDetailIdList).enqueue(new Callback<StatusResponse>() {
                @Override
                public void onResponse(Call<StatusResponse> call, Response<StatusResponse> response) {
                    if (response.isSuccessful()) {
                        if (null != response.body()) {
                            mAdapter.removeData(pos);
                            String[] odArray = orderDetailIdList.split("_");
                            List<Integer> rsUpdate = new ArrayList<Integer>();
                            for (int i = 0; i < odArray.length; i++) {
                                rsUpdate.add(Integer.valueOf(odArray[i]));
                            }
                            List<DishTableInfo> tableInfos = tableModel.getDishList();
                            List<TableGroupServe> tableGroupServes = new ArrayList<>();
                            for (int i = 0; i < tableInfos.size(); i++) {
                                DishTableInfo tableInfo = tableInfos.get(i);
                                tableGroupServes.add(new TableGroupServe(tableInfo.getOrderDetailId(), tableInfo.getQuantiyNotServe()));
                            }

                            updateOtherFragment(tableGroupServes);
                        }
                    } else {
                        if (response.code() == 500) {
                            ToastUtils.toastLongMassage(getContext(), getString(R.string.text_response_error_not_process));
                        } else {
                            ToastUtils.toastLongMassage(getContext(), getString(R.string.text_response_error_msg));
                        }
                    }
                    hideProcessing();
                }

                @Override
                public void onFailure(Call<StatusResponse> call, Throwable t) {
                    hideProcessing();
                    ToastUtils.toastLongMassage(getContext(), getString(R.string.text_response_error_connection));
                }
            });
        } else {
//            markListOrderDetailServed(pos, orderDetailIdList, tableModel);
        }

    }

    private void updateOtherFragment(List<TableGroupServe> listOD) {
        ((WaiterActivity) getActivity()).updateFromTable(listOD);
    }

    @Override
    public void onStart() {
        super.onStart();
        loadData();
    }

    private void loadData() {
        if (RetrofitUtils.checkNetworkAndServer(getContext())) {
            Services services = getServices();
            showProcessing();
            services.getTable().enqueue(new Callback<List<TableModel>>() {
                @Override
                public void onResponse(Call<List<TableModel>> call, Response<List<TableModel>> response) {
                    if (response.isSuccessful()) {
                        if (null != response.body()) {
                            mAdapter.setData(response.body());
                        }
                    } else {
                        if (response.code() == 500) {
                            ToastUtils.toastLongMassage(getContext(), getString(R.string.text_response_error_not_process));
                        } else {
                            ToastUtils.toastLongMassage(getContext(), getString(R.string.text_response_error_msg));
                        }
                    }
                    hideProcessing();
                }

                @Override
                public void onFailure(Call<List<TableModel>> call, Throwable t) {
                    hideProcessing();
                    ToastUtils.toastLongMassage(getContext(), getString(R.string.text_response_error_connection));
                }
            });
        } else {
//            loadData();
        }

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
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
}
