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

import android.view.MotionEvent;
import name.wildswift.android.guitool.gesture.OnGestureListener;
import name.wildswift.android.guitool.gesture.gestures.DoubleTap;
import name.wildswift.android.guitool.gesture.gestures.MotionPoint;
import name.wildswift.android.guitool.gesture.gestures.SingleTap;
import name.wildswift.android.guitool.gesture.recognizers.simple.DoubleTapSimpleGesture;
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
public class DoubleTapRecognizer extends GestureRecognizer{
    private OnGestureListener listener;
    private static final long TIME_DELTA = 50;

    public DoubleTapRecognizer(OnGestureListener listener) {
        this.listener = listener;
    }

    private long timeStart = -1;
    private int downNum = 0;
    private long timeEnd = -1;
    private int fingersCount = 0;
    private List<MotionPoint> points = new ArrayList<MotionPoint>(10);

    @SuppressWarnings("ConstantConditions")
    @Override
    public boolean onNewEvent(SimpleGesture[] gestures) {
        for (int i = 0; i < gestures.length; i++) {
            SimpleGesture gesture = gestures[i];
            if (gesture == null) continue;
            if (gesture.getType() != SimpleGesture.DOWN && gesture.getType() != SimpleGesture.SINGLE_TAP && gesture.getType() != SimpleGesture.DOUBLE_TAP) return false;

            if (gesture.getType() == SimpleGesture.DOWN) {
                if (i == 0) {
                    timeStart = ((DownSimpleGesture) gesture).getEvent().getEventTime();
                    fingersCount = 1;
                }

                if (i > 0) {
                    if (Math.abs(timeStart - ((DownSimpleGesture) gesture).getEvent().getEventTime()) >= TIME_DELTA) {
                        if (downNum == 0) {
                            downNum = 1;
                            timeStart = ((DownSimpleGesture) gesture).getEvent().getEventTime();
                        } else {
                            resetRecognizer();
                            return false;
                        }
                    } else {
                        fingersCount++;
                    }
                }
            }
            if (gesture.getType() == SimpleGesture.DOUBLE_TAP) {
                if (timeEnd < 0) {
                    MotionEvent event = ((DoubleTapSimpleGesture) gesture).getEvent();
                    timeEnd = event.getEventTime();
                    points.add(new MotionPoint(event.getX(), event.getY()));
                    fingersCount--;
                } else if (Math.abs(((DoubleTapSimpleGesture) gesture).getEvent().getEventTime() - timeEnd) < TIME_DELTA) {
                    MotionEvent event = ((DoubleTapSimpleGesture) gesture).getEvent();
                    points.add(new MotionPoint(event.getX(), event.getY()));
                    fingersCount--;
                } else {
                    resetRecognizer();
                    return false;
                }
                if (fingersCount == 0) {
                    listener.onGesture(DoubleTap.obtain(points.toArray(new MotionPoint[points.size()])));
                    resetRecognizer();
                }
            }
        }
        return true;
    }

    private void resetRecognizer() {
        timeStart = -1;
        timeEnd = -1;
        fingersCount = 0;
        downNum = 0;
        points.clear();
    }
}
