package com.example.bipain.boe_restaurantapp;

import com.example.bipain.boe_restaurantapp.model.Dish;

/**
 * Created by BiPain on 6/5/2017.
 */

public class DishInOrder {
    private int quantity;
    private Dish dish;

    public DishInOrder(int quantity, Dish dish) {
        this.quantity = quantity;
        this.dish = dish;
    }

    public DishInOrder() {
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Dish getDish() {
        return dish;
    }

    public void setDish(Dish dish) {
        this.dish = dish;
    }
}
