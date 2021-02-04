package com.example.addressproject;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewConfiguration;

/**
 * Created by Android Studio.
 * User: lishuai
 * Date: 2021/1/29
 * Time: 下午2:34
 */
class CustomViewPager  extends ViewPager {

    private boolean isCanPageScroll = true;

    private int scaledTouchSlop = 0;

    private float startX = 0f;

    private float startY = 0f;

    public CustomViewPager(Context context) {
        this(context,null);
    }

    public CustomViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        scaledTouchSlop = ViewConfiguration.get(context).getScaledPagingTouchSlop();
    }

    public void setCanPageScroll(boolean isCanScroll){
        this.isCanPageScroll = isCanScroll;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if(!isCanPageScroll && interceptTouchEvent(ev))
        {
            return true;
        }
        return  super.onTouchEvent(ev);
    }

    /**
     * 判断是否拦截滑动
     * @param ev
     * @return
     */
    private boolean interceptTouchEvent(MotionEvent ev)
    {
        float curX = ev.getX();
        float curY = ev.getY();

        switch (ev.getAction())
        {
            case MotionEvent.ACTION_DOWN:
                startX = curX;
                startY = curY;
                break;
            case MotionEvent.ACTION_MOVE:
                if(Math.abs(curY-startY)<Math.abs(curX-startX) && Math.abs(curX-startX)>=scaledTouchSlop)
                {
                    return true;
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                startX = 0f;
                startY = 0f;
                break;

        }
        return false;
    }

}
