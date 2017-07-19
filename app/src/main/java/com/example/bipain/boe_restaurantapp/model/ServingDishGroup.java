package com.example.bipain.boe_restaurantapp.model;

import java.util.List;

/**
 * Created by hoang on 19/07/2017.
 */

public class ServingDishGroup {
    private int dishId;
    private String dishName;
    private List<GroupDishByTable> groupDishByTableList;

    public ServingDishGroup(int dishId, String dishName, List<GroupDishByTable> groupDishByTableList) {
        this.dishId = dishId;
        this.dishName = dishName;
        this.groupDishByTableList = groupDishByTableList;
    }

    public int getDishId() {
        return dishId;
    }

    public void setDishId(int dishId) {
        this.dishId = dishId;
    }

    public String getDishName() {
        return dishName;
    }

    public void setDishName(String dishName) {
        this.dishName = dishName;
    }

    public List<GroupDishByTable> getGroupDishByTableList() {
        return groupDishByTableList;
    }

    public void setGroupDishByTableList(List<GroupDishByTable> groupDishByTableList) {
        this.groupDishByTableList = groupDishByTableList;
    }
}
