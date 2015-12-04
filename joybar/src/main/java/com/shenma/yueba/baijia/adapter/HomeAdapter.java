package com.shenma.yueba.baijia.adapter;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Paint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.shenma.yueba.R;
import com.shenma.yueba.application.MyApplication;
import com.shenma.yueba.baijia.activity.BaijiaProductInfoActivity;
import com.shenma.yueba.baijia.activity.BrandListActivity;
import com.shenma.yueba.baijia.activity.MarketMainActivity;
import com.shenma.yueba.baijia.activity.SearchBrandListActivity;
import com.shenma.yueba.baijia.activity.ShopMainActivity;
import com.shenma.yueba.baijia.modle.BrandInfo;
import com.shenma.yueba.baijia.modle.IndexProductInfo;
import com.shenma.yueba.baijia.modle.newmodel.IndexItems;
import com.shenma.yueba.util.AbsBrandListManager;
import com.shenma.yueba.util.AutoBrandListManager;
import com.shenma.yueba.util.AverageViewManager;
import com.shenma.yueba.util.PerferneceUtil;
import com.shenma.yueba.util.ToolsUtil;
import com.shenma.yueba.view.RoundImageView;

import java.util.ArrayList;
import java.util.List;

import config.PerferneceConfig;

/**
 * Created by Administrator on 2015/11/6.
 * 主页 列表的 item
 */
public class HomeAdapter extends BaseAdapter {

    Activity activity;
    List<IndexItems> infoList = new ArrayList<IndexItems>();


    public HomeAdapter(Activity activity, List<IndexItems> arraylist) {
        this.activity = activity;
        this.infoList = arraylist;
    }

