package com.example.bipain.boe_restaurantapp.utils;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import com.example.bipain.boe_restaurantapp.R;

/**
 * Created by hoang on 06/07/2017.
 */

public class CanvasLinearLayout extends LinearLayout{
    public CanvasLinearLayout(Context context) {
        super(context);
    }

    public CanvasLinearLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public CanvasLinearLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Paint p = new Paint();
        p.setColor(ContextCompat.getColor(getContext(), R.color.colorPrimaryDark));
        p.setStrokeWidth(1F);
        canvas.drawLine(0,0, this.getWidth(), 0, p);
    }
}
