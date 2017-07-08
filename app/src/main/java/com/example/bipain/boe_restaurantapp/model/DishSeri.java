package com.example.bipain.boe_restaurantapp.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by hoang on 06/07/2017.
 */

public class DishSeri {
    private int id;
    @SerializedName("dish_name")
    private String name;
    private String description;
    private float price;
    private String image;

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public float getPrice() {
        return price;
    }

    public String getImage() {
        return image;
    }
}
