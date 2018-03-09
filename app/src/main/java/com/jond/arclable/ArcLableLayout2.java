package com.jond.arclable;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


/**
 * @author: Created by jond on 2018/3/7.
 * @description:环状标签 onMeasure——>onLayout——>onDraw
 */
public class ArcLableLayout2 extends View {

    private ImageView iv_center;
    private int width, height;
    private Context mContext;

    public ArcLableLayout2(Context context) {
        this(context, null);
        mContext = context;
    }

    public ArcLableLayout2(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int sizeWidth = MeasureSpec.getSize(widthMeasureSpec);
        int modeWidth = MeasureSpec.getMode(widthMeasureSpec);
        int sizeHeight = MeasureSpec.getSize(heightMeasureSpec);
        int modeHeight = MeasureSpec.getMode(heightMeasureSpec);


        width = sizeWidth;
        height = sizeHeight;
    }

    private List<String> list;

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        list = new ArrayList<>();
        list.add("家庭贡献者");
        list.add("理财");
        list.add("运动达人");
        list.add("环保卫士");
        list.add("工薪阶层");
        list.add("90后");
        list.add("绿色消费");
        list.add("女");
        list.add("未婚");
        list.add("无贷款");
        //按照长度排个序
        Collections.sort(list, new Comparator<String>() {

            @Override
            public int compare(String o1, String o2) {
                int i = o1.length() - o2.length();
                if (i == 0) {
                    return o1.length() - o2.length();
                }
                return i;
            }
        });

        Paint p = new Paint();
        p.setAntiAlias(true);

        int imageW = Dip2PxUtils.dip2px(mContext, 40);  //图片的大小 40*40

        for (int i = 0; i < list.size(); i++) {
            if (i % 2 == 0) {
                drawText(list.get(i), canvas, p, width / 8, Dip2PxUtils.dip2px(mContext, height / 12 + i * 15), imageW, true);
            } else {
                drawText(list.get(i), canvas, p, 6 * width / 8, Dip2PxUtils.dip2px(mContext, height / 12 + (i - 1) * 15), imageW, false);
            }
        }

        Bitmap bitmap = BitmapFactory.decodeResource(this.getResources(), R.drawable.ic_camera);
        drawImage(canvas, bitmap, width / 2 - imageW / 2, height / 3 - imageW / 2, imageW, imageW);
    }

    //画文案
    private void drawText(String textString, Canvas canvas, Paint mbPaint, int textWidth, int textHeight, int imageW, Boolean isLeft) {
        //文字大小
        mbPaint.setTextSize(Dip2PxUtils.dip2px(mContext, 12));

        // 1.用FontMetrics对象计算高度
        Paint.FontMetricsInt fontMetrics = mbPaint.getFontMetricsInt();
        //textHeight = fontMetrics.bottom - fontMetrics.top; //详细:(baseY＋bottom)-(baseY-top);

        //2.用bounds计算宽度
        Rect bounds = new Rect();
        mbPaint.getTextBounds(textString, 0, textString.length(), bounds);
        //textWidth = bounds.right - bounds.left;

        //3.画背景
        mbPaint.setColor(getResources().getColor(R.color.colorCCCCCC));
        RectF roundRect = new RectF();
        roundRect.left = bounds.left + textWidth - Dip2PxUtils.dip2px(mContext, 4);
        roundRect.right = bounds.right + textWidth + Dip2PxUtils.dip2px(mContext, 4);
        roundRect.top = fontMetrics.top + textHeight - Dip2PxUtils.dip2px(mContext, 1);
        roundRect.bottom = fontMetrics.bottom + textHeight + Dip2PxUtils.dip2px(mContext, 1);
        canvas.drawRoundRect(roundRect, 10, 10, mbPaint);

        //4.画文字
        mbPaint.setColor(getResources().getColor(R.color.color333333));
        canvas.drawText(textString, textWidth, textHeight, mbPaint);//这里要计算文字绘制的起点，因为文字是按baseLine来绘制的。

        //5.连线
        mbPaint.setColor(getResources().getColor(R.color.colorCCCCCC));
        mbPaint.setStrokeWidth(2.0f);
        if (isLeft) {
            canvas.drawLine(width / 2, height / 3, roundRect.right, (roundRect.top + roundRect.bottom) / 2, mbPaint);
        } else {
            canvas.drawLine(width / 2, height / 3, roundRect.left, (roundRect.top + roundRect.bottom) / 2, mbPaint);
        }

    }

    //绘制中间图案
    public void drawImage(Canvas canvas, Bitmap bitmap, int x, int y) {
        // 绘制图像 将bitmap对象显示在坐标 x,y上
        canvas.drawBitmap(bitmap, x, y, null);
    }

    public void drawImage(Canvas canvas, Bitmap blt, int x, int y,
                          int w, int h) {
        Rect dst = new Rect();// 屏幕 >>目标矩形

        dst.left = x;
        dst.top = y;
        dst.right = x + w;
        dst.bottom = y + h;
        // 画出指定的位图，位图将自动--》缩放/自动转换，以填补目标矩形
        // 这个方法的意思就像 将一个位图按照需求重画一遍，画后的位图就是我们需要的了
        canvas.drawBitmap(blt, null, dst, null);
        dst = null;
    }

    private void recycle(Bitmap bitmap) {
        // 先判断是否已经回收
        if (bitmap != null && !bitmap.isRecycled()) {
            // 回收并且置为null
            bitmap.recycle();
            bitmap = null;
        }
    }
}

