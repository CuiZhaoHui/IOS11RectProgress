package com.cui.rectprogress;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
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

    private Resources mResources;
    /*画背景使用的Rect*/
    private RectF bgRect = new RectF();
    /*画进度使用的Rect*/
    private RectF progressRect = new RectF();
    private Paint bgPaint;
    private Paint progressPaint;

    private int orientation = VERTICAL;
    private int max = 100;
    private int progress = 15;
    private int imgSrc = 0;
    private Bitmap bitmap;
    private Rect srcRect;
    private Rect dstRect;
    private Matrix bitmapMatrix;
    private float iconPadding;


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
            imgSrc = typedArray.getResourceId(R.styleable.RectProgress_iconSrc, 0);
            iconPadding = typedArray.getDimensionPixelSize(R.styleable.RectProgress_iconPadding,10);
            if (max < progress) {
                progress = max;
            }
            typedArray.recycle();

            if (imgSrc != 0) {
                bitmap = ((BitmapDrawable) getResources().getDrawable(imgSrc)).getBitmap();
            }
        }

        bgPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        bgPaint.setColor(bgColor);

        progressPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        progressPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_ATOP));
        progressPaint.setColor(progressColor);


    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        bgRect.set(iconPadding
                , iconPadding
                , getWidth() - iconPadding
                , getHeight() - iconPadding);

        progressRect.set(iconPadding
                , bgRect.bottom - progress * bgRect.height() / max
                , getWidth() - iconPadding
                , getHeight() - iconPadding);

        if (bitmap != null){
            srcRect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
            int iconSideLength = (int) (bgRect.width() - iconPadding * 2);
            dstRect = new Rect((int)bgRect.left + (int)iconPadding
                    , (int)(bgRect.bottom - iconSideLength - iconPadding)
                    , (int)bgRect.right - (int)iconPadding
                    , (int)bgRect.bottom - (int)iconPadding);
        }


        bitmapMatrix = new Matrix();
        bitmapMatrix.setScale(0.5f,0.5f);
    }

    @Override
    protected void onDraw(Canvas canvas) {
//        if (bgRect.isEmpty()) {
//
//        }

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


            if (bitmap != null && srcRect != null && dstRect != null && bgPaint != null) {
            canvas.drawBitmap(bitmap, srcRect, dstRect, bgPaint);
//                canvas.drawBitmap(bitmap, bitmapMatrix,bgPaint);
            }

        }
        canvas.restoreToCount(layerId);
        // TODO: 2017/6/19  弄明白为什么在xml预览中,canvas.restoreToCount
        // TODO: 会导致后续的canvas对象为空 但canvas.restore方法则不会导致这个问题
//        canvas.restore();
//        canvas.save();

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
