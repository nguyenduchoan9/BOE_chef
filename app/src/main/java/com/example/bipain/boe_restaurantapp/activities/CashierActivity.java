package com.example.bipain.boe_restaurantapp.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import com.example.bipain.boe_restaurantapp.R;
import com.example.bipain.boe_restaurantapp.adapter.CashInfoAdapter;
import com.example.bipain.boe_restaurantapp.gcm.GCMIntentService;
import com.example.bipain.boe_restaurantapp.gcm.GCMRegistrationIntentService;
import com.example.bipain.boe_restaurantapp.model.CashModel;
import com.example.bipain.boe_restaurantapp.model.StatusResponse;
import com.example.bipain.boe_restaurantapp.request.NotificationResponse;
import com.example.bipain.boe_restaurantapp.services.Services;
import com.example.bipain.boe_restaurantapp.utils.EndpointManager;
import com.example.bipain.boe_restaurantapp.utils.PreferencesManager;
import com.example.bipain.boe_restaurantapp.utils.RetrofitUtils;
import com.example.bipain.boe_restaurantapp.utils.ToastUtils;
import com.google.gson.Gson;
import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class CashierActivity extends AppCompatActivity {
    @BindView(R.id.rvCash)
    RecyclerView rvCash;
    CashInfoAdapter adapter;
    private BroadcastReceiver mBroadcastReceiver;
    private PreferencesManager preferencesManager;
    private EndpointManager endpointManager;
    private Retrofit apiService;
    private Services services;
    private Gson gson;
    @BindView(R.id.rlProcessing)
    RelativeLayout rlProcessing;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cashier);
        ButterKnife.bind(this);
        preferencesManager = new PreferencesManager(this, new Gson());
        endpointManager = new EndpointManager(this);
        apiService = new RetrofitUtils(preferencesManager, endpointManager).create();
        services = apiService.create(Services.class);
        gson = new Gson();

        setBroadcastReceiver();
        initializeRv();
    }

    protected void setBroadcastReceiver() {
        mBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction().endsWith(GCMRegistrationIntentService.REGISTRATION_SUCCESS)) {
                    String token = intent.getStringExtra("token");
                    services.registerRegToken(token).enqueue(new Callback<NotificationResponse>() {
                        @Override
                        public void onResponse(Call<NotificationResponse> call, Response<NotificationResponse> response) {
                            if (response.isSuccessful()) {
                                if (null != response.body()) {
                                    Log.v("GCM-register", "Success");
                                    ToastUtils.toastLongMassage(CashierActivity.this,
                                            "reg token is sucess");
                                }
                            } else {
                                Log.v("GCM-register", "fail");
                                ToastUtils.toastLongMassage(CashierActivity.this,
                                        "reg token is fail");
                            }
                        }

                        @Override
                        public void onFailure(Call<NotificationResponse> call, Throwable t) {

                        }
                    });
//                    Log.v(GCM_TOKEN, token);
                } else if (intent.getAction().endsWith(GCMRegistrationIntentService.REGISTRATION_ERROR)) {
                    ToastUtils.toastLongMassage(CashierActivity.this,
                            "GCM registration error");
                } else if (intent.getAction().endsWith(GCMIntentService.MESSAGE_TO_CASHIER)) {
                    String message = intent.getStringExtra("body");
                    CashModel cashModel = gson.fromJson(message, CashModel.class);
                    adapter.addData(cashModel);
                } else {
                    ToastUtils.toastShortMassage(getApplicationContext(), "Nothing");
                }
            }
        };

        Intent intent = new Intent(getApplicationContext(), GCMRegistrationIntentService.class);
        startService(intent);
    }
    @Override
    protected void onPause() {
        super.onPause();
        Log.i("ZZZZZZZ", "onPause");
        LocalBroadcastManager.getInstance(getApplicationContext()).unregisterReceiver(mBroadcastReceiver);
    }

    @Override
    protected void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(getApplicationContext()).registerReceiver(mBroadcastReceiver,
                new IntentFilter(GCMRegistrationIntentService.REGISTRATION_SUCCESS));
        LocalBroadcastManager.getInstance(getApplicationContext()).registerReceiver(mBroadcastReceiver,
                new IntentFilter(GCMRegistrationIntentService.REGISTRATION_ERROR));
        LocalBroadcastManager.getInstance(getApplicationContext()).registerReceiver(mBroadcastReceiver,
                new IntentFilter(GCMIntentService.MESSAGE_TO_CASHIER));
    }

    private void initializeRv() {
        adapter = new CashInfoAdapter(this);
        adapter.setListener((orderId, pos) -> {
            handleClick(orderId, pos);
        });
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        DividerItemDecoration dividerItemDecoration =
                new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        rvCash.addItemDecoration(dividerItemDecoration);
        rvCash.setLayoutManager(layoutManager);
        rvCash.setAdapter(adapter);
    }

    public static Intent newInstance(Context context) {
        Intent i = new Intent(context, CashierActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        return i;
    }

    private void handleClick(int orderId, int pos) {
        showProcessing();
        if (RetrofitUtils.checkNetworkAndToast(this)) {
            services.markPayedByCash(orderId).enqueue(new Callback<StatusResponse>() {
                @Override
                public void onResponse(Call<StatusResponse> call, Response<StatusResponse> response) {
                    if (response.isSuccessful()) {
                        if (null != response.body()) {
                            adapter.removeByPos(pos);
                        }
                    }
                    hideProcessing();
                }

                @Override
                public void onFailure(Call<StatusResponse> call, Throwable t) {
                    hideProcessing();
                }
            });
        }else {
            hideProcessing();
        }
    }

    private void showProcessing() {
        rlProcessing.setVisibility(View.VISIBLE);
    }

    private void hideProcessing() {
        rlProcessing.setVisibility(View.GONE);
    }
}
