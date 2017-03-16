package com.sugar.myapplication.view;

import android.content.Context;
import android.widget.Scroller;

/**
 * Created by wangyu on 2016/11/28.
 */

public class FixedSpeedScroller extends Scroller {
    private int mDuration = 1000;

    public FixedSpeedScroller(Context context) {
        super(context);
    }

    public FixedSpeedScroller(Context context, android.view.animation.Interpolator interpolator) {
        super(context,interpolator);
    }

    public FixedSpeedScroller(Context context, android.view.animation.Interpolator interpolator, boolean flywheel) {
        super(context, interpolator, flywheel);
    }

    @Override
    public void startScroll(int startX, int startY, int dx, int dy, int duration) {

        if(duration == 200)//调用setCurrentItem滑动的固定速度
            super.startScroll(startX, startY, dx, dy, mDuration);
        else //其他按照正常速度来
            super.startScroll(startX, startY, dx, dy, duration);
    }


    @Override
    public void startScroll(int startX, int startY, int dx, int dy) {
        super.startScroll(startX, startY, dx, dy, mDuration);
    }
}
