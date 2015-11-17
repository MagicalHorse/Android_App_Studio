package com.shenma.yueba.util;

import android.app.Activity;
import android.graphics.Paint;
import android.graphics.Rect;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;
import android.widget.TextView;


import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2015/11/12.
 * 品牌列表管理类
 */
public class BrandListManager extends  AbsBrandListManager{
    //最多显示的品牌数
    int maxShowCoun=6;
    //总宽度
    int ll_width;
    int parentPaddingLeft=0;
    int parentPaddingRight=0;
    //品牌数据列表对象
    List<String> array_list;

    LinearLayout ll=null;

    List<TextView> view_array=new ArrayList<TextView>();

    /*******
     * @param  maxShowCoun int 每行显示的最多个数
     * @param  _ll  LinearLayout
     * *******/
    public BrandListManager(Activity activity,int maxShowCoun,LinearLayout _ll)
    {
        super(activity);
        this.maxShowCoun=maxShowCoun;
        this.ll=_ll;
        addLinearLayout();
    }


    /******
     * 通知刷新刷新数据
     * ****/
    public void nofication(List<String> array_list)
    {
        this.array_list=array_list;
        setValue();
    }

    synchronized  void setValue()
    {
        if(view_array.size()>0)
        {
            for(int i=0;i<view_array.size();i++)
            {
                view_array.get(i).setTag(new Integer(i));
                if(i<(array_list.size()))
                {
                    view_array.get(i).setEnabled(true);
                    view_array.get(i).setText(array_list.get(i));
                    view_array.get(i).setVisibility(View.VISIBLE);
                }else
                {
                    view_array.get(i).setText("");
                    view_array.get(i).setEnabled(false);
                    view_array.get(i).setVisibility(View.INVISIBLE);
                }
                if(i==(maxShowCoun-1))
                {
                    view_array.get(i).setEnabled(true);
                    view_array.get(i).setVisibility(View.VISIBLE);
                    view_array.get(i).setText(more);
                }
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
            //获取文字的宽度
            int textwidth=getMoreTextWidth(more,LasttextSize_id);
            //计算每个文本控件的宽度
            int width = (ll_width-parentPaddingLeft+parentPaddingRight) - (2 * marginvalue)-textwidth;

            //计算每个子类的宽度（排除字符串 more的宽度）
            int childWidth = width / (maxShowCoun-1) - 2 * marginvalue;
            for (int i = 0; i < maxShowCoun; i++) {
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(childWidth, LinearLayout.LayoutParams.WRAP_CONTENT);
                params.gravity= Gravity.LEFT|Gravity.BOTTOM;
                params.leftMargin = marginvalue;
                params.rightMargin = marginvalue;
                TextView tv = new TextView(activity);
                tv.setSingleLine();
                tv.setEllipsize(TextUtils.TruncateAt.END);
                //tv.setBackgroundColor(activity.getResources().getColor(R.color.color_deeoyellow));
                tv.setGravity(Gravity.CENTER);
                if (i ==(maxShowCoun-1)) {
                    params.width=textwidth;
                    int textsize=getTextSize(LasttextSize_id);
                    tv.setTextSize(textsize);
                    //tv.setTextSize(TypedValue.COMPLEX_UNIT_SP,textsize);
                } else {
                    int textsize=getTextSize(textSize_id);
                    tv.setTextSize(textsize);
                }
                ll.addView(tv, params);
                view_array.add(tv);
                tv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(onBrandItemListener!=null)
                        {
                            int i=(Integer)v.getTag();
                            if(i==(maxShowCoun-1))
                            {
                                onBrandItemListener.OnLastItemClick(v);
                            }else
                            {
                                onBrandItemListener.onItemClick(v,view_array.indexOf(v));
                            }

                        }
                    }
                });
            }
        }
    }
}
