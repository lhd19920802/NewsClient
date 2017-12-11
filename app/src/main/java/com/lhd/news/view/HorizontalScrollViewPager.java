package com.lhd.news.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by lihuaidong on 2017/12/4 15:28.
 * 微信：lhd520ssp
 * QQ:414320737
 * 作用：
 */
public class HorizontalScrollViewPager extends ViewPager
{
    public HorizontalScrollViewPager(Context context)
    {
        super(context);
    }

    public HorizontalScrollViewPager(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }

    private float startX, startY;

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev)
    {

        switch (ev.getAction())
        {
            case MotionEvent.ACTION_DOWN:
                getParent().requestDisallowInterceptTouchEvent(true);
                startX = ev.getX();
                startY = ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                float distanceX = ev.getX() - startX;
                float distanceY = ev.getY() - startY;

                if (Math.abs(distanceX) > Math.abs(distanceY))
                {
                    //水平方向滑动
                    if (distanceX > 0 && getCurrentItem() == 0)
                    {
                        getParent().requestDisallowInterceptTouchEvent(false);
                    }
                    else if (distanceX < 0 && getCurrentItem() == getAdapter().getCount() - 1)
                    {
                        getParent().requestDisallowInterceptTouchEvent(false);
                    }
                    else
                    {
                        getParent().requestDisallowInterceptTouchEvent(true);
                    }
                }
                else
                {
                    //竖直方向滑动
                    getParent().requestDisallowInterceptTouchEvent(false);
                }
                break;
            case MotionEvent.ACTION_UP:

                break;
        }
        return super.dispatchTouchEvent(ev);
    }
}
