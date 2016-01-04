package com.shenma.yueba.baijia.view;

import android.app.Activity;
import android.text.Html;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.shenma.yueba.R;
import com.shenma.yueba.baijia.modle.RequestCKProductDeatilsInfo;
import com.shenma.yueba.util.ToolsUtil;
import com.shenma.yueba.view.MyListView;

/**
 * Created by Administrator on 2015/10/19.
 * 专柜 商品详情 底部TAB 视图--售后服务
 */
public class Product_ck_AftermaketServerView {
    RequestCKProductDeatilsInfo bean;
    Activity activity;
    TextView parentView;
    public Product_ck_AftermaketServerView(Activity activity, RequestCKProductDeatilsInfo bean)
    {
        this.activity=activity;
        this.bean=bean;
        initView();
    }

    void initView()
    {
        parentView = new TextView(activity);
        ViewGroup.LayoutParams params=new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT);
        parentView.setLayoutParams(params);
        parentView.setPadding(10,5,10,5);
        String str="售后服务";
        if(bean==null || bean.getData()==null)
        {
            str="售后服务";
        }else
        {
            str=bean.getData().getStoreService();
        }
        parentView.setText(Html.fromHtml(ToolsUtil.nullToString(str)));
    }


    public View getShowData()
    {
         return parentView;
    }
}
