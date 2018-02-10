package com.example.administrator.chatapp.Utils;

import android.content.Context;
import android.support.v4.widget.DrawerLayout;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewConfiguration;

/**
 * Created by Administrator on 2018/1/20.
 */

public class DrawerMenu extends DrawerLayout {

    private int scaledTouchSlop;

    public DrawerMenu(Context context) {
        super(context, null);
    }

    public DrawerMenu(Context context, AttributeSet attrs) {
        super(context, attrs, 0);
    }

    public DrawerMenu(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        final ViewConfiguration configuration = ViewConfiguration
                .get(getContext());
        mTouchSlop = configuration.getScaledTouchSlop();
    }
    private int mTouchSlop;
    private float mLastMotionX;
    private float mLastMotionY;

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        try {
            final float x = ev.getX();
            final float y = ev.getY();
            switch (ev.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    mLastMotionX = x;
                    mLastMotionY = y;
                    break;
                case MotionEvent.ACTION_MOVE:
                    int xDiff = (int) Math.abs(x - mLastMotionX);
                    int yDiff = (int) Math.abs(y - mLastMotionY);
                    final int x_yDiff = xDiff * xDiff + yDiff * yDiff;
                    boolean xMoved = x_yDiff > mTouchSlop * mTouchSlop;
                    if (xMoved) {
                        if (xDiff > yDiff * 4) {
                            return true;
                        } else {
                            return false;
                        }
                    }
                    break;
                default:
                    break;
            }
            return super.onInterceptTouchEvent(ev);
        } catch (IllegalArgumentException ex) {
        }
        return false;
    }
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        try {
            return super.onTouchEvent(ev);
        } catch (IllegalArgumentException ex) {
        }
        return false;
    }
}