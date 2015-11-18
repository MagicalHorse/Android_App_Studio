package com.shenma.yueba.util;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2015/11/13.
 * 根据提供的视图  在linearlayout 中 设置 weight=1 平均分配 视图的大小
 *  设置 制定的 视图的高度 等于 平均分配的宽度值
 */
public class AverageViewManager {
    //最大子类个数
    int maxCount=1;
    //子视图的资源id
    int layoutResouceId;
    Activity activity;
    LinearLayout ll;
    List<View> view_array=new ArrayList<View>();
    InflaterSucessListener listener;
    int heightresourceId;
    int item_margin;
    /*********
     *@param  layoutResouceId int 加载的资源id
     *@param  heightresourceId int 需要设置高度的资源id
     *@param  maxCount int item的总数
     *@param  _ll LinearLayout 加载资源的类
     *@param  item_margin  int 每个item之间的间距
     *@param listener  InflaterSucessListener 加载完成回调
     * *********/
    public AverageViewManager(Activity activity, int layoutResouceId,int heightresourceId, int maxCount, LinearLayout _ll,int item_margin,InflaterSucessListener listener)
    {
        this.ll=_ll;
        this.activity=activity;
        this.layoutResouceId=layoutResouceId;
        this.heightresourceId=heightresourceId;
        this.item_margin=item_margin;
        this.maxCount=maxCount;
        this.listener=listener;
        if(layoutResouceId<=0 || heightresourceId<=0)
        {
            return;
        }
        if(ll==null)
        {
            return;
        }

        if(ll.getWindowVisibility()==View.VISIBLE)
        {
            initView();
        }else
        {
            ll.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                @Override
                public boolean onPreDraw() {
                    ll.getViewTreeObserver().removeOnPreDrawListener(this);
                    initView();
                    return true;
                }
            });
        }

    }

    public View getView()
    {
        return ll;
    }

    void initView()
    {
        int width=ll.getMeasuredWidth();
        int paddingleft=ll.getPaddingLeft();
        int paddingright=ll.getPaddingRight();

        int childWidth=(width-paddingleft-paddingright-((maxCount-1)*item_margin))/maxCount;
        for(int i=0;i<maxCount;i++)
        {
            View v= LayoutInflater.from(activity).inflate(layoutResouceId,null);
            LinearLayout.LayoutParams params=new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT);
            params.weight=1;
            if(i==0 && (maxCount!=1))
            {
                params.leftMargin=0;
                params.rightMargin=0;
            }else
            {
                params.leftMargin=item_margin;
                params.rightMargin=0;
            }

            //获取需要制定高度的视图对象
            View childview=v.findViewById(heightresourceId);
            ViewGroup.LayoutParams childparma=childview.getLayoutParams();
            childparma.height=childWidth;
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
