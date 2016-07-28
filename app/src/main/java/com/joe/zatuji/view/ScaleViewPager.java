package com.joe.zatuji.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * description - 处理缩放时的崩溃问题
 * <p/>
 * author - Joe.
 * create on 16/7/28.
 * change
 * change on .
 */


public class ScaleViewPager extends ViewPager{
    public ScaleViewPager(Context context) {
        super(context);
    }

    public ScaleViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        try {
            return super.onInterceptTouchEvent(ev);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            return false;
        }

    }
}
