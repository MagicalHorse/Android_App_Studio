package com.shenma.yueba.models;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.text.Layout;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lidroid.xutils.bitmap.BitmapDisplayConfig;
import com.lidroid.xutils.bitmap.callback.BitmapLoadCallBack;
import com.lidroid.xutils.bitmap.callback.BitmapLoadFrom;
import com.shenma.yueba.R;
import com.shenma.yueba.application.MyApplication;
import com.shenma.yueba.utils.ImageLoaderManager;
import com.shenma.yueba.utils.ToolsUtil;
import com.shenma.yueba.views.CustomImageView;
import com.shenma.yueba.views.RoundImageView;

import java.util.List;

/**
 * Created by Administrator on 2015/11/9.
 */
public abstract class Abs_HomeItemInfo {

    //推荐图片显示最大个数
    int maxpicCount = 3;
    //推荐品牌显示 最大个数
    int maxBrandCount = 5;
    //圆角头像
    CustomImageView home_item_top_layout_icon_customimageview;
    //名称
    TextView home_item_top_name_textview;
    //描述信息
    TextView home_item_top_desp_textview;
    //地址图片
    ImageView home_item_top_desp_imageview;
    //距离
    TextView home_item_top_destance_textview;
    //推荐图片的父类
    LinearLayout recommendproduct_linearlayout;
    //品牌列表
    LinearLayout brandlist_linearlayout;
    //倒计时
    TextView home_item_top_layout_time_textview;
    Context context;

    /******
     * 外部调用
     *****/
    public void setParetView(View view, Context context) {
        this.context = context;
        View contextview=getContextView(view);
        LinearLayout home_item_layout_include1 = (LinearLayout) view.findViewById(R.id.home_item_layout_include1);
        LinearLayout home_item_layout_include2 = (LinearLayout) view.findViewById(R.id.home_item_layout_include2);
        setHiddenView(home_item_layout_include1, home_item_layout_include2);
        initView(contextview);
        setvalue(view);
    }

    abstract void setvalue(View v);
    abstract View getContextView(View view);


    void initView(View view) {
        //圆角头像
        home_item_top_layout_icon_customimageview = (CustomImageView) view.findViewById(R.id.home_item_top_layout_icon_customimageview);
        //倒计时
        home_item_top_layout_time_textview = (TextView) view.findViewById(R.id.home_item_top_layout_time_textview);
        //名称
        home_item_top_name_textview = (TextView) view.findViewById(R.id.home_item_top_name_textview);
        //描述信息
        home_item_top_desp_textview = (TextView) view.findViewById(R.id.home_item_top_desp_textview);
        //地址图片
        home_item_top_desp_imageview = (ImageView) view.findViewById(R.id.home_item_top_desp_imageview);
        //距离
        home_item_top_destance_textview = (TextView) view.findViewById(R.id.home_item_top_destance_textview);
        //推荐图片的父类
        recommendproduct_linearlayout = (LinearLayout) view.findViewById(R.id.recommendproduct_linearlayout);
        //品牌
        brandlist_linearlayout = (LinearLayout) view.findViewById(R.id.brandlist_linearlayout);
        //倒计时
        home_item_top_layout_time_textview=(TextView)view.findViewById(R.id.home_item_top_layout_time_textview);
    }

    /******
     * 设置圆角图片地址
     *
     * @param imgurl String 图片地址
     ****/
    public void setRoundImage(String imgurl) {
        if (home_item_top_layout_icon_customimageview != null) {
            initPic(home_item_top_layout_icon_customimageview, imgurl);
        }
    }

    /******
     * 设置 商场名称/品牌名称/认证买手
     *
     * @param str String 名称
     ****/
    public void setMainname(String str) {
        if (home_item_top_name_textview != null) {
            home_item_top_name_textview.setText(ToolsUtil.nullToString(str));
        }
    }

    /******
     * 设置地址/品牌介绍/认证买上介绍
     *
     * @param str String 名称
     ****/
    public void setMainDesc(String str) {
        if (home_item_top_desp_textview != null) {
            home_item_top_desp_textview.setText(ToolsUtil.nullToString(str));
        }
    }

    /******
     * 设置地址图标是否显示
     *
     * @param b boolean true 显示 false 隐藏
     ****/
    public void setIsAddressShow(boolean b) {
        if (home_item_top_desp_imageview != null) {
            if (b) {
                home_item_top_desp_imageview.setVisibility(View.VISIBLE);
            } else {
                home_item_top_desp_imageview.setVisibility(View.GONE);
            }
        }
    }


    /******
     * 设置距离信息
     *
     * @param str String 距离
     * @param b   boolean true 显示 false 隐藏
     ****/
    public void setDistance(String str, boolean b) {
        if (home_item_top_destance_textview != null) {
            if (b) {
                home_item_top_destance_textview.setText(ToolsUtil.nullToString(str));
                home_item_top_destance_textview.setVisibility(View.VISIBLE);
            } else {
                home_item_top_destance_textview.setText("");
                home_item_top_destance_textview.setVisibility(View.GONE);
            }
        }
    }

