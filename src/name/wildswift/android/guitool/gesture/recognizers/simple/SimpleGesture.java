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

/**
 * 19.02.12
 *
 * @author Swift
 */
public abstract class SimpleGesture {
    public static final int DOWN = 0;
    public static final int SINGLE_TAP = 1;
    public static final int SCROLL = 2;
    public static final int LONG_PRESS = 3;
    public static final int FLING = 4;
    public static final int DOUBLE_TAP = 5;

    private int type;

    protected SimpleGesture(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }
}
