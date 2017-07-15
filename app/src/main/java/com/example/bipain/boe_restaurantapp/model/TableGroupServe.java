package com.example.bipain.boe_restaurantapp.model;

/**
 * Created by hoang on 15/07/2017.
 */

public class TableGroupServe {
    public int orderDetailId;
    public int quantity;
    public int quantityCountInThread;
    public int quantityCountInFragment;

    public TableGroupServe(int orderDetailId, int quantity) {
        this.orderDetailId = orderDetailId;
        this.quantity = quantity;
        quantityCountInFragment = quantity;
        quantityCountInThread = quantity;
    }
}
