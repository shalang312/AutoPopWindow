package com.example.jsonz.autopopwindow;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

/**
 * Created by JsonZ on 2018/4/10.
 */

public class ArrowLinearLayout extends LinearLayout{

    // arrow's width
    private int mArrowWidth;
    // arrow's height
    private int mArrowHeight;
    // rectangle round corner's radius
    private int mRadius;
    // background color
    private int mBackgroundColor = Color.WHITE;
    // arrow's horizontal offset relative to RIGHT side
    private int mArrowOffset;
    // shadow color
    private int mShadowColor = Color.BLACK;
    // shadow thickness
    private int mShadowThickness;
    // arrow position  0top 1bottom 2left 3right
    private int mArrowPosition;
    //cx,cy箭头指向的view的中心点坐标
    private int cx, cy;

    public ArrowLinearLayout(Context context) {
        this(context, null);
    }

    public ArrowLinearLayout(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ArrowLinearLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    public void setCx(int cx) {
        this.cx = cx;
    }

    public void setCy(int cy) {
        this.cy = cy;
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr) {
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs,
                R.styleable.cArrowLinearLayout, defStyleAttr, 0);
        mArrowWidth = a.getDimensionPixelSize(R.styleable.cArrowLinearLayout_cArrowWidth, getResources().getDimensionPixelSize(R.dimen.cMarginListItem));
        mArrowHeight = a.getDimensionPixelSize(R.styleable.cArrowLinearLayout_cArrowHeight, getResources().getDimensionPixelSize(R.dimen.cMarginListItem));
        mRadius = a.getDimensionPixelSize(R.styleable.cArrowLinearLayout_cRadius, getResources().getDimensionPixelSize(R.dimen.cConnerRadiusMiddle));
        mBackgroundColor = a.getColor(R.styleable.cArrowLinearLayout_cBackgroundColor, getResources().getColor(R.color.cColorBlueText));
        mArrowOffset = a.getDimensionPixelSize(R.styleable.cArrowLinearLayout_cArrowOffset, 0);
        mShadowColor = a.getColor(R.styleable.cArrowLinearLayout_cShadowColor, getResources().getColor(R.color.cColorGrayBackground));
        mShadowThickness = a.getDimensionPixelSize(R.styleable.cArrowLinearLayout_cShadowThickness, getResources().getDimensionPixelSize(R.dimen.cBaseMargin));
        mArrowPosition = a.getInt(R.styleable.cArrowLinearLayout_cArrowPosition, 0);
        a.recycle();
    }
//    @Override
//    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//        int paddingTop = getPaddingTop();
//        int paddingBottom = getPaddingBottom();
//        int paddingRight = getPaddingRight();
//        int paddingLeft = getPaddingLeft();
//        switch (mArrowPosition) {
//            case 0:
//                setPadding(paddingLeft, paddingTop + mArrowHeight, paddingRight, paddingBottom);
//                break;
//            case 1:
//                setPadding(paddingLeft, paddingTop, paddingRight, paddingBottom + mArrowHeight + mShadowThickness);
//                break;
//            case 2:
//                setPadding(paddingLeft + mArrowHeight, paddingTop, paddingRight, paddingBottom);
//                break;
//            case 3:
//                setPadding(paddingLeft, paddingTop + mArrowHeight + mShadowThickness, paddingRight, paddingBottom);
//                break;
//        }
//        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
//
//    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        // disable h/w acceleration for blur mask filter
        setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(mBackgroundColor);
        paint.setStyle(Paint.Style.FILL);

