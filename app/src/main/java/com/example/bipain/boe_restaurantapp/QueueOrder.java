package com.example.bipain.boe_restaurantapp;

import java.util.ArrayList;

/**
 * Created by BiPain on 6/5/2017.
 */

public class QueueOrder {
    private int orderId;
    private int numberDish;
    private int total;
    private ArrayList<DishInOrder> orderDetail;

    public QueueOrder(int orderId, ArrayList<DishInOrder> orderDetail) {
        this.orderId = orderId;
        this.orderDetail = orderDetail;
    }

    public QueueOrder(int orderId, int numberDish, int total, ArrayList<DishInOrder> orderDetail) {
        this.orderId = orderId;
        this.numberDish = numberDish;
        this.total = total;
        this.orderDetail = orderDetail;
    }

    public QueueOrder() {
    }

    public int getNumberDish() {
        return numberDish;
    }

    public void setNumberDish(int numberDish) {
        this.numberDish = numberDish;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
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
