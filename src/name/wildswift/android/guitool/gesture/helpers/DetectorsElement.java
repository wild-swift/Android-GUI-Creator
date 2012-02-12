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

import android.content.Context;
import android.view.GestureDetector;
import android.view.MotionEvent;

/**
 * Because Android GestureDetector stops handling any events after onLongPress,
 * I create 2 detectors.
 *
 * 12.02.12
 *
 * @author Swift
 */
public class DetectorsElement {
    private GestureDetector detector;
    private GestureDetector longPressDetector;

    public <T extends android.view.GestureDetector.OnGestureListener & android.view.GestureDetector.OnDoubleTapListener> DetectorsElement(Context context, T listener) {
        this.detector = new GestureDetector(context, listener);
        this.detector.setIsLongpressEnabled(false);
        this.detector.setOnDoubleTapListener(listener);
        this.longPressDetector = new GestureDetector(context, new LongPressWrapper(listener));
        this.detector.setIsLongpressEnabled(true);
    }

    public boolean onTouchEvent(MotionEvent ev) {
        boolean detectorResult = detector.onTouchEvent(ev);
        boolean longPressDetectorResult = longPressDetector.onTouchEvent(ev);
        return detectorResult || longPressDetectorResult;
    }
}
