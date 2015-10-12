package com.shenma.yueba.baijia.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.animation.AccelerateInterpolator;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.shenma.yueba.R;
import com.shenma.yueba.application.MyApplication;
import com.shenma.yueba.baijia.adapter.ProductColorTypeAdapter;
import com.shenma.yueba.baijia.adapter.ScrollViewPagerAdapter;
import com.shenma.yueba.baijia.modle.LikeUsersInfoBean;
import com.shenma.yueba.baijia.modle.ProductColorTypeBean;
import com.shenma.yueba.baijia.modle.ProductsDetailsInfoBean;
import com.shenma.yueba.baijia.modle.ProductsDetailsPromotion;
import com.shenma.yueba.baijia.modle.ProductsDetailsTagInfo;
import com.shenma.yueba.baijia.modle.ProductsDetailsTagsInfo;
import com.shenma.yueba.baijia.modle.RequestProductDetailsInfoBean;
import com.shenma.yueba.baijia.modle.UsersInfoBean;
import com.shenma.yueba.util.FontManager;
import com.shenma.yueba.util.HttpControl;
import com.shenma.yueba.util.HttpControl.HttpCallBackInterface;
import com.shenma.yueba.util.ToolsUtil;
import com.shenma.yueba.view.FixedSpeedScroller;
import com.shenma.yueba.view.MyGridView;
import com.shenma.yueba.view.RoundImageView;
import com.shenma.yueba.view.TagImageView;
import com.umeng.analytics.MobclickAgent;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * @author gyj
 * @version 创建时间：2015-5-20 下午6:02:36 程序的简单说明 定义认证买手 商品详情页
 */

@SuppressLint("NewApi")
public class ApproveBuyerDetails_ck_Activity extends Activity implements OnClickListener {
    // 当前选中的id （ViewPager选中的id）
    int currid = -1;
    // 滚动图片
    ViewPager appprovebuyer_viewpager;
    // 滚动图像下面的 原点
    LinearLayout appprovebuyer_viewpager_footer_linerlayout;

    // 滚动视图 主要内容
    ScrollView approvebuyerdetails_srcollview;
    // 底部购物车父视图
    LinearLayout approvebuyerdetails_footer;
    TextView approvebuyerdetails_closeingtime_textview;// 打烊时间
    TextView approvebuyerdetails_closeinginfo_textview;// 打烊信息
    int childWidth = 0;
    int maxcount = 8;
    ScrollViewPagerAdapter customPagerAdapter;
    // 商品id;
    int productID = -1;
    HttpControl httpControl = new HttpControl();
    RelativeLayout appprovebuyer_viewpager_relativelayout;
    // 商品信息对象
    ProductsDetailsInfoBean Data;
    // 头像
    RoundImageView approvebuyerdetails_icon_imageview;
    // 私聊
    TextView approvebuyerdetails_layout_siliao_linerlayout_textview;
    TextView approvebuyerdetails_layout_shoucang_linerlayout_textview;//收藏

