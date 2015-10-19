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
import com.shenma.yueba.baijia.modle.ProductsDetailsTagInfo;
import com.shenma.yueba.baijia.modle.RequestCKProductDeatilsInfo;
import com.shenma.yueba.util.ToolsUtil;
import com.shenma.yueba.view.MyListView;

import java.util.List;

/**
 * Created by Administrator on 2015/10/19.
 * 专柜 商品详情 底部TAB 视图--图片详情列表
 */
public class Product_ck_ImginfoView {
    RequestCKProductDeatilsInfo bean;
    Activity activity;
    View parentView;
    MyListView productexttab1_layout_listview;
    public Product_ck_ImginfoView(Activity activity,RequestCKProductDeatilsInfo bean)
    {
        this.activity=activity;
        this.bean=bean;
        initView();
    }

    void initView()
    {
        parentView = LayoutInflater.from(activity).inflate(R.layout.productexttab1_layout, null);
        ViewGroup.LayoutParams params=new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT);
        parentView.setLayoutParams(params);
        productexttab1_layout_listview = (MyListView) parentView.findViewById(R.id.productexttab1_layout_listview);
        productexttab1_layout_listview.setAdapter(new BaseAdapter() {
            @Override
            public int getCount() {

                if(bean==null || bean.getData()==null || bean.getData().getProductPic()==null)
                {
                    return 0;
                }else
                {
                   return  bean.getData().getProductPic().size();
                }

            }

            @Override
            public Object getItem(int position) {
                return null;
            }

            @Override
            public long getItemId(int position) {
                return position;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                if (convertView == null) {
                    convertView = new ImageView(activity);
                }
                ((ImageView) convertView).setImageResource(R.drawable.default_pic);
                DisplayMetrics dm = new DisplayMetrics();
                activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
                ((ImageView) convertView).setScaleType(ImageView.ScaleType.CENTER_CROP);
                convertView.setLayoutParams(new AbsListView.LayoutParams(dm.widthPixels, dm.widthPixels));
                String url="";
                List<ProductsDetailsTagInfo> info=bean.getData().getProductPic();
                ProductsDetailsTagInfo productsDetailsTagInfo=info.get(position);
                MyApplication.getInstance().getImageLoader().displayImage(ToolsUtil.nullToString(productsDetailsTagInfo.getLogo()),((ImageView) convertView),MyApplication.getInstance().getDisplayImageOptions());
                return convertView;
            }
        });
    }


    public View getShowData()
    {
         return parentView;
    }
}
