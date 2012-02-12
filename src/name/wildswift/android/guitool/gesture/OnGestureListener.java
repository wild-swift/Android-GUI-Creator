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

package name.wildswift.android.guitool.gesture;

import android.view.MotionEvent;
import name.wildswift.android.guitool.gesture.gestures.Gesture;

/**
 * 11.02.12
 *
 * @author Swift
 */
public interface OnGestureListener {
    public void onGesture(Gesture gesture);
    public void onGestureStart(MotionEvent event);
    public void onFingerStart(int index, MotionEvent event);
    public void onFingerEnd(int index, MotionEvent event);
    public void onGestureEnd(MotionEvent event);
}
