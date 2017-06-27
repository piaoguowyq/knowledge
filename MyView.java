package com.education.mypopuptest;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.os.Build;
import android.support.v4.view.GestureDetectorCompat;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by zhonghang on 16/7/29.
 */

public class MyView extends View {
    private GestureDetectorCompat gestureDetector;
    private GestureDetector.SimpleOnGestureListener listener;
    private float zoom = 1f;
    private float oldZoom = 1f;
    private float width;
    private float height;
    private Paint paint;
    private float offsetX;
    private float offsetY;
    private int mode = 0;
    public static final int ACTION_DRAG = 0;
    public static final int ACTION_NONE = -1;
    public static final int ACTION_ZOOM = 1;
    private float oldDistance;
    private float newDistance;
    private Bitmap bitmap;

    public MyView(Context context) {
        super(context);
        init(context);
    }

    public MyView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public MyView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public MyView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        width = getMeasuredWidth();
        height = getMeasuredHeight();
    }

    private void init(Context context) {

        listener = new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                //单次抬起
                return super.onSingleTapUp(e);
            }

            @Override
            public void onLongPress(MotionEvent e) {
                //长按事件
                super.onLongPress(e);
            }

            @Override
            public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
                //滚动事件
                offsetX -= distanceX;
                offsetY -= distanceY;
                invalidate();
                return true;
            }

            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                //滑动事件
                return super.onFling(e1, e2, velocityX, velocityY);
            }

            @Override
            public void onShowPress(MotionEvent e) {
                //按下事件
                super.onShowPress(e);
            }

            @Override
            public boolean onDown(MotionEvent e) {
                //按下事件
                return super.onDown(e);
            }

            @Override
            public boolean onDoubleTap(MotionEvent e) {
                //双击事件
                return super.onDoubleTap(e);
            }

            @Override
            public boolean onDoubleTapEvent(MotionEvent e) {
                return super.onDoubleTapEvent(e);
            }

            @Override
            public boolean onSingleTapConfirmed(MotionEvent e) {
                //单次确认
                return super.onSingleTapConfirmed(e);
            }

            @Override
            public boolean onContextClick(MotionEvent e) {
                //点击事件
                return super.onContextClick(e);
            }
        };
        gestureDetector = new GestureDetectorCompat(context, listener);
        bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
        paint = new Paint();
        paint.setColor(Color.BLACK);
        paint.setStrokeWidth(30);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                mode = ACTION_DRAG;
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                mode = ACTION_ZOOM;
                oldDistance = distance(event);
                break;
            case MotionEvent.ACTION_MOVE:
                if (mode == ACTION_ZOOM) {
                    newDistance = distance(event);
                    if (newDistance > 10f) {
                        zoom = newDistance / oldDistance * oldZoom;
                        invalidate();
                    }
                }
                break;
            case MotionEvent.ACTION_POINTER_UP:
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                mode = ACTION_NONE;
                oldZoom = zoom;
                break;
        }
        if (mode == ACTION_DRAG) {
            gestureDetector.onTouchEvent(event);
        }
        return true;
    }
    @Override
    protected void onDraw(Canvas canvas) {
        canvas.save();
        canvas.scale(zoom, zoom, width / 2/zoom, height / 2/zoom);
        canvas.drawBitmap(bitmap, 0 + offsetX / zoom, 0 + offsetY / zoom, paint);
//        canvas.drawLine(0 + offsetX, 0 + offsetY, 300 + offsetX, 300 + offsetY, paint);
        canvas.restore();
    }
    // 计算两个触摸点之间的距离
    private float distance(MotionEvent event) {
        float x = event.getX(0) - event.getX(1);
        float y = event.getY(0) - event.getY(1);
        return (float) Math.sqrt(x * x + y * y);
    }
    // 计算两个触摸点的中点
    // this is a test for cemtos pull.
    private PointF middle(MotionEvent event) {
        float x = event.getX(0) + event.getX(1);
        float y = event.getY(0) + event.getY(1);
        return new PointF(x / 2, y / 2);
    }
}
