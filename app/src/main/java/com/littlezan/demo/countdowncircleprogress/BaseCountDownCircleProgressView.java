package com.littlezan.demo.countdowncircleprogress;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.support.annotation.ColorInt;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;

import com.littlezan.demo.R;

import java.util.concurrent.TimeUnit;

/**
 * ClassName: BaseCountDownCircleProgressView
 * Description:
 *
 * @author 彭赞
 * @version 1.0
 * @since 2018-08-01  09:20
 */
public abstract class BaseCountDownCircleProgressView extends View {

    @ColorInt
    protected int finishedStrokeColor;
    @ColorInt
    protected int unfinishedStrokeColor;
    protected float finishedStrokeWidth;
    protected float unfinishedStrokeWidth;
    @ColorInt
    protected int innerBackgroundColor;

    protected int startingDegree;
    protected float progress = 0;
    protected int max;
    /**
     * 倒计时总时长
     */
    private long countDownMillisInFuture;
    private OnCountDownListener listener;
    private long onTickCount;

    public BaseCountDownCircleProgressView(Context context) {
        super(context);
    }

    public BaseCountDownCircleProgressView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public BaseCountDownCircleProgressView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        final TypedArray attributes = context.getTheme().obtainStyledAttributes(attrs, R.styleable.CountDownCircleProgress, defStyleAttr, 0);
        initByAttributes(attributes);
        attributes.recycle();
    }

    private void initByAttributes(TypedArray attributes) {
        finishedStrokeColor = attributes.getColor(R.styleable.CountDownCircleProgress_donut_finished_color, Color.rgb(66, 145, 241));
        unfinishedStrokeColor = attributes.getColor(R.styleable.CountDownCircleProgress_donut_unfinished_color, Color.rgb(204, 204, 204));
        finishedStrokeWidth = attributes.getDimension(R.styleable.CountDownCircleProgress_donut_finished_stroke_width, 10f);
        unfinishedStrokeWidth = attributes.getDimension(R.styleable.CountDownCircleProgress_donut_unfinished_stroke_width, 10f);
        innerBackgroundColor = attributes.getColor(R.styleable.CountDownCircleProgress_donut_background_color, Color.TRANSPARENT);
        startingDegree = attributes.getInt(R.styleable.CountDownCircleProgress_donut_circle_starting_degree, 0);
        setMax(attributes.getInt(R.styleable.CountDownCircleProgress_donut_max, 100));
        setProgress(attributes.getFloat(R.styleable.CountDownCircleProgress_donut_progress, 0));
    }

    public int getStartingDegree() {
        return startingDegree;
    }

    public void setStartingDegree(int startingDegree) {
        this.startingDegree = startingDegree;
        this.invalidate();
    }

    public float getProgressAngle() {
        return getProgress() / (float) max * 360f;
    }

    public float getProgress() {
        return progress;
    }

    public void setProgress(float progress) {
        this.progress = progress;
        if (this.progress > getMax()) {
            this.progress %= getMax();
        }
        if (listener != null) {
            if (getProgress() >= getMax()) {
                listener.onFinish();
                onTickCount = 0;
            } else {
                long currentTimeInMillis = Math.round(countDownMillisInFuture * getProgress() / getMax());
                if (TimeUnit.MILLISECONDS.toSeconds(currentTimeInMillis) > onTickCount) {
                    listener.onOneSecondTick(Math.round(countDownMillisInFuture * (getMax() - getProgress()) / getMax()));
                    onTickCount++;
                }
            }
        }
        invalidate();
    }

    public int getMax() {
        return max;
    }

    public void setMax(int max) {
        if (max > 0) {
            this.max = max;
            invalidate();
        }
    }


    public void startCountDown(long millisInFuture, OnCountDownListener listener) {
        this.countDownMillisInFuture = millisInFuture;
        this.listener = listener;
        onTickCount = 0;
        ObjectAnimator animator = ObjectAnimator.ofFloat(this, "progress", 0, getMax());
        animator.setInterpolator(new LinearInterpolator());
        animator.setDuration(millisInFuture);
        animator.start();
    }

    public interface OnCountDownListener {
        /**
         * 剩余时间
         *
         * @param millisUntilFinished millisUntilFinished
         */
        void onOneSecondTick(long millisUntilFinished);

        /**
         * 完成倒计时
         */
        void onFinish();
    }


}
