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

package name.wildswift.android.guitool.gesture.recognizers.simple;

import android.view.MotionEvent;

/**
 * 19.02.12
 *
 * @author Swift
 */
public class ScrollSimpleGesture extends SimpleGesture{

    private MotionEvent e1;
    private MotionEvent e2;
    private float distanceX;
    private float distanceY;

    public ScrollSimpleGesture(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        super(SCROLL);
        this.distanceX = distanceX;
        this.distanceY = distanceY;
        this.e1 = e1;
        this.e2 = e2;
    }

    public float getDistanceX() {
        return distanceX;
    }

    public float getDistanceY() {
        return distanceY;
    }

    public MotionEvent getE1() {
        return e1;
    }

    public MotionEvent getE2() {
        return e2;
    }
}
