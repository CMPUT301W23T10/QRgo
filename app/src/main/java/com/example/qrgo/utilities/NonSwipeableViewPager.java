package com.example.qrgo.utilities;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;

/**
 A custom ViewPager that disables both swiping and scrolling.
 */
public class NonSwipeableViewPager extends ViewPager {

    /**
     Constructor for creating the NonSwipeableViewPager programmatically.
     @param context the Context to use
     */
    public NonSwipeableViewPager(@NonNull Context context) {
        super(context);
    }

    /**
     Constructor for inflating the NonSwipeableViewPager from XML.
     @param context the Context to use
     @param attrs the AttributeSet to use
     */
    public NonSwipeableViewPager(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     Overrides the default behavior of intercepting touch events to disable swiping.
     @param event the MotionEvent to handle
     @return always returns false to disable swiping
     */
    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        // Always return false to disable swiping
        return false;
    }

    /**
     Overrides the default behavior of handling touch events to disable scrolling.
     @param event the MotionEvent to handle
     @return always returns false to disable scrolling
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // Always return false to disable scrolling
        return false;
    }
}
