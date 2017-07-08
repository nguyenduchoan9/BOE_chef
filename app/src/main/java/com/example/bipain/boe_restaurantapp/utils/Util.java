package com.example.bipain.boe_restaurantapp.utils;

import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;

/**
 * Created by hoang on 24/06/2017.
 */

public class Util {
    public static void lockObjectAndWait(Object object, boolean[] lock) {
        // after adding the Request to the volley queue
        synchronized (object) {
            try {
                while (!lock[0]) {
                    object.wait();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static void unLockObject(Object object, boolean[] lock) {
        synchronized (object) {
            lock[0] = true;
            object.notify();
        }
    }

    public static Drawable changeDrawableColor(Drawable drawable, int newColor) {
//        Drawable mDrawable = ContextCompat.getDrawable(context, icon).mutate();
        drawable.setColorFilter(new PorterDuffColorFilter(newColor, PorterDuff.Mode.SRC_IN));
        return drawable;
    }
}
