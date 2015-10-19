package com.shenma.yueba.baijia.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.shenma.yueba.R;
import com.shenma.yueba.baijia.modle.RequestCKProductDeatilsInfo;
import com.shenma.yueba.baijia.view.Product_ck_AftermaketServerView;
import com.shenma.yueba.baijia.view.Product_ck_ImginfoView;
import com.shenma.yueba.baijia.view.Product_ck_SizeExampleView;
import com.shenma.yueba.view.MyListView;

import java.util.Random;

/**
 * Created by Administrator on 2015/10/12.
 * 专柜商品详情页 底部 TAB 切换 fragment
 * 根据 Type 类型 返回对应视图
 */
public class ProductExtTab1Fragment extends Fragment {
    RequestCKProductDeatilsInfo bean;
    Type type;
    public enum Type
    {
        Img_details,//图片详细
        Size_explare,//尺码参考
        Aftermarket_Server//售后服务
    }
    Activity activity;
    View parentView;


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.activity = activity;
        Bundle bundle=getArguments();
        bean=(RequestCKProductDeatilsInfo)bundle.getSerializable("ProductDetails_data");
        type=(Type)bundle.getSerializable("type");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (parentView == null) {
            parentView = getViewByType();
        }
        if (parentView.getParent() != null) {
            ((ViewGroup) parentView.getParent()).removeView(parentView);
        }
        return parentView;
    }

  View getViewByType()
  {
      View v=new View(activity);
      switch(type)
      {
          case Img_details:
              v= new Product_ck_ImginfoView(getActivity(),bean).getShowData();
          break;
          case Size_explare:
              v=new Product_ck_SizeExampleView(getActivity(),bean).getShowData();
              break;
          case Aftermarket_Server:
              v=new Product_ck_AftermaketServerView(getActivity(),bean).getShowData();
      }
      return v;
  }
}
