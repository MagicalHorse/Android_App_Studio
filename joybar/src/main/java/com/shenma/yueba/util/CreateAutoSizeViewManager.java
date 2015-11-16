package com.shenma.yueba.util;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2015/11/13.
 * 该类 根据 每行显示的个数 以当前 父视图的宽度 计算出
 */
public class CreateAutoSizeViewManager {
    //最大子类个数
    int maxCount=1;
    //子视图的资源id
    int layoutResouceId;
    Activity activity;
    LinearLayout ll;
    int childmarginLeftRight=0;
    List<View> view_array=new ArrayList<View>();
    InflaterSucessListener listener;
    public CreateAutoSizeViewManager(Activity activity, int childmarginLeftRight, int layoutResouceId, int maxCount, LinearLayout _ll, InflaterSucessListener listener)
    {
        this.ll=_ll;
        this.activity=activity;
        this.layoutResouceId=layoutResouceId;
        this.maxCount=maxCount;
        this.childmarginLeftRight=childmarginLeftRight;
        this.listener=listener;
        if(layoutResouceId<=0)
        {
            return;
        }
        if(ll==null)
        {
            return;
        }

        ll.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                ll.getViewTreeObserver().removeOnPreDrawListener(this);
                initView();
                return true;
            }
        });
    }

    public View getView()
    {
        return ll;
    }

    void initView()
    {
        int width=ll.getMeasuredWidth();
        int childWidth=width/maxCount-(2*childmarginLeftRight);
        for(int i=0;i<maxCount;i++)
        {
            View v= LayoutInflater.from(activity).inflate(layoutResouceId,null);
            LinearLayout.LayoutParams params=new LinearLayout.LayoutParams(childWidth,childWidth);
            params.leftMargin=childmarginLeftRight;
            params.rightMargin=childmarginLeftRight;
            view_array.add(v);
            ll.addView(v, params);
        }
        if(listener!=null)
        {
            listener.returnChildListView(view_array);
        }
    }

    public List<View> getChildView()
    {
        return view_array;
    }

    public interface InflaterSucessListener
    {
        void returnChildListView(List<View> view_array);
    }
}
