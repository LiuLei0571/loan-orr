package com.load.third.jqm.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;

import com.load.third.jqm.utils.Utils;

/**
 * Created by Administrator on 2017/4/6.
 */

public class MaskView extends AppCompatImageView {
    private Paint mLinePaint;
    private Paint mAreaPaint;
    private Rect mCenterRect = null;
    private Context mContext;


    public MaskView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initPaint();
        mContext = context;
        Point p = Utils.getScreenMetrics(mContext);
        widthScreen = p.x;
        heightScreen = p.y;
    }

    private void initPaint(){
        //绘制中间透明区域矩形边界的Paint
        mLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mLinePaint.setColor(Color.TRANSPARENT);
        mLinePaint.setStyle(Paint.Style.STROKE);
        mLinePaint.setStrokeWidth(5f);
        mLinePaint.setAlpha(30);

        //绘制四周阴影区域
        mAreaPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mAreaPaint.setColor(Color.GRAY);
        mAreaPaint.setStyle(Paint.Style.FILL);
        mAreaPaint.setAlpha(180);

    }

    public void setCenterRect(Rect r){
        this.mCenterRect = r;
        postInvalidate();
    }
    public void clearCenterRect(Rect r){
        this.mCenterRect = null;
    }

    int widthScreen, heightScreen;
    @Override
    protected void onDraw(Canvas canvas) {
        if(mCenterRect == null)
            return;
        //绘制四周阴影区域
        canvas.drawRect(0, 0, widthScreen, mCenterRect.top, mAreaPaint);//上
        canvas.drawRect(0, mCenterRect.bottom + 1, widthScreen, heightScreen, mAreaPaint);//下
        canvas.drawRect(0, mCenterRect.top, mCenterRect.left - 1, mCenterRect.bottom  + 1, mAreaPaint);//左
        canvas.drawRect(mCenterRect.right + 1, mCenterRect.top, widthScreen, mCenterRect.bottom + 1, mAreaPaint);//右

        //绘制目标透明区域
        canvas.drawRect(mCenterRect, mLinePaint);
        super.onDraw(canvas);
    }

}
