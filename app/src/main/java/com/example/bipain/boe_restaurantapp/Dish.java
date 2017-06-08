package com.example.bipain.boe_restaurantapp;

/**
 * Created by BiPain on 6/5/2017.
 */

public class Dish {
    private int dishId;
    private String name;

    public Dish(int dishId, String name) {
        this.dishId = dishId;
        this.name = name;
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
}
