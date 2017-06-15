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
import android.view.View;

/**
 * Created by CZH on 2017/6/15.
 * RectProgress
 */

public class RectProgress extends View {

    private RectF contentRect = new RectF();

    private Paint bgPaint;
    private Paint progressPaint;

    private final int defaultBgColor = 0xff000000;
    private final int defaultProgressColor = 0xffffffff;

    private int bgColor = defaultBgColor;
    private int progressColor = defaultProgressColor;

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
            typedArray.recycle();
        }

        bgPaint = new Paint();
        bgPaint.setColor(bgColor);

        progressPaint = new Paint();
        progressPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_OVER));
        progressPaint.setColor(progressColor);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (contentRect.isEmpty())
            contentRect.set(10, 10, getWidth() - 10, getHeight() - 10);

        canvas.drawRect(0, 0, getWidth(), getHeight(), bgPaint);

        canvas.drawRoundRect(contentRect, 20, 20, progressPaint);
    }
}
