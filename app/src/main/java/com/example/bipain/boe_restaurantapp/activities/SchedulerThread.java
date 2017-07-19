package com.example.bipain.boe_restaurantapp.activities;

import android.os.Handler;
import android.os.Message;
import com.example.bipain.boe_restaurantapp.model.TableGroupServe;
import com.example.bipain.boe_restaurantapp.model.WaiterNotification;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by hoang on 24/06/2017.
 */

public class SchedulerThread implements Runnable {
    private String LOG_TAG = "XXXSCHEDULER";
    private List<WaiterNotification> itemTiming = new ArrayList<>();
    private List<WaiterNotification> itemTimingPlus = new ArrayList<>();
    private List<WaiterNotification> itemServed = new ArrayList<>();
    private List<TableGroupServe> dishTableServed = new ArrayList<>();
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
        itemTimingPlus.add(timing);
    }

    private boolean flagStop = true;

    public void stopThread() {
        this.flagStop = false;
    }

    public void addServedItem(WaiterNotification notification) {
        itemServed.add(notification);
    }

    public void setItemTableServed(List<TableGroupServe> served) {
        dishTableServed.addAll(served);
    }

    @Override
    public void run() {
        android.os.Process.setThreadPriority(android.os.Process.THREAD_PRIORITY_BACKGROUND);
//        while (flagStop) {
        if (null != itemTiming) {
            if (itemServed.size() > 0) {
                for (WaiterNotification served : itemServed) {
                    int pos = -1;
                    for (int i = 0; i < itemTiming.size(); i++) {
                        WaiterNotification current = itemTiming.get(i);
                        if (served.getOrderDetailId() == current.getOrderDetailId()
                                && served.getUid() == current.getUid()) {
                            pos = i;
                            break;
                        }
                    }
                    if (-1 != pos) {
                        itemTiming.remove(pos);
                    }
                }
                itemServed.clear();
            }
            if (dishTableServed.size() > 0) {
                for (int x = 0; x < dishTableServed.size(); x++) {
                    int dishServedOD = dishTableServed.get(x).orderDetailId;
                    int pos = -1;
                    for (int abc = 0; abc < dishTableServed.get(x).quantityCountInFragment; abc++) {
                        for (int i = 0; i < itemTiming.size(); i++) {
                            WaiterNotification current = itemTiming.get(i);
                            if (dishServedOD == current.getOrderDetailId()) {
                                pos = i;
                                break;
                            }
                        }
                        if (-1 != pos) {
                            itemTiming.remove(pos);
                        }
                    }
                }
                dishTableServed.clear();
            }
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
                            if (!isInItemServe(timing)) {
                                handleLongTime.sendMessage(msg2);
                                myHandler.sendMessage(msg);
                            }
                        }
                        newList.remove(newList.size() - 1);
                    } else if (timing.isWaitShort() && !timing.isNotifyToShort()) {
                        if (null != myHandler) {
                            Message msg = new Message();
                            timing.setNotifiedShort();
                            msg.obj = timing;
                            if (!isInItemServe(timing)) {
                                myHandler.sendMessage(msg);
                            }
                        }
                    }
                }
                itemTiming = newList;
            }
            if (itemTimingPlus.size() > 0) {
                itemTiming.addAll(itemTimingPlus);
                itemTimingPlus = new ArrayList<>();
            }
        }
        myHandler.postDelayed(this, 200);
//        }
    }

    private boolean isInItemServe(WaiterNotification noti) {
        if (itemServed.size() > 0) {
            for (WaiterNotification notification : itemServed) {
                if (noti.getUid() == notification.getUid() &&
                        noti.getOrderDetailId() == notification.getOrderDetailId()) {
                    return true;
                }
            }
        }
        return false;
    }
}
