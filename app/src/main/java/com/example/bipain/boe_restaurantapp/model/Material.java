package com.example.bipain.boe_restaurantapp.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by hoang on 27/06/2017.
 */

public class Material {
    private int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    private String name;
    @SerializedName("available")
    private boolean isAvailable;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public void setAvailable(boolean available) {
        isAvailable = available;
    }
}
