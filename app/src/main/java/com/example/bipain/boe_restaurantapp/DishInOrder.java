package com.example.bipain.boe_restaurantapp;

import com.example.bipain.boe_restaurantapp.model.Dish;
import java.util.Date;

/**
 * Created by BiPain on 6/5/2017.
 */

public class DishInOrder {
    private int quantity;
    private Dish dish;
    private boolean checked;
    private Date identity;
    private boolean overMaterial = false;

    public DishInOrder(int quantity, Dish dish) {
        this.quantity = quantity;
        this.dish = dish;
        checked = false;
    }

    public Date getIdentity() {
        return identity;
    }

    public void setIdentity(Date identity) {
        this.identity = identity;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
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

    public boolean isOverMaterial() {
        return overMaterial;
    }

    public void setOverMaterial(boolean overMaterial) {
        this.overMaterial = overMaterial;
    }
}
