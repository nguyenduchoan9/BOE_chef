package com.example.bipain.boe_restaurantapp.activities;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
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
    private PreferencesManager preferencesManager;
    private EndpointManager endpointManager;
    private Retrofit apiService;
    private Services services;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        preferencesManager = new PreferencesManager(this, new Gson());
        endpointManager = new EndpointManager(this);
        apiService = new RetrofitUtils(preferencesManager, endpointManager).create();
        services = apiService.create(Services.class);

        if (checkGooglePlayService()) {
            btnChef.setOnClickListener(v -> {
                loginAsChef();
//                startActivity(TabManagerActivity.newInstance(LoginActivity.this));
            });

            btnWaiter.setOnClickListener(v -> {
                loginAsWaiter();

//                startActivity(WaiterActivity.newInstance(LoginActivity.this));
            });
        }
    }

    private void loginAsWaiter() {
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
                        } else {
                            ToastUtils.toastLongMassage(LoginActivity.this, "The serve is under maintenance");
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                ToastUtils.toastLongMassage(LoginActivity.this, "The serve is under maintenance");
            }
        });
    }

    public static Intent newInstance(Context context) {
        Intent i = new Intent(context, LoginActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        return i;
    }

    private void loginAsChef() {
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
                        } else {
                            ToastUtils.toastLongMassage(LoginActivity.this, "The serve is under maintenance");
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                ToastUtils.toastLongMassage(LoginActivity.this, "The serve is under maintenance");
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
}
