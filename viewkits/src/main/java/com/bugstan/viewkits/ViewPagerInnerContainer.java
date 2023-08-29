package com.bugstan.viewkits;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;

import androidx.viewpager2.widget.ViewPager2;

/**
 * ViewPager Inner Container is a middle layer layout.
 * It is used to solve the problem of RecyclerView sliding conflict after ViewPager2 is nested in ViewPager2
 * ViewPager2 嵌套滑动冲突容器
 */
public class ViewPagerInnerContainer extends RelativeLayout {
    private ViewPager2 mViewPager;

    // When pressed, request the parent layout not to intercept the event
    private final static boolean DISALLOW_PARENT_INTERCEPTOR_DOWN_EVENT = true;

    private boolean isDisallowParentInterceptorDownEvent;

    private int startX = 0;
    private int startY = 0;

    public ViewPagerInnerContainer(Context context) {
        this(context, null);
    }

    public ViewPagerInnerContainer(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ViewPagerInnerContainer(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        try {
            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ViewPagerInnerContainer, defStyleAttr, 0);

            isDisallowParentInterceptorDownEvent = a.getBoolean(R.styleable.ViewPagerInnerContainer_intercept_down_event, DISALLOW_PARENT_INTERCEPTOR_DOWN_EVENT);
            a.recycle();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            if (child instanceof ViewPager2) {
                mViewPager = (ViewPager2) child;
            }
            if (mViewPager == null) {
                throw new IllegalStateException("ViewPagerContainer must has a child is viewpager2");
            }
        }
    }

    /**
     * Intercept Touch Event
     *
     * @param ev The motion event being dispatched down the hierarchy.
     * @return boolean
     */
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        boolean notNeedInterceptor = !mViewPager.isUserInputEnabled() || (mViewPager.getAdapter() != null && mViewPager.getAdapter().getItemCount() <= 1);
        if (notNeedInterceptor) return super.onInterceptTouchEvent(ev);

        switch (ev.getAction()) {
            // When pressed, request the parent layout not to intercept the event
            // 按下的时候 请求父布局不要拦截事件
            case MotionEvent.ACTION_DOWN:
                startX = (int) ev.getX();
                startY = (int) ev.getY();
                getParent().requestDisallowInterceptTouchEvent(!isDisallowParentInterceptorDownEvent);
                break;

            case MotionEvent.ACTION_MOVE:
                int endX = (int) ev.getX();
                int endY = (int) ev.getY();
                int disX = Math.abs(endX - startX);
                int disY = Math.abs(endY - startY);

                // slide horizontally
                if (mViewPager.getOrientation() == ViewPager2.ORIENTATION_HORIZONTAL) {
                    horizontalSwipeHandler(endX, disX, disY);
                } else if (mViewPager.getOrientation() == ViewPager2.ORIENTATION_VERTICAL) {
                    // slide vertically
                    verticalSwipeHandler(endY, disX, disY);
                }
                break;

            default:
                break;
        }

        return super.onInterceptTouchEvent(ev);
    }

    /**
     * Handle horizontal swipe events
     * 处理水平滑动事件
     *
     * @param endX end point x
     * @param disX distance x
     * @param disY distance y
     */
    private void horizontalSwipeHandler(int endX, int disX, int disY) {
        if (mViewPager.getAdapter() == null) return;

        if (disX > disY) {
            int currentItemIndex = mViewPager.getCurrentItem();
            int itemCount = mViewPager.getAdapter().getItemCount();
            if (currentItemIndex == 0 && endX - startX > 0) {
                getParent().requestDisallowInterceptTouchEvent(false);
            } else {
                getParent().requestDisallowInterceptTouchEvent(currentItemIndex != itemCount - 1 || endX - startX > 0);
            }
        } else if (disY > disX) {
            getParent().requestDisallowInterceptTouchEvent(false);
        }

    }

    /**
     * Handle vertical swipe events
     * 处理垂直滑动事件
     *
     * @param endY end point y
     * @param disX distance x
     * @param disY distance y
     */
    private void verticalSwipeHandler(int endY, int disX, int disY) {
        if (mViewPager.getAdapter() == null) return;
        if (disY > disX) {
            int currentItemIndex = mViewPager.getCurrentItem();
            int itemCount = mViewPager.getAdapter().getItemCount();

            if (currentItemIndex == 0 && endY - startY > 0) {
                getParent().requestDisallowInterceptTouchEvent(false);
            } else {
                boolean result = currentItemIndex != itemCount - 1 || endY - startY >= 0;
                getParent().requestDisallowInterceptTouchEvent(result);
            }
        } else if (disX > disY) {
            getParent().requestDisallowInterceptTouchEvent(false);
        }
    }

    /**
     * get the inner ViewPager2
     *
     * @return ViewPager2
     */
    public ViewPager2 getViewPager() {
        return mViewPager;
    }

    /**
     * set the inner ViewPager2
     *
     * @param viewPager ViewPager2
     */
    public void setViewPager(ViewPager2 viewPager) {
        mViewPager = viewPager;
    }

    /**
     * set allow Parent Interceptor Down Event
     *
     * @param allow boolean
     */
    public void setInterceptDownEvent(boolean allow) {
        isDisallowParentInterceptorDownEvent = allow;
    }

    /**
     * get allow Parent Interceptor Down Event set
     *
     * @return boolean
     */
    public boolean isInterceptDownEvent() {
        return isDisallowParentInterceptorDownEvent;
    }
}