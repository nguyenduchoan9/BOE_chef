package com.example.bipain.boe_restaurantapp;

import com.example.bipain.boe_restaurantapp.model.Dish;

/**
 * Created by BiPain on 6/5/2017.
 */

public class DishIsOrder extends Dish {
    private int quantity;

    public DishIsOrder() {
    }

    public DishIsOrder(int dishId, String name, int quantity) {
        super(dishId, name);
        this.quantity = quantity;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
