package com.cui.rectprogress;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by CZH on 2017/6/15.
 * RectProgress
 */

public class RectProgress extends View {

    private final int VERTICAL = 1;
    private final int HORIZONTAL = 2;

    private final int defaultBgColor = 0xFf000000;
    private final int defaultProgressColor = 0xFFffffff;

    private int bgColor = defaultBgColor;
    private int progressColor = defaultProgressColor;
    /*画背景使用的Rect*/
    private RectF bgRect = new RectF();
    /*画进度使用的Rect*/
    private RectF progressRect = new RectF();
    private Paint bgPaint;
    private Paint progressPaint;

    private int orientation = VERTICAL;
    private int max = 100;
    private int progress = 15;


    public RectProgress(Context context) {
        super(context);
        init(context, null);
    }

    public RectProgress(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public RectProgress(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        //关闭硬件加速，不然setXfermode()可能会不生效
        setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        if (attrs != null) {
            TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.RectProgress);
            bgColor = typedArray.getColor(R.styleable.RectProgress_bgColor, defaultBgColor);
            progressColor = typedArray.getColor(R.styleable.RectProgress_progressColor, defaultProgressColor);
            progress = typedArray.getInteger(R.styleable.RectProgress_progressValue, progress);
            max = typedArray.getInteger(R.styleable.RectProgress_progressMax, max);
            orientation = typedArray.getInteger(R.styleable.RectProgress_progressOrientation, VERTICAL);
            if (max < progress) {
                progress = max;
            }
            typedArray.recycle();
        }

        bgPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        bgPaint.setColor(bgColor);

        progressPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        progressPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_ATOP));
        progressPaint.setColor(progressColor);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (bgRect.isEmpty()) {
            bgRect.set(getPaddingLeft()
                    , getPaddingTop()
                    , getWidth() - getPaddingRight()
                    , getHeight() - getPaddingBottom());

            progressRect.set(getPaddingLeft()
                    , bgRect.bottom - progress * bgRect.height() / max
                    , getWidth() - getPaddingRight()
                    , getHeight() - getPaddingBottom());
        }

        int canvasWidth = canvas.getWidth();
        int canvasHeight = canvas.getHeight();
        int layerId = canvas.saveLayer(0, 0, canvasWidth, canvasHeight, null, Canvas.ALL_SAVE_FLAG);
        {
            bgPaint.setColor(bgColor);
            // draw the background of progress
            canvas.drawRoundRect(bgRect, 20, 20, bgPaint);
            // draw progress
            canvas.drawRect(progressRect, progressPaint);
            bgPaint.setXfermode(null);
        }
        canvas.restoreToCount(layerId);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (bgRect.contains(event.getX(), event.getY())) {
            if (orientation == VERTICAL) {

            } else {

            }

            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    if (orientation == VERTICAL)
                        progressRect.top = getPaddingTop() + event.getY();
                    break;
                case MotionEvent.ACTION_MOVE:
                    if (orientation == VERTICAL)
                        progressRect.top = getPaddingTop() + event.getY();
                    break;
            }
            postInvalidate();
            return true;
        }

        return super.onTouchEvent(event);
    }

    private OnProgressChangedListener changedListener;

    public void setChangedListener(OnProgressChangedListener changedListener) {
        this.changedListener = changedListener;
    }

    public interface OnProgressChangedListener {
        void onProgressChanged(int currentValue, int percent);
    }

}
