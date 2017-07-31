package com.example.bipain.boe_restaurantapp.utils;

import android.content.Context;
import android.content.SharedPreferences;
import com.example.bipain.boe_restaurantapp.BuildConfig;

/**
 * Created by hoang on 27/05/2017.
 */

public class EndpointManager {
    public static final String ENDPOINT = "ENDPOINT";
    private SharedPreferences sharedPreferences;

    public EndpointManager(Context context) {
        this.sharedPreferences = context.getSharedPreferences(
                Constant.ID, Context.MODE_PRIVATE);
    }

    public String getEndpoint() {
        if(BuildConfig.IS_PROD){
            return BuildConfig.BASE_URL;
        }
        Constant.API_ENDPOINT = sharedPreferences.getString(ENDPOINT, Constant.API_ENDPOINT);
        return Constant.API_ENDPOINT;
    }

    public void setEndpoint(String endpoint) {
        Constant.API_ENDPOINT = endpoint;
        sharedPreferences.edit()
                .putString(ENDPOINT, endpoint)
                .apply();
    }
}
