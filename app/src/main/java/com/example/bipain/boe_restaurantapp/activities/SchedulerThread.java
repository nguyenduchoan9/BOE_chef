package com.example.bipain.boe_restaurantapp.activities;

import android.os.Handler;
import android.os.Message;
import com.example.bipain.boe_restaurantapp.model.TableGroupServe;
import com.example.bipain.boe_restaurantapp.model.WaiterNotification;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by hoang on 24/06/2017.
 */

public class SchedulerThread implements Runnable {
    private String LOG_TAG = "XXXSCHEDULER";
    private LinkedList<WaiterNotification> itemTiming = new LinkedList<>();
    private LinkedList<WaiterNotification> itemTimingPlus = new LinkedList<>();
    private LinkedList<WaiterNotification> itemServed = new LinkedList<>();
    private LinkedList<TableGroupServe> dishTableServed = new LinkedList<>();
    private Handler myHandler;
    private Handler handleLongTime;
    private boolean isCallingApi = false;

    public void blockThread() {
        isCallingApi = true;
    }

    public void releaseThread() {
        isCallingApi = false;
    }

    public void setHandleLongTime(Handler handleLongTime) {
        this.handleLongTime = handleLongTime;
    }

    public void setMyHandler(Handler myHandler) {
        this.myHandler = myHandler;
    }

    public void setItemTiming(LinkedList<WaiterNotification> itemTiming) {
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
        if (!isCallingApi) {
            if (null != itemTiming) {
                if (itemServed.size() > 0) {
                    LinkedList<WaiterNotification> found = new LinkedList<>();
                    for (WaiterNotification served : itemServed) {
//                    int pos = -1;
                        for (int i = 0; i < itemTiming.size(); i++) {
                            WaiterNotification current = itemTiming.get(i);
                            if (served.getOrderDetailId() == current.getOrderDetailId()
                                    && served.getUid() == current.getUid()) {
//                            pos = i;
                                if (-1 == found.indexOf(current))
                                    found.add(current);
//                            break;
                            }
                        }
//                    if (-1 != pos) {
//                        itemTiming.remove(pos);
//                    }
                    }
                    if (found.size() > 0) itemTiming.removeAll(found);
                    itemServed.clear();
                }
                if (dishTableServed.size() > 0) {
                    for (int x = 0; x < dishTableServed.size(); x++) {
                        int dishServedOD = dishTableServed.get(x).orderDetailId;
//                    int pos = -1;
                        LinkedList<WaiterNotification> found = new LinkedList<>();
//                    for (int abc = 0; abc < ; abc++) {
                        for (int i = 0; i < itemTiming.size(); i++) {
                            WaiterNotification current = itemTiming.get(i);
                            if (dishServedOD == current.getOrderDetailId()) {
//                                pos = i;
                                if (-1 == found.indexOf(current)) {
                                    found.add(current);
                                    if (dishTableServed.get(x).quantityCountInThread == found.size())
                                        break;
                                }
                            }
                        }
//                    }
                        if (found.size() > 0) itemTiming.removeAll(found);
                    }
                    dishTableServed.clear();
                }
                if (itemTiming.size() > 0) {
                    LinkedList<WaiterNotification> newList = new LinkedList<>();
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
                    itemTiming.clear();
                    itemTiming.addAll(newList);
                }
                if (itemTimingPlus.size() > 0) {
                    itemTiming.addAll(itemTimingPlus);
                    itemTimingPlus = new LinkedList<>();
                }
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
        if (dishTableServed.size() > 0) {
            for (TableGroupServe group : dishTableServed) {
                if (noti.getOrderDetailId() == group.orderDetailId) {
                    return true;
                }
            }
        }
        return false;
    }
}
