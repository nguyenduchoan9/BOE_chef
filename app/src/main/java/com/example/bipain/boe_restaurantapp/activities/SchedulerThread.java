package com.example.bipain.boe_restaurantapp.activities;

import android.os.Handler;
import android.os.Message;
import com.example.bipain.boe_restaurantapp.model.WaiterNotification;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by hoang on 24/06/2017.
 */

public class SchedulerThread implements Runnable {
    private String LOG_TAG = "XXXSCHEDULER";
    private List<WaiterNotification> itemTiming = new ArrayList<>();
    private List<WaiterNotification> itemTimingMore = new ArrayList<>();
    private Handler myHandler;

    public void setMyHandler(Handler myHandler) {
        this.myHandler = myHandler;
    }

    public void setItemTiming(List<WaiterNotification> itemTiming) {
        this.itemTiming = itemTiming;
    }

    public void addItem(WaiterNotification timing) {
        itemTimingMore.add(timing);
    }

    private boolean flagStop = true;

    public void stopThread() {
        this.flagStop = false;
    }

    @Override
    public void run() {
        android.os.Process.setThreadPriority(android.os.Process.THREAD_PRIORITY_BACKGROUND);
        while (flagStop) {
            if (null != itemTiming) {
                if (itemTiming.size() > 0) {
                    List<WaiterNotification> newList = new ArrayList<>();
                    for (WaiterNotification timing : itemTiming) {
                        newList.add(timing);
                        if (timing.isWaitLong()) {
                            if (null != myHandler) {
                                Message msg = new Message();
                                msg.obj = timing;
                                myHandler.sendMessage(msg);
                            }
                            newList.remove(newList.size() - 1);
                        }
                    }
                    itemTiming = newList;
                }
                if (itemTimingMore.size() > 0) {
                    itemTiming.addAll(itemTimingMore);
                    itemTimingMore = new ArrayList<>();
                }
            }
        }
    }
}