    @Override
    public int getCount() {
        return infoList.size();
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
        Holder holder;
        if (convertView == null) {
            holder = new Holder();
            convertView = LayoutInflater.from(activity).inflate(R.layout.home_item_layout, null);
            //初始化 商品图片的高度
            initImageHeight(holder, position, convertView);
            holder.home_item_top_layout_include = (RelativeLayout) convertView.findViewById(R.id.home_item_top_layout_include);
            holder.home_item_top_layout_include.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    IndexItems _indexItems=(IndexItems)v.getTag();
                    forwardMarketActivity(_indexItems.getStoreId());

                }
            });
            holder.home_item_top_desp_imageview = (ImageView) convertView.findViewById(R.id.home_item_top_desp_imageview);

            holder.home_item_top_desp_textview = (TextView) convertView.findViewById(R.id.home_item_top_desp_textview);
            holder.home_item_top_destance_textview = (TextView) convertView.findViewById(R.id.home_item_top_destance_textview);
            holder.home_item_top_layout_icon_customimageview = (ImageView) convertView.findViewById(R.id.home_item_top_layout_icon_customimageview);
            holder.home_item_top_layout_time_textview = (TextView) convertView.findViewById(R.id.home_item_top_layout_time_textview);
            holder.home_item_top_name_textview = (TextView) convertView.findViewById(R.id.home_item_top_name_textview);
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }
        //设置品牌信息
        initBrnadView(position, convertView);
        //设置通用信息
        setCommonValue(position, holder);
        //设置商品信息
        setProductValue(position, holder.averageManager.getChildView());
        Log.i("TAG", "getView ---------------------------------------------->>>>position" + position);
        return convertView;
    }


    void setCommonValue(int position, final Holder holder) {
        final IndexItems indexItems = infoList.get(position);
        holder.home_item_top_name_textview.setText(indexItems.getStoreName());
        MyApplication.getInstance().getImageLoader().displayImage(ToolsUtil.nullToString(indexItems.getLogo()), holder.home_item_top_layout_icon_customimageview, MyApplication.getInstance().getDisplayImageOptions());
        holder.home_item_top_layout_include.setTag(indexItems);
        //如果是认证买手
        if (indexItems.getStoreLeave().equals("8")) {
            holder.home_item_top_desp_textview.setText(ToolsUtil.nullToString(indexItems.getDescription()));
            holder.home_item_top_desp_imageview.setVisibility(View.GONE);
            holder.home_item_top_destance_textview.setText("");
            holder.home_item_top_destance_textview.setVisibility(View.GONE);

        } else {

            holder.home_item_top_desp_imageview.setVisibility(View.VISIBLE);
            holder.home_item_top_desp_textview.setText(ToolsUtil.nullToString(indexItems.getLocation()));
            double long1=indexItems.getLon();
            double lat1=indexItems.getLat();
            double long2=0;
            double lat2=0;
            if(PerferneceUtil.getString(PerferneceConfig.LONGITUDE)!=null  && !PerferneceUtil.getString(PerferneceConfig.LONGITUDE).equals(""))
            {
                long2=Double.parseDouble(PerferneceUtil.getString(PerferneceConfig.LONGITUDE));
            }
            if(PerferneceUtil.getString(PerferneceConfig.LATITUDE)!=null  && !PerferneceUtil.getString(PerferneceConfig.LATITUDE).equals(""))
            {
                long2=Double.parseDouble(PerferneceUtil.getString(PerferneceConfig.LATITUDE));
            }
            holder.home_item_top_destance_textview.setText(ToolsUtil.Distance(long1, lat1, long2, lat2));
            holder.home_item_top_destance_textview.setVisibility(View.VISIBLE);
        }

        holder.home_item_top_layout_time_textview.setTag(indexItems);
        if(indexItems.isDayangGou())
        {
            holder.home_item_top_layout_time_textview.setText("剩余结束时间："+indexItems.getShowstr());
        }else
        {
            holder.home_item_top_layout_time_textview.setText("剩余开始时间："+indexItems.getShowstr());
        }

        indexItems.setTimerLinstener(new IndexItems.TimerLinstener() {
            @Override
            public void timerCallBack() {
                if(activity!=null)
                {
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            //倒计时
                            IndexItems _indexItems = (IndexItems) holder.home_item_top_layout_time_textview.getTag();
                            if(_indexItems.isDayangGou())
                            {
                                holder.home_item_top_layout_time_textview.setText("剩余结束时间："+_indexItems.getShowstr());
                            }else
                            {
                                holder.home_item_top_layout_time_textview.setText("剩余开始时间："+_indexItems.getShowstr());
                            }
                        }
                    });
                }
            }
        });


    }

    /**********
     * 设置品牌信息
     ****/
    void initBrnadView(final int position, final View view) {
        final IndexItems indexItems = infoList.get(position);
        LinearLayout home_item_layout_brand_linearlayout = (LinearLayout) view.findViewById(R.id.home_item_layout_brand_linearlayout);
        AbsBrandListManager bm = new AutoBrandListManager(activity, home_item_layout_brand_linearlayout);
        bm.settextSize(R.dimen.text_authentication_textsize);
        bm.setChildMargin(activity.getResources().getDimensionPixelSize(R.dimen.branditem_margin));
        bm.setLastText("更多品牌", R.dimen.text_authentication_textsize);
        bm.setOnClickListener(new AbsBrandListManager.OnBrandItemListener() {
            @Override
            public void onItemClick(View v, int i) {
                BrandInfo brandInfo = indexItems.getBrands().get(i);
                Intent intent = new Intent(activity, BrandListActivity.class);
                intent.putExtra("StoreId", indexItems.getStoreId());
                intent.putExtra("BrandName", brandInfo.getBrandName());
                intent.putExtra("BrandId", brandInfo.getBrandId());
                activity.startActivity(intent);
            }

            @Override
            public void OnLastItemClick(View v) {
                Intent intent = new Intent(activity, SearchBrandListActivity.class);
                intent.putExtra("StoreId", indexItems.getStoreId());
                activity.startActivity(intent);
            }
        });

        List<String> str_array = new ArrayList<String>();
        if (indexItems.getBrands() != null) {
            for (int i = 0; i < indexItems.getBrands().size(); i++) {
                str_array.add(ToolsUtil.nullToString(indexItems.getBrands().get(i).getBrandName()));
            }
        }

        if(indexItems.getBrands()==null || indexItems.getBrands().size()==0)
        {
            home_item_layout_brand_linearlayout.setVisibility(View.GONE);
        }else
        {
            home_item_layout_brand_linearlayout.setVisibility(View.VISIBLE);
            bm.nofication(str_array);
        }

    }


    /*********
     * 目的：将图片 设置为 正方形 （根据 其 父类的宽度 除以 显示的个数 以及margin 和 padding 计算）
     *********/
    void initImageHeight(Holder holder, final int position, final View view) {
        final LinearLayout recommendproduct_linearlayout = (LinearLayout) view.findViewById(R.id.recommendproduct_linearlayout);
        int margin = activity.getResources().getDimensionPixelOffset(R.dimen.branditem_margin);
        holder.averageManager = new AverageViewManager(activity, R.layout.home_item_img_layout, R.id.authentication_chid_item_layout_include, 3, recommendproduct_linearlayout, margin, new AverageViewManager.InflaterSucessListener() {
            @Override
            public void returnChildListView(List<View> view_array) {
                setProductValue(position, view_array);
            }
        });
    }

    /******
     * 设置商品信息
     ***/
    void setProductValue(int position, List<View> view_array) {

        final IndexItems indexItems = infoList.get(position);
        List<IndexProductInfo> product_list = indexItems.getProducts();
        if (product_list == null || product_list.size() == 0) {
            for (int i = 0; i < view_array.size(); i++) {
                view_array.get(i).setVisibility(View.GONE);
            }
        } else {
            for (int i = 0; i < view_array.size(); i++) {
                view_array.get(i).setVisibility(View.VISIBLE);
                //根据级别设置 是否显示 导购头像
                View daogouview=view_array.get(i).findViewById(R.id.home_item_pic1_include);
                //如果是认证
                if(indexItems.getStoreLeave().equals("8"))
                {
                    daogouview.setVisibility(View.VISIBLE);
                }else
                {
                    daogouview.setVisibility(View.GONE);
                }

                if (i < product_list.size()) {
                    IndexProductInfo product = product_list.get(i);
                    //view_array.get(i).setVisibility(View.VISIBLE);
                    //商品图片
                    ImageView authentication_child_iten_layout_pic_imageview = (ImageView) view_array.get(i).findViewById(R.id.authentication_child_iten_layout_pic_imageview);
                    String url = ToolsUtil.nullToString(product.getPic());
                    authentication_child_iten_layout_pic_imageview.setTag(product);
                    MyApplication.getInstance().getImageLoader().displayImage(url, authentication_child_iten_layout_pic_imageview, MyApplication.getInstance().getDisplayImageOptions());
                    authentication_child_iten_layout_pic_imageview.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            IndexProductInfo product = (IndexProductInfo) v.getTag();
                            Intent intent = new Intent(activity, BaijiaProductInfoActivity.class);
                            intent.putExtra("productID", Integer.valueOf(product.getProductId()));
                            activity.startActivity(intent);
                        }
                    });
                    //价格
                    TextView authentication_child_iten_layout_price_textview = (TextView) view_array.get(i).findViewById(R.id.authentication_child_iten_layout_price_textview);
                    authentication_child_iten_layout_price_textview.setText("￥"+product.getPrice() );
                    //旧的价格
                    TextView authentication_child_iten_layout_oldprice_textview = (TextView) (TextView) view_array.get(i).findViewById(R.id.authentication_child_iten_layout_oldprice_textview);
                    authentication_child_iten_layout_oldprice_textview.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
                    authentication_child_iten_layout_oldprice_textview.setText("￥"+product.getUnitPrice());

                    //头像信息
                    LinearLayout home_item_pic1_include = (LinearLayout) view_array.get(i).findViewById(R.id.home_item_pic1_include);
                    //如果是 认证 则显示 导购头像  否则 隐藏
                    if (indexItems.getStoreLeave().equals("8")) {
                        home_item_pic1_include.setVisibility(View.VISIBLE);
                    } else {
                        home_item_pic1_include.setVisibility(View.GONE);
                    }

                    //导购图片
                    RoundImageView gudie_item_layout_roundimageview = (RoundImageView) home_item_pic1_include.findViewById(R.id.gudie_item_layout_roundimageview);
                    String buyerPic = ToolsUtil.nullToString(product.getUserLogo());
                    gudie_item_layout_roundimageview.setTag(product);
                    MyApplication.getInstance().getImageLoader().displayImage(buyerPic, gudie_item_layout_roundimageview, MyApplication.getInstance().getDisplayImageOptions());
                    home_item_pic1_include.setTag(product);
                    home_item_pic1_include.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if(MyApplication.getInstance().isUserLogin(activity))
                            {
                                IndexProductInfo product = (IndexProductInfo) v.getTag();
                                Intent intent = new Intent(activity, ShopMainActivity.class);
                                intent.putExtra("userID", Integer.valueOf(product.getBuyerId()));
                                activity.startActivity(intent);
                            }
                        }
                    });
                    //导购信息1
                    TextView gudie_item_layout_name1_textview = (TextView) home_item_pic1_include.findViewById(R.id.gudie_item_layout_name1_textview);
                    gudie_item_layout_name1_textview.setText(ToolsUtil.nullToString(product.getBuyerName()));
                    //导购信息2
                    TextView gudie_item_layout_name2_textview = (TextView) home_item_pic1_include.findViewById(R.id.gudie_item_layout_name2_textview);
                    gudie_item_layout_name2_textview.setText(ToolsUtil.nullToString(product.getBrandName()));

                } else {
                    view_array.get(i).setVisibility(View.INVISIBLE);
                }

            }
        }
    }

    class Holder {
        AverageViewManager averageManager;
        //顶部 商场或买手信息的 父视图
        RelativeLayout home_item_top_layout_include;
        //头像
        ImageView home_item_top_layout_icon_customimageview;
        //倒计时时间
        TextView home_item_top_layout_time_textview;
        //头像名称
        TextView home_item_top_name_textview;
        //定位图片
        ImageView home_item_top_desp_imageview;
        //地址或描述
        TextView home_item_top_desp_textview;
        //距离
        TextView home_item_top_destance_textview;
    }

    void forwardMarketActivity(String StoreId) {
        Intent intent = new Intent(activity, MarketMainActivity.class);
        intent.putExtra("StoreId",StoreId);
        activity.startActivity(intent);
    }
}
