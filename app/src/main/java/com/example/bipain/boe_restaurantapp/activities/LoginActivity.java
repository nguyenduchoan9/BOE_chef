package com.example.bipain.boe_restaurantapp.activities;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;
import com.example.bipain.boe_restaurantapp.R;
import com.example.bipain.boe_restaurantapp.model.User;
import com.example.bipain.boe_restaurantapp.request.LoginUserParam;
import com.example.bipain.boe_restaurantapp.services.Services;
import com.example.bipain.boe_restaurantapp.utils.Constant;
import com.example.bipain.boe_restaurantapp.utils.EndpointManager;
import com.example.bipain.boe_restaurantapp.utils.PreferencesManager;
import com.example.bipain.boe_restaurantapp.utils.RetrofitUtils;
import com.example.bipain.boe_restaurantapp.utils.ToastUtils;
import com.example.bipain.boe_restaurantapp.utils.Util;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.gson.Gson;
import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Headers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class LoginActivity extends AppCompatActivity {

    @BindView(R.id.btnChef)
    Button btnChef;
    @BindView(R.id.btnWaiter)
    Button btnWaiter;
    @BindView(R.id.btnCashier)
    Button btnCashier;
    private PreferencesManager preferencesManager;
    private EndpointManager endpointManager;
    private Retrofit apiService;
    private Services services;
    @BindView(R.id.rlProcessing)
    RelativeLayout rlProcessing;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Util.handleSelectLanguage(this, Util.getLanguage(this));
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        preferencesManager = new PreferencesManager(this, new Gson());
        endpointManager = new EndpointManager(this);
        apiService = new RetrofitUtils(preferencesManager, endpointManager).create();
        services = apiService.create(Services.class);

        if (checkGooglePlayService()) {
            btnChef.setOnClickListener(v -> {
                if (RetrofitUtils.checkNetworkAndServer(LoginActivity.this)) {
                    loginAsChef();
                }
            });

            btnWaiter.setOnClickListener(v -> {
                if (RetrofitUtils.checkNetworkAndServer(LoginActivity.this)) {
                    loginAsWaiter();
                }
            });
            btnCashier.setOnClickListener(v -> {
                loginAsCashier();
            });
        }
    }

    private void loginAsCashier() {
        showProcessing();
        LoginUserParam userParam = new LoginUserParam("mastercashier", "12345678");
        services.loginAsChef(userParam).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful()) {
                    if (null != response.body()) {
                        User user = response.body();
                        if ("cashier".equals(user.getRole())) {
                            saveCredentialHeader(response.headers());
                            saveUser(user);
                            startActivity(CashierActivity.newInstance(LoginActivity.this));
                        }
                    } else {
                        if (response.code() == 500) {
                            ToastUtils.toastLongMassage(LoginActivity.this, getString(R.string.text_response_error_not_process));
                        } else {
                            ToastUtils.toastLongMassage(LoginActivity.this, getString(R.string.text_response_error_msg));
                        }
                    }
                    hideProcessing();
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                ToastUtils.toastLongMassage(LoginActivity.this, getString(R.string.text_response_error_connection));
                hideProcessing();
            }
        });
    }

    private void loginAsWaiter() {
        showProcessing();
        LoginUserParam userParam = new LoginUserParam("masterwaiter", "12345678");
        services.loginAsChef(userParam).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful()) {
                    if (null != response.body()) {
                        User user = response.body();
                        if ("waiter".equals(user.getRole())) {
                            saveCredentialHeader(response.headers());
                            saveUser(user);
                            startActivity(WaiterActivity.newInstance(LoginActivity.this));
                        }
                    } else {
                        if (response.code() == 500) {
                            ToastUtils.toastLongMassage(LoginActivity.this, getString(R.string.text_response_error_not_process));
                        } else {
                            ToastUtils.toastLongMassage(LoginActivity.this, getString(R.string.text_response_error_msg));
                        }
                    }
                    hideProcessing();
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                ToastUtils.toastLongMassage(LoginActivity.this, getString(R.string.text_response_error_connection));
                hideProcessing();
            }
        });
    }

    public static Intent newInstance(Context context) {
        Intent i = new Intent(context, LoginActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        return i;
    }

    private void loginAsChef() {
        showProcessing();
        LoginUserParam userParam = new LoginUserParam("masterchef", "12345678");
        services.loginAsChef(userParam).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful()) {
                    if (null != response.body()) {
                        User user = response.body();
                        if ("chef".equals(user.getRole())) {
                            saveCredentialHeader(response.headers());
                            saveUser(user);
                            startActivity(TabManagerActivity.newInstance(LoginActivity.this));
                        }
                    } else {
                        if (response.code() == 500) {
                            ToastUtils.toastLongMassage(LoginActivity.this, getString(R.string.text_response_error_not_process));
                        } else {
                            ToastUtils.toastLongMassage(LoginActivity.this, getString(R.string.text_response_error_msg));
                        }
                    }
                    hideProcessing();
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                hideProcessing();
                ToastUtils.toastLongMassage(LoginActivity.this, getString(R.string.text_response_error_connection));
            }
        });
    }

    private void saveUser(User user) {
        preferencesManager.logIn(user);
    }

    private void saveCredentialHeader(Headers headers) {
        preferencesManager.saveCredentialHeader(headers);
    }

    private final int PLAY_SERVICES_RESOLUTION_REQUEST = 1;

    private boolean checkGooglePlayService() {
        GoogleApiAvailability googleApi = GoogleApiAvailability.getInstance();
        int resultCode = googleApi.isGooglePlayServicesAvailable(this);
        if (ConnectionResult.SUCCESS != resultCode) {
            if (googleApi.isUserResolvableError(resultCode)) {
                googleApi.getErrorDialog(this, resultCode,
                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
//                ToastUtils.toastLongMassage(this, "Google Play Services error");
                ToastUtils.toastLongMassage(this, "This device dose not support Google Play Service");
            }
            return false;
        }
        return true;
    }

    private void showProcessing() {
        rlProcessing.setVisibility(View.VISIBLE);
    }

    private void hideProcessing() {
        rlProcessing.setVisibility(View.GONE);
    }
}
