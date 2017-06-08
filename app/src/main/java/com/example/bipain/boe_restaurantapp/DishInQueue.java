package com.example.bipain.boe_restaurantapp;

/**
 * Created by BiPain on 6/5/2017.
 */

public class DishInQueue {
    private int dishId;
    private int orderId;

    public DishInQueue(int dishId, int orderId) {
        this.dishId = dishId;
        this.orderId = orderId;
    }

    public DishInQueue() {
    }

    public int getDishId() {
        return dishId;
    }

    public void setDishId(int dishId) {
        this.dishId = dishId;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }
}
