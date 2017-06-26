package com.example.bipain.boe_restaurantapp.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by hoang on 10/06/2017.
 */

public class DishNotification {
    @SerializedName("dish_id")
    private int dishId;
    @SerializedName("dish_name")
    private String dishName;

    public int getDishId() {
        return dishId;
    }

    public String getDishName() {
        return dishName;
    }

    public DishNotification(int dishId, String dishName) {
        this.dishId = dishId;
        this.dishName = dishName;
    }
}
