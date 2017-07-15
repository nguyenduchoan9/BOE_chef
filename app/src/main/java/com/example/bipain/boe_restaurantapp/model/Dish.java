package com.example.bipain.boe_restaurantapp.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by BiPain on 6/5/2017.
 */

public class Dish {
    @SerializedName("dish_id")
    private int dishId;
    @SerializedName("dish_name")
    private String name;
    @SerializedName("order_detail_id")
    private int orderDetailId;

    public int getOrderDetailId() {
        return orderDetailId;
    }

    private int categoryId;

    public Dish(int dishId, String name) {
        this.dishId = dishId;
        this.name = name;
    }

    public Dish(int dishId, String name, int categoryId) {
        this.dishId = dishId;
        this.name = name;
        this.categoryId = categoryId;
    }

    public Dish() {
    }

    public int getDishId() {
        return dishId;
    }

    public void setDishId(int dishId) {
        this.dishId = dishId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }
}
