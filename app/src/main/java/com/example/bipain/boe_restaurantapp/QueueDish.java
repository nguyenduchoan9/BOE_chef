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
    public QueueDish(){
        this.queues=new LinkedList<>();
    }

    public DishInQueue removeFirstDishOrder(int dishId) {
        for (DishInQueue dish : queues) {
            if (dish.getDish().getDishId() == dishId) {
                queues.remove(dish);
                return dish;
            }
        }
        return null;
    }

    public void addDishInQueue(DishInQueue dishInQueue) {
        queues.add(dishInQueue);
    }

    public LinkedList<DishInQueue> getQueues() {
        return queues;
    }

    public void setQueues(LinkedList<DishInQueue> queues) {
        this.queues = queues;
    }
}
