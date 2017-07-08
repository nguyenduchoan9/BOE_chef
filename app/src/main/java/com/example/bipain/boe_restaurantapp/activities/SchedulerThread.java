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
    private Handler handleLongTime;

    public void setHandleLongTime(Handler handleLongTime) {
        this.handleLongTime = handleLongTime;
    }

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
//        while (flagStop) {
        if (null != itemTiming) {
            if (itemTiming.size() > 0) {
                List<WaiterNotification> newList = new ArrayList<>();
                for (WaiterNotification timing : itemTiming) {
                    newList.add(timing);
                    if (timing.isTooLong()) {
                        if (null != handleLongTime) {
                            Message msg = new Message();
                            Message msg2 = new Message();
                            timing.setNotifyToWarning();
                            msg.obj = timing;
                            msg2.obj = timing;
                            handleLongTime.sendMessage(msg2);
                            myHandler.sendMessage(msg);
                        }
                        newList.remove(newList.size() - 1);
                    } else if (timing.isWaitShort() && !timing.isNotifyToShort()) {
                        if (null != myHandler) {
                            Message msg = new Message();
                            timing.setNotifiedShort();
                            msg.obj = timing;
                            myHandler.sendMessage(msg);
                        }
                    }
                }
                itemTiming = newList;
            }
            if (itemTimingMore.size() > 0) {
                itemTiming.addAll(itemTimingMore);
                itemTimingMore = new ArrayList<>();
            }
        }
        myHandler.postDelayed(this, 500);
//        }
    }
}
