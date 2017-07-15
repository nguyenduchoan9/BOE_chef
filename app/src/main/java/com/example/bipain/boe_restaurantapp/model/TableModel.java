package com.example.bipain.boe_restaurantapp.model;

import com.google.gson.annotations.SerializedName;
import java.util.List;

/**
 * Created by hoang on 06/07/2017.
 */

public class TableModel {
    @SerializedName("table_number")
    private int tableNumber;
    @SerializedName("dish_detail")
    private List<DishTableInfo> dishList;

    public int getTableNumber() {
        return tableNumber;
    }

    public List<DishTableInfo> getDishList() {
        return dishList;
    }
}
