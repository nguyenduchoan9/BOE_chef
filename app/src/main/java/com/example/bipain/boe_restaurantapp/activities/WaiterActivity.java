package com.example.bipain.boe_restaurantapp.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.example.bipain.boe_restaurantapp.R;
import com.example.bipain.boe_restaurantapp.fragment.LanguageDialog;
import com.example.bipain.boe_restaurantapp.fragment.ServingFragment;
import com.example.bipain.boe_restaurantapp.fragment.TableFragment;
import com.example.bipain.boe_restaurantapp.fragment.WarningFragment;
import com.example.bipain.boe_restaurantapp.gcm.GCMIntentService;
import com.example.bipain.boe_restaurantapp.gcm.GCMRegistrationIntentService;
import com.example.bipain.boe_restaurantapp.model.TableGroupServe;
import com.example.bipain.boe_restaurantapp.model.WaiterNotification;
import com.example.bipain.boe_restaurantapp.request.NotificationResponse;
import com.example.bipain.boe_restaurantapp.request.SessionDeleteResponse;
import com.example.bipain.boe_restaurantapp.services.Services;
import com.example.bipain.boe_restaurantapp.utils.Constant;
import com.example.bipain.boe_restaurantapp.utils.EndpointManager;
import com.example.bipain.boe_restaurantapp.utils.PreferencesManager;
import com.example.bipain.boe_restaurantapp.utils.RetrofitUtils;
import com.example.bipain.boe_restaurantapp.utils.ToastUtils;
import com.example.bipain.boe_restaurantapp.utils.Util;
import com.google.gson.Gson;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class WaiterActivity extends AppCompatActivity {
    private BroadcastReceiver mBroadcastReceiver;
    private PreferencesManager preferencesManager;
    private EndpointManager endpointManager;
    private Retrofit apiService;
    private Services services;
    private Gson gson;
    public SchedulerThread myScheduler;

    @BindView(R.id.containerServing)
    LinearLayout containerServing;
    @BindView(R.id.containerAttention)
    LinearLayout containerAttention;
    @BindView(R.id.containerTable)
    LinearLayout containerTable;
    @BindView(R.id.ivServing)
    ImageView ivServing;
    @BindView(R.id.ivAttention)
    ImageView ivAttention;
    @BindView(R.id.ivTable)
    ImageView ivTable;
    @BindView(R.id.tvAttention)
    TextView tvAttention;
    @BindView(R.id.tvServing)
    TextView tvServing;
    @BindView(R.id.bottomNavi)
    LinearLayout bottomNavi;
    @BindView(R.id.tvTable)
    TextView tvTable;

    private Toolbar toolbar;

    ServingFragment servingFragment = new ServingFragment();
    WarningFragment warningFragment = new WarningFragment();
    TableFragment tableFragment = new TableFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Util.handleSelectLanguage(this, Util.getLanguage(this));
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_waiter);
        ButterKnife.bind(this);

        preferencesManager = new PreferencesManager(this, new Gson());
        endpointManager = new EndpointManager(this);
        apiService = new RetrofitUtils(preferencesManager, endpointManager).create();
        services = apiService.create(Services.class);
        gson = new Gson();
        myScheduler = new SchedulerThread();
        setBroadcastReceiver();

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.text_toolbar_title_waiter);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        initView();

    }

    private void initView() {
        initContent();
    }

    private void initContent() {
        replaceContent(servingFragment, false, "CutleryFragment");
        replaceContent(warningFragment, false, "DrinkingFragment");
        replaceContent(tableFragment, false, "TableFragment");
        onServingClick();
        showFragmentPosition(fragmentPos = 0);
        setActionOnAttentionContainer();
        setActionOnServingContainer();
        setActionOnTableContainer();
    }

    int fragmentPos = -1;
    private final int SERVING_POS = 0;
    private final int WARNING_POS = 1;
    private final int TABLE_POS = 2;

    private void showFragmentPosition(int pos) {
        if (SERVING_POS == pos) {
            showFragment(servingFragment);
            hideFragment(warningFragment);
            hideFragment(tableFragment);
        } else if (WARNING_POS == pos) {
            showFragment(warningFragment);
            hideFragment(servingFragment);
            hideFragment(tableFragment);
        } else if (TABLE_POS == pos) {
            showFragment(tableFragment);
            tableFragment.onStart();
            hideFragment(warningFragment);
            hideFragment(servingFragment);
        }
    }

    private void showFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction().show(fragment).commit();
        getSupportFragmentManager().executePendingTransactions();
    }

    private void hideFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction().hide(fragment).commit();
        getSupportFragmentManager().executePendingTransactions();
    }

    private void replaceContent(Fragment fragment, boolean exist, String tag) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        if (exist) {
            ft.show(fragment);
        } else {
            ft.add(R.id.content, fragment, tag);
        }
        ft.commit();
        getSupportFragmentManager().executePendingTransactions();
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
//                    Log.v(GCM_TOKEN, token);
                } else if (intent.getAction().endsWith(GCMRegistrationIntentService.REGISTRATION_ERROR)) {
                    ToastUtils.toastLongMassage(WaiterActivity.this,
                            "GCM registration error");
                } else if (intent.getAction().endsWith(GCMIntentService.MESSAGE_TO_WAITER)) {
                    String message = intent.getStringExtra("body");
                    WaiterNotification notification = gson.fromJson(message, WaiterNotification.class);
                    servingFragment.addDataHandlerAndAdapter(notification);
                    onServingClick();
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

    public static Intent newInstance(Context context) {
        Intent i = new Intent(context, WaiterActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        return i;
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

        myScheduler.setMyHandler(servingFragment.getMyHandler());
        myScheduler.setHandleLongTime(warningFragment.getMyHandler());
//        Thread t = new Thread(myScheduler);
//        t.start();
        servingFragment.getMyHandler().postDelayed(myScheduler, 1000);
    }

    private String LOG_TAG = "XXXSCHEDULER";

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onStop() {
        super.onStop();
//        myScheduler.stopThread();
        servingFragment.getMyHandler().removeCallbacks(myScheduler);
    }

    private void setInActive(View container, TextView label) {
        // padding top 6 & 10dp under text
        container.setPadding(0, 8, 0, 10);
        // text size roboto regular 12
        label.setVisibility(View.GONE);
    }

    private void setActive(View container, TextView label) {
        // padding top 8
        container.setPadding(0, 6, 0, 10);
        label.setVisibility(View.VISIBLE);
        //10dp under text
        // text size roboto regular 14
    }

    private void setActionOnServingContainer() {
        containerServing.setOnClickListener(v -> onServingClick());
        ivServing.setOnClickListener(v -> onServingClick());
        tvServing.setOnClickListener(v -> onServingClick());
    }

    private void setActionOnAttentionContainer() {
        containerAttention.setOnClickListener(v -> onAttentionlick());
        tvAttention.setOnClickListener(v -> onAttentionlick());
        ivAttention.setOnClickListener(v -> onAttentionlick());
    }

    private void setActionOnTableContainer() {
        containerTable.setOnClickListener(v -> onTablelick());
        tvTable.setOnClickListener(v -> onTablelick());
        ivTable.setOnClickListener(v -> onTablelick());
    }

    private void onServingClick() {
        if (fragmentPos == SERVING_POS) return;
        fragmentPos = SERVING_POS;
        showFragmentPosition(fragmentPos);
        clearBackground();
        Util.changeDrawableColor(ivServing.getDrawable(),
                ContextCompat.getColor(this, R.color.colorPrimaryDark));
        setActive(containerServing, tvServing);
    }

    private void onAttentionlick() {
        if (fragmentPos == WARNING_POS) return;
        fragmentPos = WARNING_POS;
        showFragmentPosition(fragmentPos);
        clearBackground();
        Util.changeDrawableColor(ivAttention.getDrawable(),
                ContextCompat.getColor(this, R.color.colorPrimaryDark));
        setActive(containerAttention, tvAttention);

    }

    private void onTablelick() {
        if (fragmentPos == TABLE_POS) return;
        fragmentPos = TABLE_POS;
        showFragmentPosition(fragmentPos);
        clearBackground();
        Util.changeDrawableColor(ivTable.getDrawable(),
                ContextCompat.getColor(this, R.color.colorPrimaryDark));
        setActive(containerTable, tvTable);

    }

    private void clearBackground() {
        Util.changeDrawableColor(ivServing.getDrawable(),
                ContextCompat.getColor(this, android.R.color.black));
        Util.changeDrawableColor(ivAttention.getDrawable(),
                ContextCompat.getColor(this, android.R.color.black));
        Util.changeDrawableColor(ivTable.getDrawable(),
                ContextCompat.getColor(this, android.R.color.black));
        setInActive(containerServing, tvServing);
        setInActive(containerAttention, tvAttention);
        setInActive(containerTable, tvTable);
    }

    public Services getServices() {
        return services;
    }

    public SchedulerThread getMyScheduler() {
        return myScheduler;
    }

    public void updateFromTable(List<TableGroupServe> listOD) {
        myScheduler.setItemTableServed(listOD);
        servingFragment.updateFromTable(listOD);
        warningFragment.updateFromTable(listOD);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_waiter, menu);
        MenuItem menuItem = menu.findItem(R.id.action_language);
        openSelectLanguageDialog(menuItem);
        return super.onCreateOptionsMenu(menu);
    }

    private void openSelectLanguageDialog(MenuItem menuItem) {
        menuItem.setOnMenuItemClickListener(item -> {
            FragmentManager fm = getSupportFragmentManager();
            LanguageDialog dialog = LanguageDialog.newInstance(preferencesManager.getLanguage());
            dialog.setmListener(new LanguageDialog.LanguageListener() {
                                    @Override
                                    public void onEnglishSelect() {
                                        preferencesManager.setLanguage(Constant.EN_LANGUAGE_STRING);
                                        refreshViewAfterChangeLanguage();
                                    }

                                    @Override
                                    public void onVNSelect() {
                                        preferencesManager.setLanguage(Constant.VI_LANGUAGE_STRING);
                                        refreshViewAfterChangeLanguage();
                                    }
                                }
            );
            dialog.show(fm, "putang");
            return true;
        });
    }

    private void refreshViewAfterChangeLanguage() {
        finish();
        startActivity(new Intent(this, WaiterActivity.class));
    }

    @Override
    public boolean onSupportNavigateUp() {
        onLogOut();
        return true;
    }

    private void onLogOut() {
        services.logout(preferencesManager.getUser().getId()).enqueue(new Callback<SessionDeleteResponse>() {
            @Override
            public void onResponse(Call<SessionDeleteResponse> call, Response<SessionDeleteResponse> response) {
                if (response.isSuccessful()) {
                    preferencesManager.logOut();
                    startActivity(LoginActivity.newInstance(WaiterActivity.this));
                }
            }

            @Override
            public void onFailure(Call<SessionDeleteResponse> call, Throwable t) {

            }
        });
    }
}
