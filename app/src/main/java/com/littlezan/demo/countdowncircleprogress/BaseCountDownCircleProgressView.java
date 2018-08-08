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

/**
 * ClassName: BaseCountDownCircleProgressView
 * Description:
 *
 * @author 彭赞
 * @version 1.0
 * @since 2018-08-01  09:20
 */
public abstract class BaseCountDownCircleProgressView extends View {

    private static final String TAG = "BaseCountDownCircleProg";

    /**
     * 回调时间间隔毫秒
     */
    private static final int TICK_INTERVAL_IN_MILLIS = 200;

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
    private long onTickCount = 0;
    private ObjectAnimator animator;

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
                int millisUntilFinished = Math.round(countDownMillisInFuture * (getMax() - getProgress()) / getMax());
                if (countDownMillisInFuture - millisUntilFinished > onTickCount) {
                    listener.onTick(millisUntilFinished);
                    onTickCount = +TICK_INTERVAL_IN_MILLIS;
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

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (animator != null) {
            animator.cancel();
        }
    }

    public void startCountDown(long millisInFuture, OnCountDownListener listener) {
        this.countDownMillisInFuture = millisInFuture;
        this.listener = listener;
        onTickCount = 0;
        if (animator != null) {
            animator.cancel();
        }
        animator = ObjectAnimator.ofFloat(this, "progress", 0, getMax());
        animator.setInterpolator(new LinearInterpolator());
        animator.setDuration(millisInFuture);
        animator.start();
    }

    public void stopCountDown() {
        if (animator != null) {
            animator.cancel();
        }
    }

    public interface OnCountDownListener {
        /**
         * 剩余时间
         *
         * @param millisUntilFinished millisUntilFinished
         */
        void onTick(long millisUntilFinished);

        /**
         * 完成倒计时
         */
        void onFinish();
    }


}
