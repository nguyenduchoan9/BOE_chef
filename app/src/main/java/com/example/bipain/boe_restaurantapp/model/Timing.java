package com.example.bipain.boe_restaurantapp.model;

import java.util.Date;

/**
 * Created by hoang on 24/06/2017.
 */

public class Timing {
    public int id;
    private Date timing;

    public Timing(int id) {
        this.id = id;
        timing = new Date();
    }

    public boolean isWaitLong() {
        Date currentTime = new Date();
        return (currentTime.getTime() - timing.getTime()) > 10000;
    }
}
