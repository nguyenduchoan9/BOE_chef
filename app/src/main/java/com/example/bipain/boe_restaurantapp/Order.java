package com.example.bipain.boe_restaurantapp;

/**
 * Created by BiPain on 6/4/2017.
 */

public class Order {
    private int numberDish;
    private int total;
    private int orderId;

    public Order(int numberDish, int total, int orderId) {
        this.numberDish = numberDish;
        this.total = total;
        this.orderId = orderId;
    }

    public Order() {
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
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
}
