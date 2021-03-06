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

package name.wildswift.android.guitool.gesture.gestures;

/**
 * 12.02.12
 *
 * @author Swift
 */
public class DoubleTap extends Gesture {

    private MotionPoint[] points;

    private DoubleTap(MotionPoint[] points) {
        this.points = points;
    }

    public static DoubleTap obtain(MotionPoint[] events) {
        return new DoubleTap(events);
    }

    public long getFingersCount() {
        return points.length;
    }

    @Override
    public GestureType getType() {
        return GestureType.doubleTap;
    }

    public MotionPoint getPoint(int finger) {
        return points[finger];
    }
}
