package com.example.bipain.boe_restaurantapp.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.TextView;
import com.example.bipain.boe_restaurantapp.R;
import com.example.bipain.boe_restaurantapp.adapter.DishServeAdatper;
import com.example.bipain.boe_restaurantapp.gcm.GCMIntentService;
import com.example.bipain.boe_restaurantapp.gcm.GCMRegistrationIntentService;
import com.example.bipain.boe_restaurantapp.model.User;
import com.example.bipain.boe_restaurantapp.model.WaiterNotification;
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

public class WaiterActivity extends AppCompatActivity {
    private BroadcastReceiver mBroadcastReceiver;
    private final static String GCM_TOKEN = "GCM_TOKEN";

    private PreferencesManager preferencesManager;
    private EndpointManager endpointManager;
    private Retrofit apiService;
    private Services services;
    private Gson gson;

    @BindView(R.id.tvTotal)
    TextView tvTotal;
    @BindView(R.id.rvDish)
    RecyclerView rvDish;
    DishServeAdatper mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_waiter);
        ButterKnife.bind(this);

        preferencesManager = new PreferencesManager(this, new Gson());
        endpointManager = new EndpointManager(this);
        apiService = new RetrofitUtils(preferencesManager, endpointManager).create();
        services = apiService.create(Services.class);
        gson = new Gson();

        setBroadcastReceiver();

        setUpRecyclerView();
    }

    private void setUpRecyclerView() {
        mAdapter = new DishServeAdatper();
        mAdapter.setListner(() -> setTotal());
        RecyclerView.LayoutManager manager = new LinearLayoutManager(this,
                LinearLayoutManager.VERTICAL, false);
        DividerItemDecoration decoration = new DividerItemDecoration(this,
                DividerItemDecoration.VERTICAL);
        rvDish.setLayoutManager(manager);
        rvDish.addItemDecoration(decoration);
        rvDish.setAdapter(mAdapter);

        setTotal();
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
                                    ToastUtils.toastLongMassage(WaiterActivity.this,
                                            "reg token is sucess");
                                }
                            } else {
                                Log.v("GCM-register", "fail");
                                ToastUtils.toastLongMassage(WaiterActivity.this,
                                        "reg token is fail");
                            }
                        }

                        @Override
                        public void onFailure(Call<NotificationResponse> call, Throwable t) {

                        }
                    });
                    Log.v(GCM_TOKEN, token);
                } else if (intent.getAction().endsWith(GCMRegistrationIntentService.REGISTRATION_ERROR)) {
                    ToastUtils.toastLongMassage(WaiterActivity.this,
                            "GCM registration error");
                } else if (intent.getAction().endsWith(GCMIntentService.MESSAGE_TO_WAITER)) {
                    String message = intent.getStringExtra("body");
                    WaiterNotification notification = gson.fromJson(message, WaiterNotification.class);
                    mAdapter.addData(notification);
                    setTotal();
                } else {
                    ToastUtils.toastShortMassage(getApplicationContext(), "Nothing");
                }
            }
        };

        Intent intent = new Intent(getApplicationContext(), GCMRegistrationIntentService.class);
        startService(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(getApplicationContext()).registerReceiver(mBroadcastReceiver,
                new IntentFilter(GCMRegistrationIntentService.REGISTRATION_SUCCESS));
        LocalBroadcastManager.getInstance(getApplicationContext()).registerReceiver(mBroadcastReceiver,
                new IntentFilter(GCMRegistrationIntentService.REGISTRATION_ERROR));
        LocalBroadcastManager.getInstance(getApplicationContext()).registerReceiver(mBroadcastReceiver,
                new IntentFilter(GCMIntentService.MESSAGE_TO_WAITER));
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i("ZZZZZZZ", "onPause");
        LocalBroadcastManager.getInstance(getApplicationContext()).unregisterReceiver(mBroadcastReceiver);
    }

    public static Intent newInstance(Context context) {
        Intent i = new Intent(context, WaiterActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        return i;
    }

    private void setTotal() {
        tvTotal.setText(String.valueOf(mAdapter.getItemCount()));
    }
}
