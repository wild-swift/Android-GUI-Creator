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

import android.content.Context;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import name.wildswift.android.guitool.gesture.helpers.DetectorsElement;
import name.wildswift.android.guitool.gesture.helpers.FingerState;
import name.wildswift.android.guitool.gesture.recognizers.GestureRecognizer;
import name.wildswift.android.guitool.gesture.recognizers.SingleTapRecognizer;
import name.wildswift.android.guitool.gesture.recognizers.simple.SimpleGesture;
import name.wildswift.android.guitool.gesture.recognizers.simple.*;

import java.util.Arrays;
import java.util.LinkedList;


/**
 * 11.02.12
 *
 * @author Swift
 */
public class CompositeGestureDetector {

    private OnGestureListener listener;

    private DetectorsElement[] detectors;
    private FingerState[] states;
    private SimpleGesture[] gestures;

    private GestureRecognizer[] recognizers;

    private LinkedList<GestureRecognizer> activeRecognizers;

    private byte maxFingers;

    public CompositeGestureDetector(Context context, byte maxFingers, OnGestureListener listener) {
        if (maxFingers <= 0) throw new IllegalArgumentException("maxFingers must be more then 0");

        this.maxFingers = maxFingers;
        this.listener = listener;

        detectors = new DetectorsElement[maxFingers];
        for (byte i = 0; i < maxFingers; i++) {
            detectors[i] = new DetectorsElement(context, new InternalGestureDetectorListener(i));
        }

        states = new FingerState[maxFingers];
        for (byte i = 0; i < maxFingers; i++) {
            states[i] = new FingerState();
        }

        gestures = new SimpleGesture[maxFingers];
        for (byte i = 0; i < maxFingers; i++) {
            gestures[i] = null;
        }

        recognizers = new GestureRecognizer[]{
                new SingleTapRecognizer(listener)
        };
    }

    public boolean onMotionEvent(MotionEvent event) {
        int actionIndexExternal;
        int actionIndex;
        switch (event.getActionMasked()){
            case MotionEvent.ACTION_DOWN:
                // if (event.getActionMasked() == MotionEvent.ACTION_DOWN)
                listener.onGestureStart(event);
                activeRecognizers = new LinkedList<GestureRecognizer>(Arrays.asList(recognizers));
            case MotionEvent.ACTION_POINTER_DOWN:
                // find empty slot for new finger, mark it as busy
                // and increment indexes of all fingers that more or equals than new 
                actionIndexExternal = event.getActionIndex();
                boolean foundEmptySlot = false;
                actionIndex = -1;
                for (int i = 0; i < maxFingers; i++){
                    if (states[i].isDown() && states[i].getFingerNum() >= actionIndexExternal) {
                        states[i].setFingerNum(states[i].getFingerNum() + 1);
                    }
                    if (!states[i].isDown() && !foundEmptySlot) {
                        foundEmptySlot = true;
                        states[i].setDown(true);
                        states[i].setDownTime(event.getEventTime());
                        states[i].setFingerNum(actionIndexExternal);
                        actionIndex = i;
                    }
                }
                if (actionIndex == -1) return false;

                // send event to associated GestureDetector group
                detectors[actionIndex].onTouchEvent(MotionEvent.obtain(event.getEventTime(), event.getEventTime(), MotionEvent.ACTION_DOWN,
                        event.getX(states[actionIndex].getFingerNum()), event.getY(states[actionIndex].getFingerNum()), event.getMetaState()));

                // notify listener about new finger in gesture
                listener.onFingerStart(actionIndex, event);
                break;


            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_POINTER_UP:
                // check index of finger
                actionIndexExternal = event.getActionIndex();
                // up event generated in 0 - event.getPointerCount() interval
                // you may receive all up events with 0 actionIndex
                actionIndex = -1;
                for (int i = 0; i < maxFingers; i++) {
                    if (states[i].isDown() && states[i].getFingerNum() == actionIndexExternal) {
                        actionIndex = i;
                        states[i].setDown(false);
                    }
                    if (states[i].isDown() && states[i].getFingerNum() >= actionIndexExternal) {
                        states[i].setFingerNum(states[i].getFingerNum() - 1);
                    }
                }
                // if action index not changed action not for our detector
                if (actionIndex == -1) return false;


                // send event to associated GestureDetector group
                detectors[actionIndex].onTouchEvent(MotionEvent.obtain(states[actionIndex].getDownTime(), event.getEventTime(), MotionEvent.ACTION_UP,
                        event.getX(states[actionIndex].getFingerNum()), event.getY(states[actionIndex].getFingerNum()), event.getMetaState()));

                // notify listener about finger is up
                listener.onFingerEnd(actionIndex, event);
                // if we receive action UP that means that all
                if (event.getActionMasked() == MotionEvent.ACTION_UP) {
                    listener.onGestureEnd(event);
                }
                break;

            case MotionEvent.ACTION_MOVE:
                for(int i = 0; i < maxFingers; i++) {
                    FingerState state = states[i];
                    if (!state.isDown()) continue;
                    detectors[i].onTouchEvent(MotionEvent.obtain(states[i].getDownTime(), event.getEventTime(), MotionEvent.ACTION_MOVE,
                            event.getX(states[i].getFingerNum()), event.getY(states[i].getFingerNum()), event.getMetaState()));
                }
                break;
        }

        notifyRecognizers();

        return true;
    }


