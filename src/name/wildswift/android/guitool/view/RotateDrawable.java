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

package name.wildswift.android.guitool.view;

import android.graphics.drawable.Drawable;

import android.content.res.Resources;
import android.graphics.*;

/**
 * Class for rotate drawable around y axis
 */
public class RotateDrawable extends Drawable implements Drawable.Callback {
    private RotationState rotationState;
    private boolean mutated;
    public int minLevel = 0;
    public int maxLevel = 500;
    private int level;

    RotateDrawable() {
        this(null, null);
    }

    public RotateDrawable(Drawable drawable) {
        this(null, null);

        rotationState.drawable = drawable;

        if (drawable != null) {
            drawable.setCallback(this);
        }
    }

    public void setMaxLevel(int maxLevel) {
        this.maxLevel = maxLevel;
    }

    public void setMinLevel(int minLevel) {
        this.minLevel = minLevel;
    }

    /**
     * Returns the original drawable
     * @return original drawable
     */
    public Drawable getDrawable() {
        return rotationState.drawable;
    }

    // overrides from Drawable.Callback

    public void invalidateDrawable(Drawable who) {
        if (getCallback() != null) {
            getCallback().invalidateDrawable(this);
        }
    }

    public void scheduleDrawable(Drawable who, Runnable what, long when) {
        if (getCallback() != null) {
            getCallback().scheduleDrawable(this, what, when);
        }
    }

    public void unscheduleDrawable(Drawable who, Runnable what) {
        if (getCallback() != null) {
            getCallback().unscheduleDrawable(this, what);
        }
    }

    // overrides from Drawable

    @Override
    public void draw(Canvas canvas) {
        Matrix canvasMatrix = canvas.getMatrix();
        Matrix invertedCanvasMatrix = new Matrix();
        canvasMatrix.invert(invertedCanvasMatrix);
        Camera camera = new Camera();
        camera.translate(rotationState.drawable.getBounds().width() / 2.0f, 0, 0);
        camera.rotateY(((((float) level) - minLevel) / (maxLevel - minLevel)) * 180);
        camera.translate(-rotationState.drawable.getBounds().width() / 2.0f, 0, 0);
        Matrix matrix = new Matrix();
        camera.getMatrix(matrix);
        matrix.postConcat(canvasMatrix);
        canvas.setMatrix(matrix);

        rotationState.drawable.draw(canvas);
        canvas.setMatrix(canvasMatrix);
    }

    @Override
    public int getChangingConfigurations() {
        return super.getChangingConfigurations()
                | rotationState.changingConfigurations
                | rotationState.drawable.getChangingConfigurations();
    }

    @Override
    public boolean getPadding(Rect padding) {
        return rotationState.drawable.getPadding(padding);
    }

    @Override
    public boolean setVisible(boolean visible, boolean restart) {
        rotationState.drawable.setVisible(visible, restart);
        return super.setVisible(visible, restart);
    }

    @Override
    public void setAlpha(int alpha) {
        rotationState.drawable.setAlpha(alpha);
    }

    @Override
    public void setColorFilter(ColorFilter cf) {
        rotationState.drawable.setColorFilter(cf);
    }

    @Override
    public int getOpacity() {
        return rotationState.drawable.getOpacity();
    }

    @Override
    public boolean isStateful() {
        return rotationState.drawable.isStateful();
    }

    @Override
    protected boolean onStateChange(int[] state) {
        return rotationState.drawable.setState(state);
    }

    @Override
    protected boolean onLevelChange(int level) {
        rotationState.drawable.setLevel(level);
        this.level = Math.min(Math.max(minLevel, level), maxLevel);
        invalidateSelf();
        return true;
    }

    @Override
    protected void onBoundsChange(Rect bounds) {
        rotationState.drawable.setBounds(bounds.left, bounds.top, bounds.right, bounds.bottom);
    }

    @Override
    public int getIntrinsicWidth() {
        return rotationState.drawable.getIntrinsicWidth();
    }

    @Override
    public int getIntrinsicHeight() {
        return rotationState.drawable.getIntrinsicHeight();
    }

    @Override
    public ConstantState getConstantState() {
        if (rotationState.canConstantState()) {
            rotationState.changingConfigurations = getChangingConfigurations();
            return rotationState;
        }
        return null;
    }

    @Override
    public Drawable mutate() {
        if (!mutated && super.mutate() == this) {
            rotationState.drawable.mutate();
            mutated = true;
        }
        return this;
    }

    final static class RotationState extends ConstantState {
        Drawable drawable;
        int changingConfigurations;
        boolean useIntrinsicSizeAsMin;

        private boolean checkedConstantState;
        private boolean canConstantState;

        RotationState(RotationState orig, RotateDrawable owner, Resources res) {
            if (orig != null) {
                if (res != null) {
                    drawable = orig.drawable.getConstantState().newDrawable(res);
                } else {
                    drawable = orig.drawable.getConstantState().newDrawable();
                }
                drawable.setCallback(owner);
                useIntrinsicSizeAsMin = orig.useIntrinsicSizeAsMin;
                checkedConstantState = canConstantState = true;
            }
        }

        @Override
        public Drawable newDrawable() {
            return new RotateDrawable(this, null);
        }

        @Override
        public Drawable newDrawable(Resources res) {
            return new RotateDrawable(this, res);
        }

        @Override
        public int getChangingConfigurations() {
            return changingConfigurations;
        }

        boolean canConstantState() {
            if (!checkedConstantState) {
                canConstantState = drawable.getConstantState() != null;
                checkedConstantState = true;
            }

            return canConstantState;
        }
    }

    private RotateDrawable(RotationState state, Resources res) {
        rotationState = new RotationState(state, this, res);
    }
}

