package com.example.bipain.boe_restaurantapp.fragment;

import android.app.Service;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.bipain.boe_restaurantapp.R;
import com.example.bipain.boe_restaurantapp.activities.TabManagerActivity;
import com.example.bipain.boe_restaurantapp.adapter.MaterialAdapter;
import com.example.bipain.boe_restaurantapp.model.Material;
import com.example.bipain.boe_restaurantapp.model.StatusResponse;
import com.example.bipain.boe_restaurantapp.services.Services;
import com.example.bipain.boe_restaurantapp.utils.Constant;
import com.example.bipain.boe_restaurantapp.utils.ItemDecorationAlbumColumns;
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
        materialAdapter = new MaterialAdapter(new ArrayList<>(), getContext());
        materialAdapter.setListener(new MaterialAdapter.MaterialAapterListener() {
            @Override
            public void onCheckClick(boolean status, int id) {
                if (status) {
                    markAvailable(id);
                } else {
                    markNotAvailable(id);
                }
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
        Services services = getServices();
        services.markMaterialAvailable(id).enqueue(new Callback<StatusResponse>() {
            @Override
            public void onResponse(Call<StatusResponse> call, Response<StatusResponse> response) {
                if (response.isSuccessful()) {
                    if (null != response.body()) {
                        ToastUtils.toastShortMassage(getContext(), "not available");
                    }
                }
            }

            @Override
            public void onFailure(Call<StatusResponse> call, Throwable t) {

            }
        });
    }

    private void markNotAvailable(int id) {
        Services services = getServices();
        services.markMaterialNotAvailable(id).enqueue(new Callback<StatusResponse>() {
            @Override
            public void onResponse(Call<StatusResponse> call, Response<StatusResponse> response) {
                if (response.isSuccessful()) {
                    if (null != response.body()) {
                        ToastUtils.toastShortMassage(getContext(), "not avaiable");
                    }
                }
            }

            @Override
            public void onFailure(Call<StatusResponse> call, Throwable t) {

            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d(Constant.LOG_TAG, "Menu-onstart");
        setPos();
        Services service = getServices();
        service.getMaterial().enqueue(new Callback<List<Material>>() {
            @Override
            public void onResponse(Call<List<Material>> call, Response<List<Material>> response) {
                if (response.isSuccessful()) {
                    if (null != response.body()) {
                        materialAdapter.setData(response.body());
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Material>> call, Throwable t) {

            }
        });
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
}
