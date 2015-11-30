package com.shenma.yueba.util;

import android.app.Activity;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.shenma.yueba.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2015/11/12.
 * 品牌列表管理类
 */
public class AutoBrandListManager extends  AbsBrandListManager{
    //总宽度
    int ll_width;
    int parentPaddingLeft=0;
    int parentPaddingRight=0;
    //品牌数据列表对象
    List<String> array_list;
    LinearLayout ll=null;
    List<TextView> view_array=new ArrayList<TextView>();
    int childWidth=0;
    public void settextSize(int textSizeResource_id)
    {
        this.textSize_id=textSizeResource_id;
    }


    /*******
     * @param  activity Activity
     * @param  _ll  LinearLayout
     * *******/
    public AutoBrandListManager(Activity activity,LinearLayout _ll)
    {
        super(activity);
        this.ll=_ll;
    }


    /******
     * 通知刷新刷新数据
     * ****/
    public void nofication(List<String> array_list)
    {
        this.array_list=array_list;
        if(ll!=null)
        {
            ll.removeAllViews();
        }
        view_array.clear();
        childWidth=0;
        addLinearLayout();
        setValue();
    }

    synchronized  void setValue()
    {
        if(view_array.size()>0)
        {
            for(int i=0;i<view_array.size();i++)
            {
                view_array.get(i).setTag(new Integer(i));
                view_array.get(i).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int i=(Integer)v.getTag();
                        if(onBrandItemListener!=null)
                        {
                            if(i==(view_array.size()-1))
                            {
                                onBrandItemListener.OnLastItemClick(v);
                            }else{
                                onBrandItemListener.onItemClick(v,i);
                            }
                        }

                    }
                });
            }
        }
    }

    /**********
     * 加载视图
     * ******/
    void addLinearLayout()
    {
        if(ll!=null)
        {
            if(ll.getWindowVisibility()==View.VISIBLE)
            {
                initView(ll);

            }else{
                ll.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                    @Override
                    public boolean onPreDraw() {
                        ll.getViewTreeObserver().removeOnPreDrawListener(this);
                        //初始化视图
                        initView(ll);
                        Log.i("TAG", "onPreDraw ----------------------->>");
                        return true;
                    }
                });
            }

        }

    }

    void initView(LinearLayout ll)
    {
        parentPaddingLeft=ll.getPaddingLeft();
        parentPaddingRight=ll.getPaddingRight();
        ll_width=ll.getWidth();
        setBrand();
        setValue();
    }

    /**************
     * 设置品牌的显示个数
     ************/
    private void setBrand() {
        if (ll != null) {
            ll.removeAllViews();
            view_array.clear();
            childWidth=0;
            //获取最后一位文字的宽度
            int textwidth=getLastTextWidth(more,LasttextSize_id);
            //所能使用的的宽视的图度
            int width = ll_width-(parentPaddingLeft+parentPaddingRight) -textwidth;
            int LastMarginLeft=0;
            for(int i=0;i<array_list.size();i++)
            {
                TextView tv = new TextView(activity);
                //tv.setBackgroundColor(activity.getResources().getColor(R.color.color_deeoyellow));
                tv.setSingleLine();
                tv.setEllipsize(TextUtils.TruncateAt.END);
                int item_childWidth=getMoreTextWidth(array_list.get(i).trim(),textSize_id);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                params.gravity= Gravity.LEFT|Gravity.BOTTOM;
                params.leftMargin = marginvalue;
                params.rightMargin = marginvalue;
                if((childWidth+item_childWidth)>width)
                {
                    //addLastView(LastMarginLeft);
                    return;
                }else
                {
                    int textsize=getTextSize(textSize_id);
                    tv.setTextSize(textsize);
                    tv.setText(ToolsUtil.nullToString(array_list.get(i)));
                    ll.addView(tv, params);
                    childWidth+=item_childWidth;
                    view_array.add(tv);

                }
                LastMarginLeft=width-childWidth;
                addLastView(LastMarginLeft);
            }

        }
    }

    void addLastView(int marginleft)
    {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.gravity= Gravity.LEFT|Gravity.BOTTOM;
        params.leftMargin = marginleft+marginvalue;
        params.rightMargin = 0;
        TextView tv = new TextView(activity);
        tv.setSingleLine();
        tv.setEllipsize(TextUtils.TruncateAt.END);
        tv.setText(ToolsUtil.nullToString(more));
        int textsize=getTextSize(LasttextSize_id);
        tv.setTextSize(textsize);
        ll.addView(tv, params);
        view_array.add(tv);
    }

}
