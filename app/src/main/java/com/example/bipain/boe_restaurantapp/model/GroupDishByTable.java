package com.example.bipain.boe_restaurantapp.model;

import android.support.annotation.NonNull;
import java.security.acl.Group;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by hoang on 19/07/2017.
 */

public class GroupDishByTable implements Comparable<GroupDishByTable> {
    private int orderDetailId;
    private int tableNumber;
    private List<Date> uids;
    private int quantity;
    private boolean isShortNotify;
    private boolean isWarning;


    public GroupDishByTable() {
    }

    public GroupDishByTable(int orderDetailId, int tableNumber, Date uid) {
        this.orderDetailId = orderDetailId;
        this.tableNumber = tableNumber;
        uids = new ArrayList<>();
        uids.add(uid);
        quantity = 1;
        isShortNotify = false;
        isWarning = false;
    }

    public boolean isWarning() {
        return isWarning;
    }

    public void setWarning(boolean warning) {
        isWarning = warning;
    }

    public boolean isShortNotify() {
        return isShortNotify;
    }

    public void setShortNotify(boolean shortNotify) {
        isShortNotify = shortNotify;
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

    @Override
    public int compareTo(@NonNull GroupDishByTable o) {
        return this.tableNumber - o.getTableNumber();
    }
}
