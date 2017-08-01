package com.example.bipain.boe_restaurantapp.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import com.example.bipain.boe_restaurantapp.R;
import com.example.bipain.boe_restaurantapp.activities.TabManagerActivity;
import com.example.bipain.boe_restaurantapp.adapter.MaterialAdapter;
import com.example.bipain.boe_restaurantapp.model.Material;
import com.example.bipain.boe_restaurantapp.model.StatusResponse;
import com.example.bipain.boe_restaurantapp.services.Services;
import com.example.bipain.boe_restaurantapp.utils.Constant;
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

public class MaterialFragment extends Fragment {
    private Unbinder unbinder;
    @BindView(R.id.rvMaterial)
    RecyclerView rvMaterial;
    MaterialAdapter materialAdapter;
    private boolean init = true;
    @BindView(R.id.rlProcessing)
    RelativeLayout rlProcessing;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.activity_menu_fragment, container, false);
        unbinder = ButterKnife.bind(this, view);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        hideProcessing();
        materialAdapter = new MaterialAdapter(new ArrayList<>(), getContext());
        materialAdapter.setListener((status, id) -> {
            if (status) {
                markAvailable(id);
            } else {
                markNotAvailable(id);
            }
        });
        RecyclerView.LayoutManager layoutManager =
                new GridLayoutManager(getContext(), 2, LinearLayoutManager.VERTICAL, false);
//        rvMaterial.addItemDecoration(new ItemDecorationAlbumColumns(
//                getResources().getDimensionPixelSize(R.dimen.photos_list_spacing),
//                getResources().getInteger(R.integer.photo_list_preview_columns)));
        rvMaterial.setLayoutManager(layoutManager);
        rvMaterial.setAdapter(materialAdapter);
    }

    private void markAvailable(int id) {
        showProcessing();
        if (RetrofitUtils.checkNetworkAndServer(getContext())) {
            Services services = getServices();
            services.markMaterialAvailable(id).enqueue(new Callback<StatusResponse>() {
                @Override
                public void onResponse(Call<StatusResponse> call, Response<StatusResponse> response) {
                    if (response.isSuccessful()) {
                        if (null != response.body()) {

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
            hideProcessing();
//            markAvailable(id);
        }
    }

    private void markNotAvailable(int id) {
        showProcessing();
        if (RetrofitUtils.checkNetworkAndServer(getContext())) {
            Services services = getServices();
            services.markMaterialNotAvailable(id).enqueue(new Callback<List<Integer>>() {
                @Override
                public void onResponse(Call<List<Integer>> call, Response<List<Integer>> response) {
                    if (response.isSuccessful()) {
                        if (null != response.body()) {
//                        ToastUtils.toastShortMassage(getContext(), "not available");

                            notifyDishServing(response.body());
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
                public void onFailure(Call<List<Integer>> call, Throwable t) {
                    hideProcessing();
                    ToastUtils.toastLongMassage(getContext(), getString(R.string.text_response_error_connection));
                }
            });
        } else {
            hideProcessing();
//            markNotAvailable(id);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d(Constant.LOG_TAG, "Menu-onstart");
        setPos();
        getMaterial();
    }

    private void getMaterial() {
        if (RetrofitUtils.checkNetworkAndToast(getContext())) {
            Services service = getServices();
            service.getMaterial().enqueue(new Callback<List<Material>>() {
                @Override
                public void onResponse(Call<List<Material>> call, Response<List<Material>> response) {
                    if (response.isSuccessful()) {
                        if (null != response.body()) {
                            materialAdapter.setData(response.body());
                        }
                    } else {
                        if (response.code() == 500) {
                            ToastUtils.toastLongMassage(getContext(), getString(R.string.text_response_error_not_process));
                        } else {
                            ToastUtils.toastLongMassage(getContext(), getString(R.string.text_response_error_msg));
                        }
                    }
                }

                @Override
                public void onFailure(Call<List<Material>> call, Throwable t) {
                    ToastUtils.toastLongMassage(getContext(), getString(R.string.text_response_error_connection));
                }
            });
        } else {
//            getMaterial();
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(Constant.LOG_TAG, "Menu-onResume");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    public Services getServices() {
        return ((TabManagerActivity) getActivity()).getServices();
    }

    private void setPos() {
        if (!init) {
            ((TabManagerActivity) getActivity()).setFragmentPos(1);
        }
        init = !init;
    }

    private void notifyDishServing(List<Integer> integers) {
        ((TabManagerActivity) getActivity()).notifyDishNotAvailable(integers);
    }

    private void showProcessing() {
        rlProcessing.setVisibility(View.VISIBLE);
    }

    private void hideProcessing() {
        rlProcessing.setVisibility(View.GONE);
    }
}
