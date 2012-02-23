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

package name.wildswift.android.guitool;

import android.app.Activity;
import android.content.ClipData;
import android.os.Bundle;
import android.util.Log;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;
import name.wildswift.android.guitool.view.RotateDrawable;

/**
 * 22.01.12
 *
 * @author Swift
 */
public class MainBuilderScreen extends Activity implements View.OnDragListener, View.OnTouchListener {

    private RotateDrawable d;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_screen);
        findViewById(R.id.control).setOnDragListener(this);
        findViewById(R.id.control).setOnTouchListener(this);
        d = new RotateDrawable(getResources().getDrawable(R.drawable.menu_open_icon));
        findViewById(R.id.control).setBackgroundDrawable(d);
    }



    public boolean onDrag(View view, DragEvent dragEvent) {
        Log.d(getClass().getSimpleName(), "dragEvent = " + dragEvent.toString());
        return false;
    }

    public boolean onTouch(View view, MotionEvent motionEvent) {
        if (motionEvent.getAction() == MotionEvent.ACTION_DOWN){
            view.startDrag(new ClipData("test", new String[]{"text/plain"}, new ClipData.Item("test")), new View.DragShadowBuilder(view), null, 0);
        }
        return false;  
    }
}
