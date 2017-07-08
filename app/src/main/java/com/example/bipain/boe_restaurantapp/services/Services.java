package com.example.bipain.boe_restaurantapp.services;

import com.example.bipain.boe_restaurantapp.model.Material;
import com.example.bipain.boe_restaurantapp.model.StatusResponse;
import com.example.bipain.boe_restaurantapp.model.TableModel;
import com.example.bipain.boe_restaurantapp.model.User;
import com.example.bipain.boe_restaurantapp.request.LoginUserParam;
import com.example.bipain.boe_restaurantapp.request.NotificationResponse;
import com.example.bipain.boe_restaurantapp.request.SessionDeleteResponse;
import java.util.List;
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

    @POST("orders/mark_order_accept")
    @FormUrlEncoded
    Call<StatusResponse> markOrderAccept(@Field("order_id") int oderId);

    @POST("orders/mark_order_reject")
    @FormUrlEncoded
    Call<StatusResponse> markOrderReject(@Field("order_id") int oderId);

    @POST("orders/dish_done")
    @FormUrlEncoded
    Call<StatusResponse> markDishDone(@Field("order_id") int oderId, @Field("dish_id") int dishId);

    @POST("orders/list_dish_done")
    @FormUrlEncoded
    Call<StatusResponse> markListDishDone(@Field("list_done") String oderId);

    @GET("materials")
    Call<List<Material>> getMaterial();

    @POST("materials/mark_not_available")
    @FormUrlEncoded
    Call<StatusResponse> markMaterialNotAvailable(@Field("ids") int ids);

    @POST("materials/mark_available")
    @FormUrlEncoded
    Call<StatusResponse> markMaterialAvailable(@Field("ids") int ids);

    @GET("orders/table_dish")
    Call<List<TableModel>> getTable();
}
