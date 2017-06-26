package com.yushan.utildemo;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by beiyong on 2017-6-8.
 */

public class QuickIndexBar extends View {
    /**
     * 画笔
     */
    private Paint paint;
    /**
     * 控件宽度
     */
    private int width;
    /**
     * 字母高度 注意不要用int类型，否则会造成精度丢失
     */
    private float cellHeight;
    /**
     * 上一次触摸字母的索引
     */
    private int lastIndex = -1;
    /**
     * 索引字母
     */
    private String[] indexArr = {"A", "B", "C", "D", "E", "F", "G", "H",
            "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U",
            "V", "W", "X", "Y", "Z", "#"};

    /**
     * 索引字母的大小
     */
    private int textSize;
    /**
     * 索引字母的颜色
     */
    private int textColor;
    @SuppressLint("HandlerLeak")
    private android.os.Handler mHandler = new android.os.Handler() {
    };

    /**
     * 构造方法
     *
     * @param context
     * @param attrs
     * @param defStyle
     */
    public QuickIndexBar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs, defStyle);
    }

    public QuickIndexBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0);
    }

    public QuickIndexBar(Context context) {
        super(context);
        init(context, null, 0);
    }

    /**
     * 初始化属性
     *
     * @param context
     * @param attrs
     * @param defStyle
     */
    private void init(Context context, AttributeSet attrs, int defStyle) {
        // 默认属性
        defaultData(context);

        // 读取xml中设置的属性
        if (attrs != null) {

            TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.QuickIndexBar);
            textSize = (int) typedArray.getDimension(R.styleable.QuickIndexBar_barTextSize, textSize);
            textColor = typedArray.getColor(R.styleable.QuickIndexBar_barTextColor, textColor);
        }

        // 出事化画笔
        paint = new Paint();
        // 设置抗锯齿
        paint.setAntiAlias(true);
        paint.setColor(textColor);
        paint.setTextSize(textSize);
        // 画笔绘制文本默认的起点是文本的左下角,将文本的起点设置为文本底边的中心
        paint.setTextAlign(Paint.Align.CENTER);
    }

    /**
     * 设置索引字体的大小
     *
     * @param size
     */
    public void setTextSize(int size) {
        textSize = size;
        paint.setTextSize(textSize);
        invalidate();
    }

    /**
     * 设置索引字体的颜色
     *
     * @param color
     */
    public void setTextColor(int color) {
        textColor = color;
        paint.setTextSize(textColor);
        invalidate();
    }

    /**
     * 设置控件默认的属性
     *
     * @param context
     */
    private void defaultData(Context context) {
        textSize = (int) dip2px(context, 16);
        textColor = 0xFFffffff;
    }

    /**
     * 绘制字母
     *
     * @param canvas:
     */
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        for (int i = 0; i < indexArr.length; i++) {
            float x = width / 2;
            float y = cellHeight / 2 + getTextHeight(indexArr[i]) / 2 + i * cellHeight;
            // 判断触摸的和正在绘制的是否是同一个字母
            paint.setColor(lastIndex == i ? Color.DKGRAY : textColor);
            canvas.drawText(indexArr[i], x, y, paint);
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        width = getMeasuredWidth();
        // 计算格子的高度
        cellHeight = getMeasuredHeight() * 1f / indexArr.length;
    }

    /**
     * 获取文本的高度
     *
     * @return :
     */
    private int getTextHeight(String text) {
        Rect bounds = new Rect();
        // 只要下面的方法功能一执行,则bound就有值了
        paint.getTextBounds(text, 0, text.length(), bounds);
        return bounds.height();
    }

    /**
     * 显示当前的字母
     *
     * @param letter
     */
    public void showCurrentWord(final TextView view, String letter) {
        if (view == null) {
            return;
        }

        if (letter == null) {
            return;
        }

        view.setVisibility(View.VISIBLE);
        view.setText(letter);
        // 让之前的任务取消掉
        mHandler.removeCallbacksAndMessages(null);
        // 延时隐藏
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                view.setVisibility(View.GONE);
            }
        }, 2000);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
                // 获取当前触摸字母索引
                int index = (int) (event.getY() / cellHeight);
                // 增强代码的健壮性
                if (index >= 0 && index < indexArr.length) {
                    // 如果当前触摸的和上一次触摸的不是同一个则打印
                    if (index != lastIndex) {
                        Log.i("tag", indexArr[index]);
                        if (listener != null) {
                            listener.onLetterChange(indexArr[index]);
                        }
                    }
                }
                lastIndex = index;
                break;
            case MotionEvent.ACTION_UP:
                // 抬起的时候重置lastIndex
                lastIndex = -1;
                break;
        }
        // 重绘
        postInvalidate();
        return true;
    }

    private OnLetterChangeListener listener;

    public void setOnLetterChangeListener(OnLetterChangeListener listener) {
        this.listener = listener;
    }

    /**
     * 回调接口
     */
    public interface OnLetterChangeListener {
        void onLetterChange(String letter);
    }

    /**
     * 将px转换成dip
     */
    public static float dip2px(Context context, float dipValue) {
        float scale = context.getResources().getDisplayMetrics().density;
        return dipValue * scale + 0.5f;
    }

    private static Toast t;

    public static void showCenter(Context context, String message) {
        if (t == null) {
            t = Toast.makeText(context, message,
                    Toast.LENGTH_SHORT);
        }
        t.setGravity(Gravity.CENTER, 0, 0);
        t.show();
        t = null;
    }
}
