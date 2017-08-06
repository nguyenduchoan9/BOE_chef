package com.example.bipain.boe_restaurantapp.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by hoang on 03/08/2017.
 */

public class CashModel {
    private float total;
    @SerializedName("order_id")
    private int orderId;
    @SerializedName("table_number")
    private int tableNumber;

    public float getTotal() {
        return total;
    }

    public void setTotal(float total) {
        this.total = total;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public int getTableNumber() {
        return tableNumber;
    }

    public void setTableNumber(int tableNumber) {
        this.tableNumber = tableNumber;
    }
}
