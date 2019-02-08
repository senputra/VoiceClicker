package com.doodee.voiceclicker;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class CustomViewPager extends ViewPager {

    private boolean isSwipeable;

    public CustomViewPager(@NonNull Context context) {
        super(context);
        isSwipeable = true;
    }

    public CustomViewPager(@NonNull Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        isSwipeable = true;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return super.onTouchEvent(event);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        if (isSwipeable) {
            return super.onInterceptTouchEvent(event);
        }

        return false;
    }

    public void setSwipeable(boolean b) {
        this.isSwipeable = b;
    }
}
