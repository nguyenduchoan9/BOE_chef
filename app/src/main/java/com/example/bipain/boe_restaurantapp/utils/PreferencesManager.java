package com.example.bipain.boe_restaurantapp.utils;

import android.content.Context;
import android.content.SharedPreferences;
import com.example.bipain.boe_restaurantapp.model.HeaderCredential;
import com.example.bipain.boe_restaurantapp.model.User;
import com.google.gson.Gson;
import okhttp3.Headers;

/**
 * Created by hoang on 08/06/2017.
 */

public class PreferencesManager {
    private static final String USER = "USER";
    private static final String HEADERS = "HEADERS";
    private static final String TABLE_INFO = "TABLE_INFO";
    private SharedPreferences sharedPreferences;
    private Gson gson;

    public PreferencesManager(Context context, Gson gson) {
        this.sharedPreferences = context
                .getSharedPreferences("BOE", Context.MODE_PRIVATE);
        this.gson = gson;
    }

    public User getUser() {
        String userJSON = sharedPreferences.getString(USER, "");
        if (!StringUtils.isEmpty(userJSON)) {
            return gson.fromJson(userJSON, User.class);
        }
        return null;
    }

    public void logIn(User user) {
        sharedPreferences.edit()
                .putString(USER, gson.toJson(user))
                .apply();
    }

    public boolean isLoggedin() {
        return null != getUser();
    }

    public void logOut() {
        sharedPreferences.edit()
                .remove(USER)
                .apply();
        sharedPreferences.edit()
                .remove(TABLE_INFO)
                .apply();
        sharedPreferences.edit()
                .remove(HEADERS)
                .apply();
    }

    public void saveCredentialHeader(Headers headerResponse) {
        String accessToken = headerResponse.get("access-token");
        String expiry = headerResponse.get("expiry");
        String tokenType = headerResponse.get("token-type");
        String uid = headerResponse.get("uid");
        String client = headerResponse.get("client");
        HeaderCredential headers = new HeaderCredential(accessToken,
                expiry, tokenType, uid, client);
        sharedPreferences.edit()
                .putString(HEADERS, gson.toJson(headers))
                .apply();
    }

    public HeaderCredential getCredentialHeader() {
        String headersCredential = sharedPreferences.getString(HEADERS, "");
        if (!StringUtils.isEmpty(headersCredential)) {
            HeaderCredential headers = gson.fromJson(headersCredential, HeaderCredential.class);
            return headers;
        }
        return null;
    }

    public String getRole() {
        return getUser().getRole();
    }
}
