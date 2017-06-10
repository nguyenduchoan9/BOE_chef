package com.example.bipain.boe_restaurantapp.services;

import com.example.bipain.boe_restaurantapp.model.User;
import com.example.bipain.boe_restaurantapp.request.LoginUserParam;
import com.example.bipain.boe_restaurantapp.request.NotificationResponse;
import com.example.bipain.boe_restaurantapp.request.SessionDeleteResponse;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Created by hoang on 08/06/2017.
 */

public interface Services {
    @POST("sessions")
    Call<User> loginAsChef(@Body LoginUserParam param);

    @DELETE("sessions/{id}")
    Call<SessionDeleteResponse> logout(@Path("id") long id);

    @POST("session/{id}")
    @FormUrlEncoded
    Call<User> getUserProfile(@Field("username") String username, @Path("id") String id);

    @POST("notifications/register_reg_token")
    @FormUrlEncoded
    Call<NotificationResponse> registerRegToken(@Field("reg_token") String regToken);
}
