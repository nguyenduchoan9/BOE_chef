package com.example.bipain.boe_restaurantapp;

import java.util.ArrayList;

/**
 * Created by BiPain on 6/5/2017.
 */

public class QueueOrder {
    private int orderId;
    private ArrayList<DishInOrder> orderDetail;

    public QueueOrder(int orderId, ArrayList<DishInOrder> orderDetail) {
        this.orderId = orderId;
        this.orderDetail = orderDetail;
    }

    public QueueOrder() {
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public ArrayList<DishInOrder> getOrderDetail() {
        return orderDetail;
    }

    public void setOrderDetail(ArrayList<DishInOrder> orderDetail) {
        this.orderDetail = orderDetail;
    }
}
