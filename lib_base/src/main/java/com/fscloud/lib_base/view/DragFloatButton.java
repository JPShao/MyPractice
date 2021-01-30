package com.fscloud.lib_base.view;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;

public class DragFloatButton extends androidx.appcompat.widget.AppCompatImageView {
    private int parentHeight;
    private int parentWidth;

    private int lastX;
    private int lastY;

    private boolean isDrag;

    public DragFloatButton(Context context) {
        super(context);
    }

    public DragFloatButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public DragFloatButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public boolean onTouchEvent(MotionEvent event) {
        int rawX = (int) event.getRawX();
        int rawY = (int) event.getRawY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                this.getParent().requestDisallowInterceptTouchEvent(true);
                this.setPressed(true);
                this.isDrag = false;
                this.lastX = rawX;
                this.lastY = rawY;
                if (this.getParent() != null) {
                    ViewGroup parent = (ViewGroup) this.getParent();
                    this.parentHeight = parent.getHeight();
                    this.parentWidth = parent.getWidth();
                }
                break;
            case MotionEvent.ACTION_UP:
                if (!this.isNotDrag()) {
                    this.setPressed(false);
                    this.moveHide(rawX);
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if ((double) this.parentHeight > 0.2D && (double) this.parentWidth > 0.2D) {
                    this.isDrag = true;
                    int dx = rawX - this.lastX;
                    int dy = rawY - this.lastY;
                    int distance = (int) Math.sqrt(dx * dx + dy * dy);
                    if (distance == 0) {
                        this.isDrag = false;
                    } else {
                        float x = this.getX() + (float) dx;
                        float y = this.getY() + (float) dy;
                        x = x < 0.0F ? 0.0F : (Math.min(x, (float) (this.parentWidth - this.getWidth())));
                        y = this.getY() < 0.0F ? 0.0F : (this.getY() + (float) this.getHeight() > (float) this.parentHeight ? (float) (this.parentHeight - this.getHeight()) : y);
                        this.setX(x);
                        this.setY(y);
                        this.lastX = rawX;
                        this.lastY = rawY;
                    }
                } else {
                    this.isDrag = false;
                }
        }

        return !this.isNotDrag() || super.onTouchEvent(event);
    }

    private boolean isNotDrag() {
        return !this.isDrag && (this.getX() == 0.0F || this.getX() == (float) (this.parentWidth - this.getWidth()));
    }

    private void moveHide(int rawX) {
        if (rawX >= this.parentWidth / 2) {
            this.animate().setInterpolator(new DecelerateInterpolator()).setDuration(500L).xBy((float) (this.parentWidth - this.getWidth()) - this.getX()).start();
        } else {
            ObjectAnimator oa = ObjectAnimator.ofFloat(this, "x", this.getX(), 0.0F);
            oa.setInterpolator(new DecelerateInterpolator());
            oa.setDuration(500L);
            oa.start();
        }
    }
}