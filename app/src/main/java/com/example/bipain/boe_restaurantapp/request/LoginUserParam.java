package com.example.bipain.boe_restaurantapp.request;

/**
 * Created by hoang on 02/05/2017.
 */

public class LoginUserParam {
    public final String username;
    public final String password;

    public LoginUserParam(String email, String password) {
        this.username = email;
        this.password = password;
    }
}
