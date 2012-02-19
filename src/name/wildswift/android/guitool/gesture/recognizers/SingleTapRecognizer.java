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

package name.wildswift.android.guitool.gesture.recognizers;

import android.util.Log;
import android.view.MotionEvent;
import name.wildswift.android.guitool.gesture.OnGestureListener;
import name.wildswift.android.guitool.gesture.gestures.MotionPoint;
import name.wildswift.android.guitool.gesture.gestures.SingleTap;
import name.wildswift.android.guitool.gesture.recognizers.simple.DownSimpleGesture;
import name.wildswift.android.guitool.gesture.recognizers.simple.SimpleGesture;
import name.wildswift.android.guitool.gesture.recognizers.simple.SingleTapSimpleGesture;

import java.util.ArrayList;
import java.util.List;

/**
 * 19.02.12
 *
 * @author Swift
 */
public class SingleTapRecognizer extends GestureRecognizer{
    private OnGestureListener listener;
    private static final long TIME_DELTA = 50;

    public SingleTapRecognizer(OnGestureListener listener) {
        this.listener = listener;
    }

    private long timeStart = -1;
    private long timeEnd = -1;
    private int fingersCount = 0;
    private List<MotionPoint> points = null;

    @SuppressWarnings("ConstantConditions")
    @Override
    public boolean onNewEvent(SimpleGesture[] gestures) {
        for (int i = 0; i < gestures.length; i++) {
            SimpleGesture gesture = gestures[i];
            if (gesture == null) continue;
            Log.d(getClass().getSimpleName(), "gesture " + gesture.getType());
            if (gesture.getType() != SimpleGesture.DOWN && gesture.getType() != SimpleGesture.SINGLE_TAP) return false;

            if (gesture.getType() == SimpleGesture.DOWN) {
                Log.d(getClass().getSimpleName(), "gestureTime = " + ((DownSimpleGesture) gesture).getEvent().getEventTime());
                if (i == 0) {
                    timeStart = ((DownSimpleGesture) gesture).getEvent().getEventTime();
                    fingersCount = 1;
                }

                if (i > 0) {
                    if (Math.abs(timeStart - ((DownSimpleGesture) gesture).getEvent().getEventTime()) >= TIME_DELTA) {
                        timeStart = -1;
                        timeEnd = -1;
                        fingersCount = 0;
                        return false;
                    } else {
                        fingersCount++;
                    }
                }
            }
            if (gesture.getType() == SimpleGesture.SINGLE_TAP) {
                Log.d(getClass().getSimpleName(), "gestureTime = " + ((SingleTapSimpleGesture) gesture).getEvent().getEventTime());
                if (timeEnd < 0) {
                    MotionEvent event = ((SingleTapSimpleGesture) gesture).getEvent();
                    timeEnd = event.getEventTime();
                    points = new ArrayList<MotionPoint>(fingersCount);
                    points.add(new MotionPoint(event.getX(), event.getY()));
                    fingersCount--;
                } else if (Math.abs(((SingleTapSimpleGesture) gesture).getEvent().getEventTime() - timeEnd) < TIME_DELTA) {
                    MotionEvent event = ((SingleTapSimpleGesture) gesture).getEvent();
                    points.add(new MotionPoint(event.getX(), event.getY()));
                    fingersCount--;
                    if (fingersCount == 0) {
                        listener.onGesture(SingleTap.obtain(points.toArray(new MotionPoint[points.size()])));
                        timeStart = -1;
                        timeEnd = -1;
                        fingersCount = 0;
                    }
                } else {
                    timeStart = -1;
                    timeEnd = -1;
                    fingersCount = 0;
                    return false;
                }
            }
        }
        return true;
    }
}
