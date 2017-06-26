package com.example.bipain.boe_restaurantapp.model;

import com.example.bipain.boe_restaurantapp.utils.Constant;
import com.google.gson.annotations.SerializedName;
import java.util.Date;

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

    public WaiterNotification(int tableNumber, DishNotification dish) {
        this.tableNumber = tableNumber;
        this.dish = dish;
    }

    private Date timing;

    public void initCountTime() {
        timing = new Date();
        type = Constant.IN_TIME;
    }

    public boolean isWaitLong() {
        Date currentTime = new Date();
        return (currentTime.getTime() - timing.getTime()) > 20000;
    }

    private int type;

    public int getType() {
        return type;
    }

    public void setOverTime() {
        type = Constant.OVER_TIME;
    }

    private boolean notify = false;

    public boolean isNotify() {
        return notify;
    }

    public void setNotified() {
        notify = true;
    }
}
