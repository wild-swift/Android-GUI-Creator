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

package name.wildswift.android.guitool.decorators;

import android.gesture.GestureOverlayView;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

/**
 * 05.02.12
 *
 * @author Swift
 */
public class ViewDecorator extends ViewGroup {

    private View view;
    private GestureOverlayView overlay;

    public ViewDecorator(View view) {
        super(view.getContext());
        this.view = view;
        overlay = new GestureOverlayView(getContext());
        super.addView(view);
        super.addView(overlay);
    }

    @Override
    protected void onLayout(boolean changed, int l, int r, int t, int b) {
        view.layout(l, r, t, b);
        overlay.measure(MeasureSpec.makeMeasureSpec(r-l, MeasureSpec.EXACTLY), MeasureSpec.makeMeasureSpec(b-t, MeasureSpec.EXACTLY));
        overlay.layout(l, r, t, b);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        view.measure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(view.getMeasuredWidth(), view.getMeasuredHeight());
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return super.onTouchEvent(event);
    }
}