    // 加入购物车
    Button approvebuyer_addcartbutton;
    // 直接购买
    Button approvebuyerbuybutton;
    List<View> viewlist = new ArrayList<View>();
    LinearLayout approvebuyerdetails_closeingtime_linearlayout;
    Timer timer;
    RequestProductDetailsInfoBean bean;
    LinearLayout ll_attentionpeople_contener;
    MyGridView product_spec_layout_colortype_mygridview;//颜色的分类
    ProductColorTypeAdapter productColorTypeAdapter;//颜色分类的 适配器
    List<ProductColorTypeBean>  colortypelist=new ArrayList<ProductColorTypeBean>();//颜色分类对象

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        MyApplication.getInstance().addActivity(this);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.approvebuyerdetails_ck_layout);
        productID = this.getIntent().getIntExtra("productID", -1);
        if (productID < 0) {
            MyApplication.getInstance().showMessage(this, "数据错误,请重试");
            this.finish();
            return;
        }
        // productID = 12947;
        initViews();
        initData();
        setFont();
    }

    private void initViews() {
        TextView tv_top_left = (TextView) findViewById(R.id.tv_top_left);
        tv_top_left.setVisibility(View.VISIBLE);
        tv_top_left.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                ApproveBuyerDetails_ck_Activity.this.finish();
            }
        });
        TextView tv_top_right = (TextView) findViewById(R.id.tv_top_right);
        tv_top_right.setVisibility(View.VISIBLE);
        tv_top_right.setBackground(getResources().getDrawable(R.drawable.productshare));
        tv_top_right.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!MyApplication.getInstance().isUserLogin(ApproveBuyerDetails_ck_Activity.this)) {
                    return;
                }

                if (bean != null) {
                    ProductsDetailsInfoBean productinfobean = bean.getData();
                    if (productinfobean != null) {
                        String content = ToolsUtil.nullToString(productinfobean.getShareDesc());
                        String url = productinfobean.getShareLink();
                        List<ProductsDetailsTagInfo> piclist = productinfobean.getProductPic();
                        String img_name = "";
                        if (piclist.size() > 0) {
                            img_name = piclist.get(0).getLogo();
                        }
                        String icon = ToolsUtil.getImage(ToolsUtil.nullToString(img_name), 320, 0);
                        ToolsUtil.shareUrl(ApproveBuyerDetails_ck_Activity.this, productinfobean.getProductId(), "", content, url, icon);
                    }
                }
            }
        });
        // 收藏按钮
        approvebuyerdetails_layout_shoucang_linerlayout_textview = (TextView) findViewById(R.id.approvebuyerdetails_layout_shoucang_linerlayout_textview);
        //头像包裹视图
        ll_attentionpeople_contener = (LinearLayout) findViewById(R.id.ll_attentionpeople_contener);
        // 活动父视图
        approvebuyerdetails_closeingtime_linearlayout = (LinearLayout) findViewById(R.id.approvebuyerdetails_closeingtime_linearlayout);
        // 打烊时间
        approvebuyerdetails_closeingtime_textview = (TextView) findViewById(R.id.approvebuyerdetails_closeingtime_textview);
        // 打烊信息
        approvebuyerdetails_closeinginfo_textview = (TextView) findViewById(R.id.approvebuyerdetails_closeinginfo_textview);
        approvebuyerdetails_footer = (LinearLayout) findViewById(R.id.approvebuyerdetails_footer);
        approvebuyerdetails_srcollview = (ScrollView) findViewById(R.id.approvebuyerdetails_srcollview);

        appprovebuyer_viewpager_footer_linerlayout = (LinearLayout) findViewById(R.id.appprovebuyer_viewpager_footer_linerlayout);
        appprovebuyer_viewpager = (ViewPager) findViewById(R.id.appprovebuyer_viewpager);
        appprovebuyer_viewpager_relativelayout = (RelativeLayout) findViewById(R.id.appprovebuyer_viewpager_relativelayout);
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
        int height = width;
        appprovebuyer_viewpager_relativelayout.setLayoutParams(new LinearLayout.LayoutParams(width, height));

        appprovebuyer_viewpager.setOnTouchListener(new OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {

                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        stopTimerToViewPager();
                        break;
                    case MotionEvent.ACTION_UP:
                        startTimeToViewPager();
                        break;
                }
                return false;
            }
        });
        appprovebuyer_viewpager
                .setOnPageChangeListener(new OnPageChangeListener() {

                    @Override
                    public void onPageSelected(int arg0) {
                        currid = arg0;
                        setcurrItem(arg0);
                    }

                    @Override
                    public void onPageScrolled(int arg0, float arg1, int arg2) {

                    }

                    @Override
                    public void onPageScrollStateChanged(int arg0) {

                    }
                });

        approvebuyerdetails_icon_imageview = (RoundImageView) findViewById(R.id.approvebuyerdetails_icon_imageview);
        approvebuyerdetails_icon_imageview.setOnClickListener(this);

        approvebuyerdetails_layout_siliao_linerlayout_textview = (TextView) findViewById(R.id.approvebuyerdetails_layout_siliao_linerlayout_textview);
        approvebuyerdetails_layout_siliao_linerlayout_textview.setOnClickListener(this);

        approvebuyer_addcartbutton = (Button) findViewById(R.id.approvebuyer_addcartbutton);
        approvebuyerbuybutton = (Button) findViewById(R.id.approvebuyerbuybutton);
        approvebuyerbuybutton.setOnClickListener(this);

        /****************
         * 颜色的 分类
         *
         * ********************/
        for(int i=0;i<10;i++)
        {
            colortypelist.add(new ProductColorTypeBean());
        }

                productColorTypeAdapter = new ProductColorTypeAdapter(ApproveBuyerDetails_ck_Activity.this, colortypelist);
        product_spec_layout_colortype_mygridview = (MyGridView) findViewById(R.id.product_spec_layout_colortype_mygridview);
        product_spec_layout_colortype_mygridview.setAdapter(productColorTypeAdapter);
        product_spec_layout_colortype_mygridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.i("TAG", "onItemSelected position=" + position);
                for(int i=0;i<colortypelist.size();i++)
                {
                    colortypelist.get(i).setIsChecked(false);
                }
                ProductColorTypeBean bean=colortypelist.get(position);
                bean.setIsChecked(true);
                if(productColorTypeAdapter!=null)
                {
                    productColorTypeAdapter.notifyDataSetChanged();
                }
            }
        });
    }

    void startChatActivity() {
        if (!MyApplication.getInstance().isUserLogin(
                ApproveBuyerDetails_ck_Activity.this)) {
            return;
        }
        if (bean != null) {
            /*Intent intent = new Intent(ApproveBuyerDetailsActivity.this,ChatActivity.class);
			intent.putExtra("Chat_NAME", bean.getData().getBuyerName());// 圈子名字
			intent.putExtra("toUser_id", bean.getData().getBuyerId());// 私聊的话需要传对方id
			intent.putExtra("DATA", bean);
			startActivity(intent);*/
            ToolsUtil.forwardChatActivity(ApproveBuyerDetails_ck_Activity.this, bean.getData().getBuyerName(), bean.getData().getBuyerId(), 0, null, bean);
        }
    }

    /***
     * 设置文本值
     *
     * @param res int 视图id
     * @param str 要显示内容 null 不进行负值
     ***/
    void setdataValue(int res, String str) {
        View v = findViewById(res);
        if (v != null && v instanceof TextView) {
            if (str != null) {
                ((TextView) v).setText(str);
            }
            FontManager.changeFonts(this, ((TextView) v));
        }
    }

    // 加载数据
    public void initData() {
        httpControl.getMyBuyerProductDetails(productID,
                new HttpCallBackInterface() {

                    @Override
                    public void http_Success(Object obj) {
                        if (obj != null
                                && obj instanceof RequestProductDetailsInfoBean) {
                            bean = (RequestProductDetailsInfoBean) obj;
                            if (bean.getData() == null) {
                                http_Fails(500, "商品信息不存在");
                                ApproveBuyerDetails_ck_Activity.this.finish();
                                return;
                            }
                            Data = bean.getData();
                            setDatValue();
                        }

                    }

                    @Override
                    public void http_Fails(int error, String msg) {
                        MyApplication.getInstance().showMessage(
                                ApproveBuyerDetails_ck_Activity.this, msg);
                        finish();
                    }
                }, ApproveBuyerDetails_ck_Activity.this);
    }

    /*****
     * 设置数据
     ****/
    void setDatValue() {

        // 自提地址
        String address = ToolsUtil.nullToString(Data.getPickAddress());
        // 成交数据
        int truncount = Data.getTurnCount();
        //好评率
        String haoping = "0%";
        ;
        // 买手头像
        String usericon = ToolsUtil.nullToString(Data.getBuyerLogo());
        // 买手昵称
        String username = ToolsUtil.nullToString(Data.getBuyerName());
        // 商品发布时间
        String creattime = Data.getCreateTime();
        // 关注人列表
        LikeUsersInfoBean usersInfoList = Data.getLikeUsers();
        // 价格
        double price = Data.getPrice();
        // 商品名称
        String productName = ToolsUtil.nullToString(Data.getProductName());
        initPic(usericon, approvebuyerdetails_icon_imageview);
        // 自提地址
        setdataValue(R.id.approvebuyerdetails_addressvalue_textview, address);

        setdataValue(R.id.approvebuyerdetails_name_textview, username);
        // 金额
        setdataValue(R.id.approvebuyerdetails_price_textview,
                "￥" + Double.toString(price));
        // 商品名称
        setdataValue(R.id.approvebuyerdetails_producename_textview, productName);
        //title名字
        TextView tv_top_title = (TextView) findViewById(R.id.tv_top_title);
        tv_top_title.setText("商品详情");
        tv_top_title.setVisibility(View.VISIBLE);


        LikeUsersInfoBean likeUsersInfoBean = Data.getLikeUsers();
        if (likeUsersInfoBean != null) {
            int linkwidt = ll_attentionpeople_contener.getWidth() - ((LinearLayout.LayoutParams) ll_attentionpeople_contener.getLayoutParams()).leftMargin - ((LinearLayout.LayoutParams) ll_attentionpeople_contener.getLayoutParams()).rightMargin;
            int item_width = linkwidt / 8;
            // 喜欢
            TextView approvebuyerdetails_attention_textview = (TextView) findViewById(R.id.approvebuyerdetails_attention_textview);
            LayoutParams tv_params = approvebuyerdetails_attention_textview.getLayoutParams();
            tv_params.height = item_width;//设置 新型图片的高度
            approvebuyerdetails_attention_textview.setLayoutParams(tv_params);

            // 收藏按钮
            approvebuyerdetails_layout_shoucang_linerlayout_textview.setOnClickListener(this);
            approvebuyerdetails_layout_shoucang_linerlayout_textview.setTag(Data);
            if (Data.isIsFavorite()) {
                approvebuyerdetails_layout_shoucang_linerlayout_textview.setText("取消收藏");
            } else {
                approvebuyerdetails_layout_shoucang_linerlayout_textview.setText("收藏");

            }
            approvebuyerdetails_layout_shoucang_linerlayout_textview.setSelected(Data.isIsFavorite());

            approvebuyerdetails_attention_textview
                    .setSelected(likeUsersInfoBean.isIsLike());
            approvebuyerdetails_attention_textview.setText(likeUsersInfoBean
                    .getCount() + "");
            approvebuyerdetails_attention_textview.setTag(Data);
            approvebuyerdetails_attention_textview.setOnClickListener(this);

            //已收藏的 用户列表
            List<UsersInfoBean> users = likeUsersInfoBean.getUsers();
            int size = 8;
            //如果当前的列表个数 超过8个 则只显示前8个
            if (size > users.size()) {
                size = users.size();
            }
            for (int i = 0; i < size; i++) {
                RoundImageView riv = new RoundImageView(ApproveBuyerDetails_ck_Activity.this);
                LayoutParams params = new LayoutParams(item_width, item_width);
                riv.setLayoutParams(params);
                if (i != 7) {
                    initPic(ToolsUtil.nullToString(users.get(i).getLogo()), riv);
                    riv.setTag(users.get(i).getUserId());
                } else {
                    riv.setTag(null);
                    riv.setBackgroundResource(R.drawable.test003);
                }
                riv.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!MyApplication.getInstance().isUserLogin(ApproveBuyerDetails_ck_Activity.this)) {
                            return;
                        }
                        if (v.getTag() == null || ((Integer) v.getTag()) <= 0) {
                            return;
                        }
                        ToolsUtil.forwardShopMainActivity(ApproveBuyerDetails_ck_Activity.this, (Integer) v.getTag());
                    }
                });

                ll_attentionpeople_contener.addView(riv);
            }
        }

        if (Data.getProductPic() != null && Data.getProductPic().size() > 0) {
            //图片和标签
            List<ProductsDetailsTagInfo> productsDetailsTagInfo_list = Data.getProductPic();
            for (int i = 0; i < productsDetailsTagInfo_list.size(); i++) {
                RelativeLayout rl = new RelativeLayout(ApproveBuyerDetails_ck_Activity.this);
                ImageView iv = new ImageView(ApproveBuyerDetails_ck_Activity.this);
                iv.setBackgroundColor(ApproveBuyerDetails_ck_Activity.this.getResources().getColor(R.color.color_lightgrey));
                iv.setScaleType(ScaleType.CENTER_CROP);
                rl.addView(iv, new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));
                TagImageView tiv = new TagImageView(ApproveBuyerDetails_ck_Activity.this);
                //tiv.setBackgroundColor(ApproveBuyerDetailsActivity.this.getResources().getColor(R.color.color_blue));
                //tiv.setAlpha(0.5f);
                List<ProductsDetailsTagsInfo> productsDetailsTagsInfo_list = productsDetailsTagInfo_list.get(i).getTags();
                if (productsDetailsTagsInfo_list != null && productsDetailsTagsInfo_list.size() > 0) {
                    for (int j = 0; j < productsDetailsTagsInfo_list.size(); j++) {
                        DisplayMetrics dm = new DisplayMetrics();
                        getWindowManager().getDefaultDisplay().getMetrics(dm);
                        int width = dm.widthPixels;
                        int height = width;
                        ProductsDetailsTagsInfo pdtinfo = productsDetailsTagsInfo_list.get(j);
                        int tagx = (int) (pdtinfo.getPosX() * width);
                        int tagy = (int) (pdtinfo.getPosY() * height);
                        tiv.addTextTagCanNotMove(ToolsUtil.nullToString(pdtinfo.getName()), tagx, tagy, pdtinfo);
                    }
                }
                rl.addView(tiv, new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));
                viewlist.add(rl);
                initPic(ToolsUtil.getImage(ToolsUtil.nullToString(productsDetailsTagInfo_list.get(i).getLogo()), 320, 0), iv);
            }
            customPagerAdapter = new ScrollViewPagerAdapter(ApproveBuyerDetails_ck_Activity.this, viewlist);
            appprovebuyer_viewpager.setAdapter(customPagerAdapter);
            setcurrItem(0);
            startTimeToViewPager();

        } else {
            appprovebuyer_viewpager_relativelayout.setVisibility(View.VISIBLE);
        }
        ProductsDetailsPromotion productsDetailsPromotion = Data.getPromotion();
        if (productsDetailsPromotion == null || !productsDetailsPromotion.isIsShow()) {
            approvebuyerdetails_closeingtime_linearlayout.setVisibility(View.GONE);

        } else if (productsDetailsPromotion.isIsShow()) {
            approvebuyerdetails_closeingtime_linearlayout.setVisibility(View.VISIBLE);
            approvebuyerdetails_closeingtime_textview.setText(ToolsUtil.nullToString(productsDetailsPromotion.getDescriptionText()));
            approvebuyerdetails_closeinginfo_textview.setText(ToolsUtil.nullToString(productsDetailsPromotion.getTipText()));
        }
        setFont();
        approvebuyerdetails_srcollview.smoothScrollTo(0, 0);
        approvebuyerbuybutton.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onResume() {

        super.onResume();
        MobclickAgent.onResume(this);
        startTimeToViewPager();
    }

    @Override
    protected void onPause() {

        super.onPause();
        MobclickAgent.onPause(this);
        stopTimerToViewPager();
    }

    /***
     * 添加原点
     *
     * @param size  int 原点的个数
     * @param value int 当前选中的tab
     **/
    void addTabImageView(int size, int value) {
        appprovebuyer_viewpager_footer_linerlayout.removeAllViews();
        ((RelativeLayout.LayoutParams) appprovebuyer_viewpager_footer_linerlayout.getLayoutParams()).bottomMargin = 40;
        if (size <= 1) {
            return;
        }
        for (int i = 0; i < size; i++) {
            View v = new View(ApproveBuyerDetails_ck_Activity.this);
            v.setBackgroundResource(R.drawable.tabround_background);
            int width = (int) ApproveBuyerDetails_ck_Activity.this.getResources()
                    .getDimension(R.dimen.shop_main_lineheight8_dimen);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    width, width);
            params.leftMargin = (int) ApproveBuyerDetails_ck_Activity.this
                    .getResources()
                    .getDimension(R.dimen.shop_main_width3_dimen);
            appprovebuyer_viewpager_footer_linerlayout.addView(v, params);
            if (i == value % size) {
                v.setSelected(true);
            } else {
                v.setSelected(false);
            }
        }
    }

    /****
     * 设置当前显示的 item
     **/
    void setcurrItem(int i) {
        appprovebuyer_viewpager.setCurrentItem(i);
        addTabImageView(viewlist.size(), i);
    }

    /****
     * 加载图片
     */
    void initPic(final String url, final ImageView iv) {
        Log.i("TAG", "URL:" + url);
        MyApplication.getInstance().getBitmapUtil().display(iv, url);
    }

    void setFont() {

        setdataValue(R.id.approvebuyerdetails_closeingtime_textview, null);
        setdataValue(R.id.product_spec_layout_colortype_textview, null);
        setdataValue(R.id.product_spec_layout_dimentype_textview, null);
        setdataValue(R.id.product_spec_layout_count_textview, null);
        setdataValue(R.id.product_spec_layout_stock_textview, null);
        setdataValue(R.id.product_spec_layout_serve_textview, null);
        setdataValue(R.id.product_spec_layout_notes_textview, null);

        setdataValue(R.id.approvebuyerdetails_closeinginfo_textview, null);
        setdataValue(R.id.tv_top_title, null);
        // 设置昵称
        setdataValue(R.id.approvebuyerdetails_name_textview, null);
        // 金额
        setdataValue(R.id.approvebuyerdetails_price_textview, null);
        // 商品名称
        setdataValue(R.id.approvebuyerdetails_producename_textview, null);
        // 提货提醒
        setdataValue(R.id.approvebuyerdatails_layout_desc1_textview, null);
        // 自提地点
        setdataValue(R.id.approvebuyerdetails_address_textview, null);
        // 自提地址
        setdataValue(R.id.approvebuyerdetails_addressvalue_textview, null);
        // 收藏
        setdataValue(R.id.approvebuyerdetails_layout_shoucang_linerlayout_textview, null);
        // 私聊
        setdataValue(R.id.approvebuyerdetails_layout_siliao_linerlayout_textview, null);
        // 喜欢人数
        setdataValue(R.id.approvebuyerdetails_attention_textview, null);
        FontManager.changeFonts(this, approvebuyer_addcartbutton);
        FontManager.changeFonts(this, approvebuyerbuybutton);

    }

    /*****
     * 启动自动滚动
     **/
    void startTimeToViewPager() {

        stopTimerToViewPager();
        if (viewlist == null || viewlist.size() <= 2) {
            return;
        }
        timer = new Timer();
        timer.schedule(new TimerTask() {

            @Override
            public void run() {
                currid++;
                ApproveBuyerDetails_ck_Activity.this.runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        setViewPagerDuration(1000);
                        setcurrItem(currid);
                    }
                });
            }
        }, 2000, 3000);
    }

    /*****
     * 停止自动滚动
     **/
    void stopTimerToViewPager() {
        setViewPagerDuration(500);
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }

    // 设置滑动速度
    void setViewPagerDuration(int value) {
        try {
            Field field = ViewPager.class.getDeclaredField("mScroller");
            field.setAccessible(true);
            FixedSpeedScroller scroller = new FixedSpeedScroller(
                    appprovebuyer_viewpager.getContext(),
                    new AccelerateInterpolator());
            field.set(appprovebuyer_viewpager, scroller);
            scroller.setmDuration(value);
        } catch (Exception e) {
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.approvebuyerdetails_icon_imageview:// 头像
                if (!MyApplication.getInstance().isUserLogin(this)) {
                    return;
                }
                ToolsUtil.forwardShopMainActivity(ApproveBuyerDetails_ck_Activity.this, bean.getData().getBuyerId());
                break;
            case R.id.approvebuyerdetails_layout_siliao_linerlayout_textview:
                startChatActivity();
                break;
            case R.id.approvebuyerbuybutton:
                startChatActivity();
                break;
            case R.id.approvebuyerdetails_attention_textview:// 喜欢或取消喜欢
                if (!MyApplication.getInstance().isUserLogin(
                        ApproveBuyerDetails_ck_Activity.this)) {
                    return;
                }
                if (v.getTag() != null
                        || v.getTag() instanceof ProductsDetailsInfoBean) {
                    ProductsDetailsInfoBean Data = (ProductsDetailsInfoBean) v
                            .getTag();
                    LikeUsersInfoBean likeUsersInfoBean = Data.getLikeUsers();
                    if (likeUsersInfoBean != null) {
                        if (likeUsersInfoBean.isIsLike()) {
                            setLikeOrUnLike(Data, 0, (TextView) v);
                        } else {
                            setLikeOrUnLike(Data, 1, (TextView) v);
                        }
                    }

                }
                break;
            case R.id.approvebuyerdetails_layout_shoucang_linerlayout_textview:
                if (!MyApplication.getInstance().isUserLogin(
                        ApproveBuyerDetails_ck_Activity.this)) {
                    return;
                }
                if (v.getTag() != null
                        && v.getTag() instanceof ProductsDetailsInfoBean) {
                    ProductsDetailsInfoBean Data = (ProductsDetailsInfoBean) v
                            .getTag();
                    if (Data != null) {
                        if (Data.isIsFavorite()) {
                            submitAttention(0, Data, v);
                        } else {
                            submitAttention(1, Data, v);
                        }
                    }

                }
                break;
        }

    }

    /*****
     * 设置喜欢 或取消喜欢
     *
     * @param bean   ProductsInfoBean 商品对象
     * @param Status int 0表示取消喜欢 1表示喜欢
     * @param v      TextView
     ***/
    void setLikeOrUnLike(final ProductsDetailsInfoBean bean, final int Status,
                         final TextView v) {
        httpControl.setLike(bean.getProductId(), Status,
                new HttpCallBackInterface() {

                    @Override
                    public void http_Success(Object obj) {
                        int count = bean.getLikeUsers().getCount();
                        switch (Status) {
                            case 0:
                                v.setSelected(false);
                                count--;
                                if (count < 0) {
                                    count = 0;
                                }
                                bean.getLikeUsers().setIsLike(false);
                                bean.getLikeUsers().setCount(count);
                                v.setText(count + "");
                                break;
                            case 1:
                                count++;
                                v.setSelected(true);
                                v.setText(count + "");
                                bean.getLikeUsers().setIsLike(true);
                                bean.getLikeUsers().setCount(count);
                                break;
                        }
                    }

                    @Override
                    public void http_Fails(int error, String msg) {
                        MyApplication.getInstance().showMessage(
                                ApproveBuyerDetails_ck_Activity.this, msg);
                    }
                }, ApproveBuyerDetails_ck_Activity.this);
    }

    /****
     * 提交收藏与取消收藏商品
     *
     * @param type              int 0表示取消收藏 1表示收藏
     * @param brandCityWideInfo BrandCityWideInfo 商品对象
     **/
    void submitAttention(final int Status, final ProductsDetailsInfoBean bean,
                         final View v) {
        httpControl.setFavor(bean.getProductId(), Status,
                new HttpCallBackInterface() {

                    @Override
                    public void http_Success(Object obj) {
                        if (v != null && v instanceof TextView) {
                            switch (Status) {
                                case 0:
                                    v.setSelected(false);
                                    ((TextView) v).setText("收藏");
                                    bean.setIsFavorite(false);
                                    break;
                                case 1:
                                    ((TextView) v).setText("取消收藏");
                                    v.setSelected(true);
                                    bean.setIsFavorite(true);
                                    break;
                            }

                        }
                    }

                    @Override
                    public void http_Fails(int error, String msg) {
                        MyApplication.getInstance().showMessage(
                                ApproveBuyerDetails_ck_Activity.this, msg);
                    }
                }, ApproveBuyerDetails_ck_Activity.this);
    }


    @Override
    protected void onDestroy() {
        MyApplication.getInstance().removeActivity(this);//加入回退栈
        super.onDestroy();
    }
}