    private void notifyRecognizers() {
        LinkedList<GestureRecognizer> recognizersForDelete = new LinkedList<GestureRecognizer>();
        for (GestureRecognizer recognizer : activeRecognizers) {
            if (!recognizer.onNewEvent(gestures)) {
                recognizersForDelete.add(recognizer);
            }
        }
        activeRecognizers.removeAll(recognizersForDelete);
        for (int i = 0; i < gestures.length; i++) {
            gestures[i] = null;
        }
    }


    private class InternalGestureDetectorListener implements GestureDetector.OnGestureListener, GestureDetector.OnDoubleTapListener  {
        private int index;

        private InternalGestureDetectorListener(int index) {
            this.index = index;
        }

        public boolean onDown(MotionEvent motionEvent) {
            gestures[index] = new DownSimpleGesture(motionEvent);
            Log.d(getClass().getSimpleName(), "onDown " + index);
            return true;
        }

        public void onShowPress(MotionEvent motionEvent) {
            // Pass not need
        }

        public boolean onSingleTapUp(MotionEvent motionEvent) {
            gestures[index] = new SingleTapSimpleGesture(motionEvent);
            Log.d(getClass().getSimpleName(), "onSingleTapUp " + index);
            return true;
        }

        public boolean onScroll(MotionEvent e1, MotionEvent e2, float v, float v1) {
            gestures[index] = new ScrollSimpleGesture(e1, e2, v, v1);
            Log.d(getClass().getSimpleName(), "onScroll " + index);
            return true;
        }

        public void onLongPress(MotionEvent motionEvent) {
            // because this event generates from handler after some time, it have special handling
            gestures[index] = new LongPressSimpleGesture(motionEvent);
            Log.d(getClass().getSimpleName(), "onLongPress " + index);
            notifyRecognizers();
        }

        public boolean onFling(MotionEvent e1, MotionEvent e2, float v, float v1) {
            gestures[index] = new FlingSimpleGesture(e1, e2, v, v1);
            Log.d(getClass().getSimpleName(), "onFling " + index);
            return true;
        }

        public boolean onSingleTapConfirmed(MotionEvent motionEvent) {
            // Pass not need
            return true;
        }

        public boolean onDoubleTap(MotionEvent motionEvent) {
            gestures[index] = new DoubleTapGesture(motionEvent);
            Log.d(getClass().getSimpleName(), "onDoubleTap " + index);
            return true;
        }

        public boolean onDoubleTapEvent(MotionEvent motionEvent) {
            // Pass not need
            return false;
        }
    }


}
