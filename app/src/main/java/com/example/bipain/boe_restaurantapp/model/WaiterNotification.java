package com.example.bipain.boe_restaurantapp.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by hoang on 10/06/2017.
 */

public class WaiterNotification {
    @SerializedName("table_number")
    private int tableNumber;
    @SerializedName("dish")
    private DishNotification dish;

    public int getTableNumber() {
        return tableNumber;
    }

    public DishNotification getDish() {
        return dish;
    }
}
