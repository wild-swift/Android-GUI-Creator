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

package name.wildswift.android.guitool.gesture.helpers;

import android.view.GestureDetector;
import android.view.MotionEvent;

/**
 * Wrapper Class for remove implementation GestureDetector.OnDoubleTapListener in gesture detector
 *
 * 12.02.12
 *
 * @author Swift
 */
public class OnGestureListenerWrapper implements GestureDetector.OnGestureListener{
    private GestureDetector.OnGestureListener listener;

    public OnGestureListenerWrapper(GestureDetector.OnGestureListener listener) {
        this.listener = listener;
    }

    public boolean onDown(MotionEvent event) {
        return listener.onDown(event);
    }

    public boolean onFling(MotionEvent event, MotionEvent event1, float v, float v1) {
        return listener.onFling(event, event1, v, v1);
    }

    public void onLongPress(MotionEvent event) {
        listener.onLongPress(event);
    }

    public boolean onScroll(MotionEvent event, MotionEvent event1, float v, float v1) {
        return listener.onScroll(event, event1, v, v1);
    }

    public void onShowPress(MotionEvent event) {
        listener.onShowPress(event);
    }

    public boolean onSingleTapUp(MotionEvent event) {
        return listener.onSingleTapUp(event);
    }
}
