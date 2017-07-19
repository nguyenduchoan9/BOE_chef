package com.example.bipain.boe_restaurantapp;

import com.example.bipain.boe_restaurantapp.model.Dish;
import com.google.gson.annotations.SerializedName;
import java.util.Date;

/**
 * Created by BiPain on 6/5/2017.
 */

public class DishInQueue {
    private Date identity;
    private boolean checked = false;
    private Dish dish;
    @SerializedName("order_id")
    private int orderId;

    public DishInQueue(Dish dish, int orderId) {
        this.dish = dish;
        this.orderId = orderId;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public void setCurrentTime() {
        identity = new Date();
    }

    public boolean isMe(Date identity) {
        return (this.identity.getTime() - identity.getTime()) == 0;
    }

    public Date getIdentity() {
        return identity;
    }

    public DishInQueue() {
    }

    public Dish getDish() {
        return dish;
    }

    public void setDish(Dish dish) {
        this.dish = dish;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }
}
