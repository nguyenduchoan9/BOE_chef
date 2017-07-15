package com.example.bipain.boe_restaurantapp.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by hoang on 10/07/2017.
 */

public class DishTableInfo {
    private DishSeri dish;
    @SerializedName("quantity_not_serve")
    private int quantiyNotServe;
    @SerializedName("order_detail_id")
    private int orderDetailId;

    public int getQuantiyNotServe() {
        return quantiyNotServe;
    }

    public DishSeri getDish() {
        return dish;
    }

    public void setDish(DishSeri dish) {
        this.dish = dish;
    }

    public int getOrderDetailId() {
        return orderDetailId;
    }

    public void setOrderDetailId(int orderDetailId) {
        this.orderDetailId = orderDetailId;
    }
}
