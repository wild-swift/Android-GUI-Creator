/*
 * Copyright (c) 2012.
 * This file is part of Android Interface Toolkit application.
 *
 * Android Interface Toolkit is free software: you can redistribute it
 * and/or modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * Android Interface Toolkit is distributed in the hope that it will be
 * useful, but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General
 * Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along
 * with Android Interface Toolkit.  If not, see <http://www.gnu.org/licenses/>.
 */

package name.wildswift.android.guitool.view;

import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.Scroller;
import name.wildswift.android.guitool.R;
import name.wildswift.android.guitool.gesture.CompositeGestureDetector;
import name.wildswift.android.guitool.gesture.OnGestureListener;
import name.wildswift.android.guitool.gesture.gestures.Gesture;
import name.wildswift.android.guitool.gesture.gestures.GestureType;

/**
 * 29.01.12
 *
 * @author Swift
 */
public class SlidePanel extends FrameLayout implements OnGestureListener{
    private static final int HIDE_DURATION = 1000;

    public Arrow arrow;
    public Scroller scroller;
    public ScrollUpdater updater = new ScrollUpdater();
    
    private int minScroll;
    private int maxScroll;

    public SlidePanel(Context context) {
        super(context);
        arrow = new Arrow(context);
        addView(arrow);
        scroller = new Scroller(context);
    }

    public SlidePanel(Context context, AttributeSet attrs) {
        super(context, attrs);
        arrow = new Arrow(context);
        addView(arrow);
        scroller = new Scroller(context);
    }

    public SlidePanel(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        arrow = new Arrow(context);
        addView(arrow);
        scroller = new Scroller(context);
    }

    
    // FIX views 
    @Override
    public void addView(View child) {
        if (getChildCount() > 1) throw new IllegalStateException("SlidePanel must contains only one child");
        super.addView(child, 0);
    }

    @Override
    public void addView(View child, int index) {
        if (getChildCount() > 1) throw new IllegalStateException("SlidePanel must contains only one child");
        super.addView(child, 0);
    }

    @Override
    public void addView(View child, int index, ViewGroup.LayoutParams params) {
        if (getChildCount() > 1) throw new IllegalStateException("SlidePanel must contains only one child");
        super.addView(child, 0, params);
    }

    @Override
    public void addView(View child, ViewGroup.LayoutParams params) {
        if (getChildCount() > 1) throw new IllegalStateException("SlidePanel must contains only one child");
        super.addView(child,0, params);
    }

    @Override
    public void addView(View child, int width, int height) {
        if (getChildCount() > 1) throw new IllegalStateException("SlidePanel must contains only one child");
        super.addView(child, 0, new ViewGroup.LayoutParams(width, height));
    }

    @Override
    protected boolean addViewInLayout(View child, int index, ViewGroup.LayoutParams params) {
        if (getChildCount() > 1) throw new IllegalStateException("SlidePanel must contains only one child");
        return super.addViewInLayout(child, 0, params);
    }

    @Override
    protected boolean addViewInLayout(View child, int index, ViewGroup.LayoutParams params, boolean preventRequestLayout) {
        if (getChildCount() > 1) throw new IllegalStateException("SlidePanel must contains only one child");
        return super.addViewInLayout(child, 0, params, preventRequestLayout);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        if (getChildCount() != 2) throw new IllegalStateException("SlidePanel must contains only one child");
        if (getChildAt(1) != arrow) throw new IllegalStateException("View is manually changed");

        Rect rect = new Rect(left, top, right, bottom);
        Rect arrowRect = new Rect(0, 0, Math.min(arrow.getMeasuredWidth(), rect.width()), Math.min(arrow.getMeasuredHeight(), rect.height()));
        Rect childRect = new Rect(0, 0, rect.width() - arrowRect.width() / 2, Math.max(rect.height(), arrowRect.height()));
        arrowRect.offset(rect.left, rect.top);
        arrowRect.offset(rect.width() - arrowRect.width(), (rect.height() - arrowRect.height()) / 2);
        childRect.offset(rect.left, rect.top);
        arrow.layout(arrowRect.left, arrowRect.top, arrowRect.right, arrowRect.bottom);
        getChildAt(0).layout(childRect.left, childRect.top, childRect.right, childRect.bottom);
        minScroll = rect.left;
        maxScroll = arrowRect.left;
        arrow.setupDrawable(minScroll, maxScroll);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (getChildCount() != 2) throw new IllegalStateException("SlidePanel must contains only one child");
        if (getChildAt(1) != arrow) throw new IllegalStateException("View is manually changed");

        arrow.measure(widthMeasureSpec, heightMeasureSpec);
        getChildAt(0).measure(MeasureSpec.makeMeasureSpec(MeasureSpec.getSize(widthMeasureSpec) - arrow.getMeasuredWidth() / 2, MeasureSpec.getMode(widthMeasureSpec)), heightMeasureSpec);
        setMeasuredDimension(getChildAt(0).getMeasuredWidth() + arrow.getMeasuredWidth() / 2, Math.max(arrow.getMeasuredHeight(), getChildAt(0).getMeasuredHeight()));
    }


    @Override
    public void scrollTo(int x, int y) {
        arrow.setCurrentLevel(x);
        super.scrollTo(x, y);
    }

    protected class Arrow extends View {

        private final BitmapDrawable drawable;
        private final CompositeGestureDetector detector;
        private final RotateDrawable backgroundDrawable;

        public Arrow(Context context) {
            super(context);
            drawable = (BitmapDrawable) context.getResources().getDrawable(R.drawable.menu_open_icon);
            backgroundDrawable = new RotateDrawable(drawable);
            setBackgroundDrawable(backgroundDrawable);
            detector = new CompositeGestureDetector(context, (byte) 1, SlidePanel.this);
        }

        @Override
        protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
            setMeasuredDimension(drawable.getBitmap().getWidth(), drawable.getBitmap().getHeight());
        }

        @Override
        public boolean onTouchEvent(MotionEvent event) {
            return detector.onMotionEvent(event);
        }

        public void setupDrawable(int min, int max){
            backgroundDrawable.setMinLevel(min);
            backgroundDrawable.setMaxLevel(max);
        }

        public void setCurrentLevel(int level){
            backgroundDrawable.setLevel(level);
        }
    }

    public void onGesture(Gesture gesture) {
        Log.w(getClass().getSimpleName(), "onGesture");
        if (gesture.getType() == GestureType.singleTap) {
            int scrollTo = 0;
            if (Math.abs(getScrollX() - minScroll) > Math.abs(getScrollX() - maxScroll)) {
                scrollTo = minScroll;
            } else {
                scrollTo = maxScroll;
            }
            scroller.startScroll(getScrollX(), 0, scrollTo - getScrollX(), 0, HIDE_DURATION);
        }
        post(updater);
    }

    public void onGestureStart(MotionEvent event) {
        scroller.abortAnimation();
    }

    public void onFingerStart(int index, MotionEvent event) {/*pass*/}

    public void onFingerEnd(int index, MotionEvent event) {/*pass*/}

    public void onGestureEnd(MotionEvent event) {
        Log.w(getClass().getSimpleName(), "onGestureEnd");
    }
    
    private class ScrollUpdater implements Runnable {
        public void run() {
            if (scroller.computeScrollOffset()){
                scrollTo(scroller.getCurrX(), 0);
                post(this);
            }
        }
    }
}
