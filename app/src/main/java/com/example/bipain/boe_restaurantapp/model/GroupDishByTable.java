package com.example.bipain.boe_restaurantapp.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by hoang on 19/07/2017.
 */

public class GroupDishByTable {
    private int orderDetailId;
    private int tableNumber;
    private List<Date> uids;
    private int quantity;

    public GroupDishByTable(int orderDetailId, int tableNumber, Date uid) {
        this.orderDetailId = orderDetailId;
        this.tableNumber = tableNumber;
        uids = new ArrayList<>();
        uids.add(uid);
        quantity = 1;
    }

    public int getOrderDetailId() {
        return orderDetailId;
    }

    public void setOrderDetailId(int orderDetailId) {
        this.orderDetailId = orderDetailId;
    }

    public int getTableNumber() {
        return tableNumber;
    }

    public void setTableNumber(int tableNumber) {
        this.tableNumber = tableNumber;
    }

    public List<Date> getUids() {
        return uids;
    }

    public void setUids(List<Date> uids) {
        this.uids = uids;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
