package com.shenma.yueba.util;

import android.app.Activity;
import android.graphics.Paint;
import android.graphics.Rect;
import android.text.TextPaint;
import android.util.TypedValue;
import android.view.View;

import com.shenma.yueba.R;

import java.util.List;

/**
 * Created by Administrator on 2015/11/16.
 */
public abstract class AbsBrandListManager {
    //每个品牌之间的距离
    int marginvalue=0;
    //最后文字 大小
    int LasttextSize_id= R.dimen.text_Lmoreauthentication_textsize;
    //品牌信息文字大小
    int textSize_id=R.dimen.text_authentication_textsize;
    Activity activity;
    String more="更多品牌...";
    //设置位子padding 左和右的 距离
    int textpadding=0;

    public void setTextpadding(int textpadding) {
        if(textpadding>0)
        {
            this.textpadding=activity.getResources().getDimensionPixelSize(textpadding);
        }
    }

    //点击回调
    OnBrandItemListener onBrandItemListener;
    public AbsBrandListManager(Activity activity)
    {
        this.activity=activity;
    }

    public void setOnClickListener(OnBrandItemListener onBrandItemListener)
    {
        this.onBrandItemListener=onBrandItemListener;
    }

    public void settextSize(int _textSize_id)
    {
        this.textSize_id=_textSize_id;
    }

    public abstract void nofication(List<String> array_list);
    /********
     * 品牌间距赋值
     * ***/
    public void setChildMargin(int marginvalue)
    {
        this.marginvalue=marginvalue;
    }

    /********
     * 设置 最后显示的文字信息以及文字大小
     * ****/
    public void setLastText(String s,int LasttextSizeResource_id)
    {
        if(s!=null)
        {
            more=s;
        }
        this.LasttextSize_id=LasttextSizeResource_id;
    }

    /*********
     * 点击回调接口
     * *****/
    public interface OnBrandItemListener
    {
        void onItemClick(View v,int i);
        void OnLastItemClick(View v);
    }

    /*******
     * 计算文字的宽度
     * *****/
    synchronized int getMoreTextWidth(String str,int resourceID)
    {
        TextPaint textPaint=new TextPaint();
        int textsize=activity.getResources().getDimensionPixelSize(resourceID);
        textPaint.setTextSize(textsize);
        int textwidth=(int)textPaint.measureText(str);
        textwidth=textwidth+(marginvalue*2)+(textpadding*2);;
        return textwidth;
    }

    /*******
     * 计算文字的宽度
     * *****/
    synchronized int getLastTextWidth(String str,int resourceID)
    {
        TextPaint textPaint=new TextPaint();
        int textsize=activity.getResources().getDimensionPixelSize(resourceID);
        textPaint.setTextSize(textsize);
        int textwidth=(int)textPaint.measureText(str);
        textwidth=textwidth+marginvalue+(textpadding*2);
        return textwidth;
    }

    /******
     * 根据资源id 获取 对应的像素值
     * ****/
    int getTextSize(int _textsize)
    {
        TypedValue typedValue=new TypedValue();
        activity.getResources().getValue(_textsize, typedValue, true);
        return (int) TypedValue.complexToFloat(typedValue.data);
    }

}
