package com.shenma.yueba.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

/**
 * Created by Administrator on 2015/10/13.
 */
public class CustomViewPager extends ViewPager {
    int smallHieght=0;//最新高度
    public int getSmallHieght() {
        return smallHieght;
    }

    public void setSmallHieght(int smallHieght) {
        this.smallHieght = smallHieght;
    }


    public CustomViewPager(Context context) {
        super(context);
    }

    public CustomViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // TODO Auto-generated method stub
        int viewHeight = 0;
        View childView = getChildAt(getCurrentItem());
        if(childView!=null)
        {
            childView.measure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(0,MeasureSpec.UNSPECIFIED));
            viewHeight = childView.getMeasuredHeight();
            Log.i("TAG", "onPageSelected heightMeasureSpec:viewHeight:" + viewHeight);
            if(viewHeight<smallHieght)
            {
                viewHeight=smallHieght;
            }
            Log.i("TAG", "onPageSelected heightMeasureSpec:smallHieght:" + smallHieght);
            heightMeasureSpec = MeasureSpec.makeMeasureSpec(viewHeight, MeasureSpec.EXACTLY);
            Log.i("TAG", "onPageSelected heightMeasureSpec:" + heightMeasureSpec);
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }else
        {
            if(smallHieght>0)
            {
                heightMeasureSpec = MeasureSpec.makeMeasureSpec(viewHeight, MeasureSpec.EXACTLY);

            }
            super.onMeasure(widthMeasureSpec,heightMeasureSpec);
            Log.i("TAG", "onPageSelected heightMeasureSpec:null" + heightMeasureSpec);
        }
    }

}