    /**************
     * 设置品牌
     ************/
    public void setBrand(List list)
    {
        if(brandlist_linearlayout!=null)
        {
            if(brandlist_linearlayout.getChildCount()>0)
            {
                return;
            }
            brandlist_linearlayout.removeAllViews();
            //数据显示的个数
            int showDataCount=0;
            //如果 最大个数 大于或等于 当前的 size 则 最大数=size
            if((maxBrandCount-1)>=list.size())
            {
                showDataCount=list.size();
            }else {

                showDataCount=maxBrandCount;
            }
            int screenWidth=ToolsUtil.getDisplayWidth(context);
            int marginvalue = context.getResources().getDimensionPixelOffset(R.dimen.branditem_margin);
            int width=screenWidth-2*marginvalue;
            int childWidth=width/maxBrandCount-2*marginvalue;
            LinearLayout.LayoutParams params=new LinearLayout.LayoutParams(childWidth,LinearLayout.LayoutParams.WRAP_CONTENT);
            params.leftMargin=marginvalue;
            params.rightMargin=marginvalue;
            for(int i=0;i<showDataCount;i++)
            {

                TextView tv=new TextView(context);
                //tv.setBackgroundColor(context.getResources().getColor(R.color.color_deeoyellow));
                tv.setSingleLine();
                tv.setEllipsize(TextUtils.TruncateAt.END);
                tv.setGravity(Gravity.CENTER);
                if(i==4)
                {
                    tv.setText("更多品牌");
                }else
                {
                    tv.setText("1111"+i);
                }
                brandlist_linearlayout.addView(tv, params);
                tv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });
            }
        }
    }

    /******
     * 倒计时
     * @param  time String 初始时间
     * ********/
    public void setCountDown(String time)
    {
        if(home_item_top_layout_time_textview!=null)
        {
            home_item_top_layout_time_textview.setText(time);
        }
    }


    /**************
     * 设置推荐商品 信息
     * @param  b boolean true 显示  false 隐藏
     ************/
    public void setRecommendProduct(List list,boolean b) {

        if (recommendproduct_linearlayout != null) {
            if(recommendproduct_linearlayout.getChildCount()>0)
            {
                return;
            }
            recommendproduct_linearlayout.removeAllViews();
            if (maxpicCount > list.size()) {
                maxpicCount = list.size();
            }

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            for (int i = 0; i < maxpicCount; i++) {
                View child = LayoutInflater.from(context).inflate(R.layout.home_item_img_layout, null);
                LinearLayout home_item_pic1_include=(LinearLayout)child.findViewById(R.id.home_item_pic1_include);
                if(b)
                {
                    home_item_pic1_include.setVisibility(View.VISIBLE);
                }else
                {
                    home_item_pic1_include.setVisibility(View.GONE);
                }
                int marginvalue=context.getResources().getDimensionPixelOffset(R.dimen.item_margin);
                params.leftMargin=marginvalue;
                params.rightMargin=marginvalue;
                initRecommendProduct(list.get(i), child);
                recommendproduct_linearlayout.addView(child, params);
            }
        }

    }


    /**************
     * 设置推荐商品赋值
     ************/
    void initRecommendProduct(Object obj,View view) {
        int screenWidth=ToolsUtil.getDisplayWidth(context);
        int marginvalue=context.getResources().getDimensionPixelOffset(R.dimen.item_margin);
        int width=screenWidth-2*marginvalue;
        int childWidth=width/maxpicCount-(marginvalue*2);
        int childHeight=childWidth;
        ImageView home_item_pic1_imageview=(ImageView)view.findViewById(R.id.home_item_pic1_imageview);
        LinearLayout.LayoutParams params=(LinearLayout.LayoutParams)home_item_pic1_imageview.getLayoutParams();
        params.height=childHeight;
        params.width=childWidth;
        initdata(obj,view);
        home_item_pic1_imageview.setLayoutParams(params);


    }

    void initdata(Object obj,View view)
    {
        //推荐图片
        ImageView home_item_pic1_imageview=(ImageView)view.findViewById(R.id.home_item_pic1_imageview);
        String imgurl="http://f.hiphotos.baidu.com/image/pic/item/ac4bd11373f08202f7fce43e49fbfbedab641b40.jpg";
        initPic(home_item_pic1_imageview,imgurl);
        home_item_pic1_imageview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        //圆形图像
        CustomImageView gudie_item_layout_customimageview=(CustomImageView)view.findViewById(R.id.gudie_item_layout_customimageview);
        initPic(gudie_item_layout_customimageview,imgurl);
        gudie_item_layout_customimageview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        //导购名称
        TextView gudie_item_layout_name1_textview=(TextView)view.findViewById(R.id.gudie_item_layout_name1_textview);
        //距离/品牌名
        TextView gudie_item_layout_name2_textview=(TextView)view.findViewById(R.id.gudie_item_layout_name2_textview);
    }



    public void setHiddenView(View... view) {
        for (int i = 0; i < view.length; i++) {
            View v = view[i];
            if (v != null) {
                v.setVisibility(View.GONE);
            }
        }
    }

    void setCommonView(View v) {

    }

    void initPic(View v,String url)
    {
        setpicBitmap(v, null);
        ImageLoaderManager.getInstace().display(v, url, new BitmapLoadCallBack() {
            @Override
            public void onLoadCompleted(View view, String s, Bitmap bitmap, BitmapDisplayConfig bitmapDisplayConfig, BitmapLoadFrom bitmapLoadFrom) {
                setpicBitmap(view, bitmap);
            }

            @Override
            public void onLoadFailed(View view, String s, Drawable drawable) {
                setpicBitmap(view, null);
            }
        });
    }

    void setpicBitmap(View v,Bitmap bitmap)
    {
        if(v instanceof  ImageView)
        {
            if(bitmap==null)
            {
                ((ImageView) v).setImageResource(R.mipmap.default_pic);
            }else
            {
                ((ImageView) v).setImageBitmap(bitmap);
            }

        }else if( v instanceof CustomImageView)
        {
            ((CustomImageView)v).setBitmap(bitmap);
        }
    }

}
