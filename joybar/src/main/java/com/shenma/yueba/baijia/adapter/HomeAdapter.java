package com.shenma.yueba.baijia.adapter;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Paint;
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
import com.shenma.yueba.baijia.activity.ApproveBuyerDetailsActivity;
import com.shenma.yueba.baijia.activity.MarketMainActivity;
import com.shenma.yueba.baijia.activity.SearchBrandListActivity;
import com.shenma.yueba.baijia.modle.newmodel.Abs_HomeItemInfo;
import com.shenma.yueba.util.AbsBrandListManager;
import com.shenma.yueba.util.AutoBrandListManager;
import com.shenma.yueba.util.AverageViewManager;
import com.shenma.yueba.util.TimerDownUtils;
import com.shenma.yueba.view.RoundImageView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2015/11/6.
 * 主页 列表的 item
 */
public class HomeAdapter extends BaseAdapter {
    Activity activity;
    List<Abs_HomeItemInfo> infoList = new ArrayList<Abs_HomeItemInfo>();


    public HomeAdapter(Activity activity) {
        this.activity = activity;
        for (int i = 0; i < 10; i++) {
            Abs_HomeItemInfo abs_HomeItemInfo = new Abs_HomeItemInfo();
            abs_HomeItemInfo.setStartTime(9000+(5000*i));
            abs_HomeItemInfo.setEndTime(9000 + (5000 * i));
            abs_HomeItemInfo.setHasStarted(false);
            abs_HomeItemInfo.startTime();
            infoList.add(abs_HomeItemInfo);
        }
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
                    forwardActivity();
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
        return convertView;
    }


    void setCommonValue(int position,final Holder holder) {
        Abs_HomeItemInfo abs_HomeItemInfo= infoList.get(position);
        holder.home_item_top_name_textview.setText("商场/买手的名字" + position);
        holder.home_item_top_desp_textview.setText("地址或描述信息" + position);
        holder.home_item_top_destance_textview.setText("距离信息" + position);
        holder.home_item_top_layout_time_textview.setTag(abs_HomeItemInfo);
        holder.home_item_top_layout_time_textview.setText(abs_HomeItemInfo.getShowstr());
        abs_HomeItemInfo.setTimerCallListener(new Abs_HomeItemInfo.HomeItemInfoListener() {
            @Override
            public void callback() {
                //倒计时
                Abs_HomeItemInfo _abs_HomeItemInfo=(Abs_HomeItemInfo)holder.home_item_top_layout_time_textview.getTag();
                holder.home_item_top_layout_time_textview.setText(_abs_HomeItemInfo.getShowstr());
            }
        });


    }

    /**********
     * 设置品牌信息
     ****/
    void initBrnadView(final int position, final View view) {
        LinearLayout home_item_layout_brand_linearlayout = (LinearLayout) view.findViewById(R.id.home_item_layout_brand_linearlayout);
        AbsBrandListManager bm = new AutoBrandListManager(activity, home_item_layout_brand_linearlayout);
        bm.settextSize(R.dimen.text_authentication_textsize);
        bm.setChildMargin(activity.getResources().getDimensionPixelSize(R.dimen.branditem_margin));
        bm.setLastText("更多品牌", R.dimen.text_authentication_textsize);
        bm.setOnClickListener(new AbsBrandListManager.OnBrandItemListener() {
            @Override
            public void onItemClick(View v, int i) {
                MyApplication.getInstance().showMessage(activity, "position=" + position + "  i=" + i);
            }

            @Override
            public void OnLastItemClick(View v) {
                Intent intent=new Intent(activity, SearchBrandListActivity.class);
                activity.startActivity(intent);
                MyApplication.getInstance().showMessage(activity, "更多");
            }
        });
        List<String> str_array = new ArrayList<String>();
        for (int i = 0; i < 10; i++) {
            str_array.add("品牌" + position + "-i" + i);
        }
        bm.nofication(str_array);
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

        for (int i = 0; i < view_array.size(); i++) {
            //商品图片
            ImageView authentication_child_iten_layout_pic_imageview = (ImageView) view_array.get(i).findViewById(R.id.authentication_child_iten_layout_pic_imageview);
            authentication_child_iten_layout_pic_imageview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(activity, ApproveBuyerDetailsActivity.class);
                    intent.putExtra("productID",12985);
                    activity.startActivity(intent);
                    MyApplication.getInstance().showMessage(activity, "图片的点击事件");
                }
            });
            //价格
            TextView authentication_child_iten_layout_price_textview = (TextView) view_array.get(i).findViewById(R.id.authentication_child_iten_layout_price_textview);

            //旧的价格
            TextView authentication_child_iten_layout_oldprice_textview = (TextView) (TextView) view_array.get(i).findViewById(R.id.authentication_child_iten_layout_oldprice_textview);
            authentication_child_iten_layout_oldprice_textview.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);

            //头像信息
            LinearLayout home_item_pic1_include = (LinearLayout) view_array.get(i).findViewById(R.id.home_item_pic1_include);
            //商品图片
            RoundImageView gudie_item_layout_roundimageview = (RoundImageView) home_item_pic1_include.findViewById(R.id.gudie_item_layout_roundimageview);
            home_item_pic1_include.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MyApplication.getInstance().showMessage(activity, "导购点击事件");
                }
            });
            //导购信息1
            TextView gudie_item_layout_name1_textview = (TextView) home_item_pic1_include.findViewById(R.id.gudie_item_layout_name1_textview);
            //导购信息2
            TextView gudie_item_layout_name2_textview = (TextView) home_item_pic1_include.findViewById(R.id.gudie_item_layout_name2_textview);

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

    void forwardActivity() {
        Intent intent = new Intent(activity, MarketMainActivity.class);
        activity.startActivity(intent);
    }
}
