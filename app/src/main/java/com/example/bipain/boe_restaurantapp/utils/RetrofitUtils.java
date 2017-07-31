package com.example.bipain.boe_restaurantapp.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import com.example.bipain.boe_restaurantapp.BuildConfig;
import com.example.bipain.boe_restaurantapp.R;
import com.example.bipain.boe_restaurantapp.model.HeaderCredential;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.concurrent.TimeUnit;
import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by hoang on 27/03/2017.
 */

public class RetrofitUtils {
    PreferencesManager preferencesManager;
    EndpointManager endpointManager;

    public RetrofitUtils(PreferencesManager preferencesManager, EndpointManager endpointManager) {
        this.preferencesManager = preferencesManager;
        this.endpointManager = endpointManager;
    }

    public Retrofit create() {
        return new Retrofit.Builder()
                .baseUrl(endpointManager.getEndpoint() + "api/")
                .client(client())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public OkHttpClient client() {
        return new OkHttpClient.Builder()
                .addInterceptor(headerInterceptor())
                .addInterceptor(headerAuthenticateInterceptor())
                .addInterceptor(loggingInterceptorLevelHeaders())
                .addInterceptor(loggingInterceptorLevelBody())
                .connectTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .build();
    }

    private HttpLoggingInterceptor loggingInterceptorLevelHeaders() {
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.HEADERS);
        return httpLoggingInterceptor;
    }

    private HttpLoggingInterceptor loggingInterceptorLevelBody() {
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        return httpLoggingInterceptor;
    }

    private Interceptor headerInterceptor() {
        return chain -> {
            Request request = chain.request();
            Headers newHeaders = request.headers()
                    .newBuilder()
                    .add("AcceptVersion", "version=v1")
                    .add("Accept", "application/json")
                    .add("Accept-Charset", "utf-8")
                    .add("Cache-Control", "no-cache")
                    .build();
            request = request.newBuilder()
                    .headers(newHeaders)
                    .build();
            return chain.proceed(request);
        };
    }

    private Interceptor headerAuthenticateInterceptor() {
        return chain -> {
            Log.d("retrofitUtils", "hedaerAuthen");
            Request request = chain.request();
            if (null != preferencesManager) {
                if (preferencesManager.isLoggedin()) {
                    HeaderCredential headerCredential;
                    if (null != (headerCredential = preferencesManager.getCredentialHeader())) {
                        Headers newHeaders = request.headers()
                                .newBuilder()
                                .add(Constant.HEADER_ACCESS_TOKEN, headerCredential.getAccesToken())
                                .add(Constant.HEADER_TOKEN_TYPE, headerCredential.getTokenType())
                                .add(Constant.HEADER_CLIENT, headerCredential.getClient())
                                .add(Constant.HEADER_EXPIRY, headerCredential.getExpiry())
                                .add(Constant.HEADER_UID, headerCredential.getUid())
                                .build();
                        request = request.newBuilder()
                                .headers(newHeaders)
                                .build();
                    }
                }
            }
            return chain.proceed(request);
        };
    }

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();
    }

    public static boolean isOnline() {
        Runtime runtime = Runtime.getRuntime();
        try {
            int timeoutMs = 1500;
            Socket sock = new Socket();
            SocketAddress sockaddr = null;
            sockaddr = new InetSocketAddress(BuildConfig.IP_BASE_URL, 3000);

            sock.connect(sockaddr, timeoutMs);
            sock.close();
//            Process ipProcess = runtime.exec("/system/bin/ping -c 1 " + getHost(Constant.API_ENDPOINT));
//            int exitValue = ipProcess.waitFor();
//            return (exitValue == 0);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean checkNetworkAndServer(Context context) {
        if (!isNetworkAvailable(context)) {
            ToastUtils.toastLongMassage(context, context.getString(R.string.text_not_available_network));
            return false;
        } else if (!isOnline()) {
            ToastUtils.toastLongMassage(context, context.getString(R.string.text_server_maintanance));
            return false;
        }
        return true;
    }
}
