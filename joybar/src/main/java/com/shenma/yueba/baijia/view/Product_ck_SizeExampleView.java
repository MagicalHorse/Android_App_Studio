package com.shenma.yueba.baijia.view;

import android.app.Activity;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.shenma.yueba.R;
import com.shenma.yueba.application.MyApplication;
import com.shenma.yueba.baijia.modle.RequestCKProductDeatilsInfo;
import com.shenma.yueba.util.ToolsUtil;
import com.shenma.yueba.view.MyListView;

/**
 * Created by Administrator on 2015/10/19.
 * 专柜 商品详情 底部TAB 视图--尺寸参考
 */
public class Product_ck_SizeExampleView {
    RequestCKProductDeatilsInfo bean;
    Activity activity;
    ImageView parentView;
    public Product_ck_SizeExampleView(Activity activity, RequestCKProductDeatilsInfo bean)
    {
        this.activity=activity;
        this.bean=bean;
        initView();
    }

    void initView()
    {
        parentView = new ImageView(activity);
        ViewGroup.LayoutParams params=new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT);
        parentView.setLayoutParams(params);
        parentView.setScaleType(ImageView.ScaleType.FIT_CENTER);
        String url="";
        if(bean==null || bean.getData()==null)
        {
            url="";
        }else
        {
            url= ToolsUtil.nullToString(bean.getData().getSizeContrastPic());
        }

        MyApplication.getInstance().ShowImage(url,parentView);
        //MyApplication.getInstance().getImageLoader().displayImage(url, parentView, MyApplication.getInstance().getDisplayImageOptions());
    }


    public View getShowData()
    {
         return parentView;
    }
}