        // set Xfermode for source and shadow overlap
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_OVER));


        // draw arrow
        switch (mArrowPosition) {
            case 0:
                drawTopArrow(canvas, paint);
                break;
            case 1:
                drawBottomArrow(canvas, paint);
                break;
            case 2:
                drawLeftArrow(canvas, paint);
                break;
            case 3:
                drawRightArrow(canvas, paint);
                break;
        }

        super.dispatchDraw(canvas);
    }

    private void drawTopArrow(Canvas canvas, Paint paint) {
        // draw round corner rectangle
        paint.setColor(mBackgroundColor);
        canvas.drawRoundRect(new RectF(0, mArrowHeight, getMeasuredWidth() - mShadowThickness, getMeasuredHeight() - mShadowThickness), mRadius, mRadius, paint);

        if (cx <= 0) {
            mArrowOffset = getMeasuredWidth() / 2;
        } else {
            int[] loc = new int[2];
            getLocationOnScreen(loc);
            mArrowOffset = cx - loc[0] - mArrowWidth / 2;
        }

        Path path = new Path();
        int startPoint = mArrowOffset;
        path.moveTo(startPoint, mArrowHeight);
        path.lineTo(startPoint + mArrowWidth, mArrowHeight);
        path.lineTo(startPoint + mArrowWidth / 2, 0);
        path.close();
        canvas.drawPath(path, paint);
        // draw shadow
        if (mShadowThickness > 0) {
            paint.setMaskFilter(new BlurMaskFilter(mShadowThickness, BlurMaskFilter.Blur.OUTER));
            paint.setColor(mShadowColor);
            canvas.drawRoundRect(new RectF(mShadowThickness, mArrowHeight + mShadowThickness, getMeasuredWidth() - mShadowThickness, getMeasuredHeight() - mShadowThickness), mRadius, mRadius, paint);
        }
    }

    private void drawBottomArrow(Canvas canvas, Paint paint) {
        // draw round corner rectangle
        paint.setColor(mBackgroundColor);
        canvas.drawRoundRect(new RectF(0, 0, getMeasuredWidth() - mShadowThickness, getMeasuredHeight() - mShadowThickness - mArrowHeight), mRadius, mRadius, paint);

        if (cx <= 0) {
            mArrowOffset = getMeasuredWidth() / 2;
        } else {
            int[] loc = new int[2];
            getLocationOnScreen(loc);
            mArrowOffset = cx - loc[0] - mArrowWidth / 2;
        }
        Path path = new Path();
        int startPoint = mArrowOffset;
        //箭头底边的y坐标
        int by = getMeasuredHeight() - mShadowThickness - mArrowHeight;
        path.moveTo(startPoint, by);
        path.lineTo(startPoint + mArrowWidth, by);
        path.lineTo(startPoint + mArrowWidth / 2, by + mArrowHeight);
        path.close();
        canvas.drawPath(path, paint);
        // draw shadow
        if (mShadowThickness > 0) {
            paint.setMaskFilter(new BlurMaskFilter(mShadowThickness, BlurMaskFilter.Blur.OUTER));
            paint.setColor(mShadowColor);
            canvas.drawRoundRect(new RectF(mShadowThickness, mShadowThickness, getMeasuredWidth() - mShadowThickness, getMeasuredHeight() - mArrowHeight - mShadowThickness), mRadius, mRadius, paint);
            path.moveTo(startPoint + mShadowThickness, by);
            path.lineTo(startPoint + mArrowWidth, by);
            path.lineTo(startPoint + mArrowWidth / 2, by + mArrowHeight);
            path.close();
            canvas.drawPath(path, paint);
        }
    }

    private void drawLeftArrow(Canvas canvas, Paint paint) {
        // draw round corner rectangle
        paint.setColor(mBackgroundColor);
        canvas.drawRoundRect(new RectF(mArrowWidth, 0, getMeasuredWidth() - mShadowThickness, getMeasuredHeight() - mShadowThickness), mRadius, mRadius, paint);
        if (cy <= 0) {
            mArrowOffset = (getMeasuredHeight() - mShadowThickness - mArrowWidth) / 2;
        } else {
            int[] loc = new int[2];
            getLocationOnScreen(loc);
            mArrowOffset = cy - loc[1] - mArrowWidth / 2;
        }
        Path path = new Path();
        int startPoint = mArrowOffset;
        path.moveTo(mArrowHeight, startPoint);
        path.lineTo(mArrowHeight, startPoint + mArrowWidth);
        path.lineTo(0, startPoint + mArrowWidth / 2);
        path.close();
        canvas.drawPath(path, paint);
        // draw shadow
        if (mShadowThickness > 0) {
            paint.setMaskFilter(new BlurMaskFilter(mShadowThickness, BlurMaskFilter.Blur.OUTER));
            paint.setColor(mShadowColor);
            canvas.drawRoundRect(new RectF(mArrowHeight + mShadowThickness, mShadowThickness, getMeasuredWidth() - mShadowThickness, getMeasuredHeight() - mShadowThickness), mRadius, mRadius, paint);
            path.moveTo(mArrowHeight, startPoint + mShadowThickness);
            path.lineTo(mArrowHeight, startPoint + mArrowWidth);
            path.lineTo(0, startPoint + mArrowWidth / 2);
            path.close();
            canvas.drawPath(path, paint);
        }
    }

    private void drawRightArrow(Canvas canvas, Paint paint) {
        // draw round corner rectangle
        paint.setColor(mBackgroundColor);
        canvas.drawRoundRect(new RectF(0, 0, getMeasuredWidth() - mArrowWidth - mShadowThickness, getMeasuredHeight() - mShadowThickness), mRadius, mRadius, paint);
        if (cy <= 0) {
            mArrowOffset = (getMeasuredHeight() - mShadowThickness - mArrowWidth) / 2;
        } else {
            int[] loc = new int[2];
            getLocationOnScreen(loc);
            mArrowOffset = cy - loc[1] - mArrowWidth / 2;
        }
        Path path = new Path();
        int startPoint = mArrowOffset;
        int bx = getMeasuredWidth() - mArrowHeight - mShadowThickness;
        path.moveTo(bx, startPoint);
        path.lineTo(bx, startPoint + mArrowWidth);
        path.lineTo(bx + mArrowHeight, startPoint + mArrowWidth / 2);
        path.close();
        canvas.drawPath(path, paint);
        // draw shadow
        if (mShadowThickness > 0) {
            paint.setMaskFilter(new BlurMaskFilter(mShadowThickness, BlurMaskFilter.Blur.OUTER));
            paint.setColor(mShadowColor);
            canvas.drawRoundRect(new RectF(mShadowThickness, mShadowThickness, getMeasuredWidth() - mArrowWidth - mShadowThickness, getMeasuredHeight() - mShadowThickness), mRadius, mRadius, paint);
            path.moveTo(mArrowHeight, startPoint + mShadowThickness);
            path.lineTo(mArrowHeight, startPoint + mArrowWidth);
            path.lineTo(0, startPoint + mArrowWidth / 2);
            path.close();
            canvas.drawPath(path, paint);
        }
    }

}
