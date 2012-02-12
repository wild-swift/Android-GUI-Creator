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
 * Wrapper Class for remove Handling all events excepting onLongPress
 * Because Android GestureDetector stops handling any events after onLongPress,
 * I create 2 detectors and need events to be generating single time
 *
 * 12.02.12
 *
 * @author Swift
 */
public class LongPressWrapper implements GestureDetector.OnGestureListener{
    private GestureDetector.OnGestureListener listener;

    public LongPressWrapper(GestureDetector.OnGestureListener listener) {
        this.listener = listener;
    }

    public boolean onDown(MotionEvent motionEvent) {
        return true;
    }

    public void onShowPress(MotionEvent motionEvent) {
    }

    public boolean onSingleTapUp(MotionEvent motionEvent) {
        return true;
    }

    public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
        return true;
    }

    public void onLongPress(MotionEvent motionEvent) {
        listener.onLongPress(motionEvent);
    }

    public boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
        return true;
    }
}
