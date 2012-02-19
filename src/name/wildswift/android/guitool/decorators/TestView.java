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

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import name.wildswift.android.guitool.gesture.CompositeGestureDetector;
import name.wildswift.android.guitool.gesture.OnGestureListener;
import name.wildswift.android.guitool.gesture.gestures.Gesture;
import name.wildswift.android.guitool.gesture.gestures.GestureType;

/**
 * 11.02.12
 *
 * @author Swift
 */
public class TestView extends View implements OnGestureListener {

    protected CompositeGestureDetector detector;

    public TestView(Context context) {
        super(context);
        detector = new CompositeGestureDetector(context, (byte) 3, this);
    }

    public TestView(Context context, AttributeSet attrs) {
        super(context, attrs);
        detector = new CompositeGestureDetector(context, (byte) 3, this);
    }

    public TestView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        detector = new CompositeGestureDetector(context, (byte) 3, this);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return detector.onMotionEvent(event);
    }

    public void onGesture(Gesture gesture) {
        GestureType type = gesture.getType();
        Log.i(getClass().getSimpleName(), type.toString());
    }

    public void onGestureStart(MotionEvent event) {
    }

    public void onFingerStart(int index, MotionEvent event) {
    }

    public void onFingerEnd(int index, MotionEvent event) {
    }

    public void onGestureEnd(MotionEvent event) {
    }
}
