package com.example.bipain.boe_restaurantapp;

import java.util.LinkedList;

/**
 * Created by BiPain on 6/5/2017.
 */

public class QueueDish {
    private LinkedList<DishInQueue> queues;

    public QueueDish(LinkedList<DishInQueue> queues) {
        this.queues = queues;
    }

    public DishInQueue removeFirstDishOrder(int dishId) {
        for (DishInQueue dish : queues) {
            if (dish.getDishId() == dishId) {
                queues.remove(dish);
                return dish;
            }
        }
        return null;
    }
}
