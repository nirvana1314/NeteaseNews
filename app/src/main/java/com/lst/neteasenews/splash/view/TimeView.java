package com.lst.neteasenews.splash.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.lst.neteasenews.R;

/**
 * Created by lisongtao on 2017/10/30.
 */

public class TimeView extends View {

    // 文字间距
    int padding = 10;
    //  内圆直径
    int inner;
    //  外圆直径
    int all;
    private TextPaint mTextPaint;
    private Paint innerPaint;
    private Paint outPaint;
    private RectF rectF;

    private int baseline;

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;

        invalidate();
    }

    public int progress = 0;


    // 每秒刷新多少次
    private int countPer = 24;
    // 动画执行时间
    private int animDur = 3;
    // 每次转多少角度
    private int anglePer = 360 / (animDur * countPer);
    // 当前角度
    private int angleTotal = 0;

    private Handler mhandler;

    OnTimeClickListener listener;


    public TimeView(Context context) {
        super(context);
    }

    public TimeView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        //获取到xml定义的属性
        TypedArray arry = context.obtainStyledAttributes(attrs, R.styleable.TimeView);
        int outColor = arry.getColor(R.styleable.TimeView_outerColor,Color.BLUE);

        mTextPaint = new TextPaint();
        //抗锯齿
        mTextPaint.setFlags(Paint.ANTI_ALIAS_FLAG);
        mTextPaint.setTextSize(50);
        mTextPaint.setColor(Color.WHITE);


        float textWidth = mTextPaint.measureText("跳过");
        inner = (int) (textWidth + 2 * padding);
        all = inner + 2 * padding;

        innerPaint = new Paint();
        innerPaint.setFlags(Paint.ANTI_ALIAS_FLAG);
        innerPaint.setColor(Color.GRAY);

        outPaint = new Paint();
        outPaint.setFlags(Paint.ANTI_ALIAS_FLAG);
        outPaint.setColor(outColor);
        outPaint.setStyle(Paint.Style.STROKE);
        outPaint.setStrokeWidth(padding);

        rectF = new RectF(padding / 2, padding / 2, all - padding / 2, all - padding / 2);

        Rect targetRect = new Rect(0, 0, all, all);
        Paint.FontMetricsInt fontMetrics = mTextPaint.getFontMetricsInt();

        baseline = (targetRect.bottom + targetRect.top - fontMetrics.bottom - fontMetrics.top) / 2;

        showJumpAnim();
    }

    public void setListener(OnTimeClickListener listener) {
        this.listener = listener;
    }

    public void showJumpAnim() {
        mhandler = new Handler();
        mhandler.post(refreshRing);
    }

    Runnable refreshRing = new Runnable() {
        @Override
        public void run() {
            mhandler.postDelayed(refreshRing, 1000/countPer);
            angleTotal+=anglePer;
            if (angleTotal > 360) {
                mhandler.removeCallbacks(refreshRing);
            }else {
                setProgress(angleTotal);
            }
        }
    };

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(all, all);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawArc(rectF, -90, progress, true, outPaint);
        canvas.drawCircle(all/2, all/2, inner/2+1, innerPaint);
        canvas.drawText("跳过", padding*2, baseline, mTextPaint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                setAlpha(0.3f);
                break;
            case MotionEvent.ACTION_UP:
                setAlpha(1.0f);
                if (listener != null) {
                    mhandler.removeCallbacks(refreshRing);
                    listener.onClickTime();
                }
                break;
        }
        return true;
    }
}
