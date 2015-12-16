package com.shenma.yueba.baijia.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.Editable;
import android.text.Selection;
import android.text.Spannable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewTreeObserver;
import android.view.animation.AccelerateInterpolator;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.shenma.yueba.ChatActivity;
import com.shenma.yueba.R;
import com.shenma.yueba.application.MyApplication;
import com.shenma.yueba.baijia.activity.ConfirmOrderForZhuanGui;
import com.shenma.yueba.baijia.adapter.ProductColorTypeAdapter;
import com.shenma.yueba.baijia.adapter.ProductSPECAdapter;
import com.shenma.yueba.baijia.adapter.ScrollViewPagerAdapter;
import com.shenma.yueba.baijia.modle.AffirmProductInfo;
import com.shenma.yueba.baijia.modle.CKProductCountDownBean;
import com.shenma.yueba.baijia.modle.CKProductDeatilsInfoBean;
import com.shenma.yueba.baijia.modle.FragmentBean;
import com.shenma.yueba.baijia.modle.LikeUsersInfoBean;
import com.shenma.yueba.baijia.modle.ProductColorTypeBean;
import com.shenma.yueba.baijia.modle.ProductSPECbean;
import com.shenma.yueba.baijia.modle.ProductsDetailsPromotion;
import com.shenma.yueba.baijia.modle.ProductsDetailsTagInfo;
import com.shenma.yueba.baijia.modle.ProductsDetailsTagsInfo;
import com.shenma.yueba.baijia.modle.RequestCKProductDeatilsInfo;
import com.shenma.yueba.baijia.modle.RequestCk_SPECDetails;
import com.shenma.yueba.baijia.modle.newmodel.PubuliuBeanInfo;
import com.shenma.yueba.baijia.view.TabViewpagerManager;
import com.shenma.yueba.util.CollectobserverManage;
import com.shenma.yueba.util.FontManager;
import com.shenma.yueba.util.HttpControl;
import com.shenma.yueba.util.HttpControl.HttpCallBackInterface;
import com.shenma.yueba.util.ToolsUtil;
import com.shenma.yueba.view.CustomViewPager;
import com.shenma.yueba.view.FixedSpeedScroller;
import com.shenma.yueba.view.MyGridView;
import com.shenma.yueba.view.RoundImageView;
import com.shenma.yueba.view.TagImageView;
import com.shenma.yueba.view.scroll.MyScrollView;

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
public class ApproveBuyerDetails_ck_Fragment extends Fragment implements OnClickListener,CollectobserverManage.ObserverListener {
    // 当前选中的id （ViewPager选中的id）
    int currid = -1;
    // 滚动图片
    ViewPager appprovebuyer_viewpager;
    // 滚动图像下面的 原点
    LinearLayout appprovebuyer_viewpager_footer_linerlayout;

    // 滚动视图 主要内容
    MyScrollView approvebuyerdetails_srcollview;
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
    CKProductDeatilsInfoBean Data;
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
    LinearLayout ll_attentionpeople_contener;
    MyGridView product_spec_layout_colortype_mygridview;//颜色的分类
    ProductColorTypeAdapter productColorTypeAdapter;//颜色分类的 适配器
    List<ProductColorTypeBean> colortypelist = new ArrayList<ProductColorTypeBean>();//颜色分类对象
    ProductColorTypeBean checked_ProductColorTypeBean = null;//选中的颜色分类
    MyGridView product_spec_layout_dimentype_mygridview;//规格分类
    ProductSPECAdapter productSPECAdapter;//规格分类的适配器
    List<ProductSPECbean> spectypelist = new ArrayList<ProductSPECbean>();//规格分类对象
    ProductSPECbean checked_ProductSPECbean = null;//选中的尺寸
    Button create_dialog_jian_button;//加
    Button create_dialog_jia_button;//减
    EditText createorder_dialog_layout_countvalue_edittext;//购买数量
    TextView product_spec_layout_stockvalue_textview;//库存数量
    LinearLayout footer_right_linerlayout;//底部按钮操作对象的父类用于 设置显示或隐藏 按钮
    CustomViewPager approvebuydetails_ck_bak_viewpager;//（图片详情 ，尺码参考 售后服务）
    LinearLayout approvebuydetails_ck_tab_bak_linearlayout;//TAB切换视图父对象
    View approvebuydetails_ck_tab_bak_linearlayout_view;//tab底部横线
    LinearLayout approvebuydetails_ck_suspensiontab_bak_linearlayout;//悬浮TAB切换视图父对象
    View approvebuydetails_ck_suspensiontab_bak_linearlayout_view;//悬浮TAB底部横线
    List<FragmentBean> tab_list = new ArrayList<FragmentBean>();
    TabViewpagerManager tabViewpagerManager;
    TabViewpagerManager suspenstabViewpagerManager;
    LinearLayout ll_footer;//按钮对象父视图

    int myScrollViewTop;//scrollY轴滑动的距离
    int buyLayoutHeight;
    int buyLayoutTop;
    RequestCk_SPECDetails requestCk_SPECDetails;//产品的颜色规格信息对象
    boolean isSucess = false;//是否加载完成
    RequestCKProductDeatilsInfo bean;
    Activity activity;
    LayoutInflater layoutInflater;
    View parentView;
    CKProductCountDownBean ckProductCountDownBean;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.activity = activity;
        layoutInflater = LayoutInflater.from(activity);
        bean = (RequestCKProductDeatilsInfo) activity.getIntent().getSerializableExtra("ProductInfo");
        if(bean!=null)
        {
            Data = bean.getData();
            if(Data!=null)
            {
                productID=Integer.valueOf(Data.getProductId());
                if(ckProductCountDownBean==null)
                {
                    ckProductCountDownBean=new CKProductCountDownBean();
                    ckProductCountDownBean.setCkProductDeatilsInfoBean(bean.getData());
                    ckProductCountDownBean.startTimer();
                }
            }
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (parentView == null) {
            parentView = layoutInflater.inflate(R.layout.approvebuyerdetails_ck_layout, null);
            initViews();
            setFont();
            setDatValue();
            getCkrProductSPECDetails();
            CollectobserverManage.getInstance().addObserver(this);
        }
        calculateContactHeight();
        if (parentView.getParent() != null) {
            ((ViewGroup) parentView.getParent()).removeView(parentView);
        }
        dangyanggouTime();
        return parentView;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private void initViews() {
        //返回
        ImageView back_grey_imageview = (ImageView) parentView.findViewById(R.id.back_grey_imageview);
        back_grey_imageview.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.finish();
            }
        });
        //分享
        ImageView share_grey_imageview = (ImageView) parentView.findViewById(R.id.share_grey_imageview);
        share_grey_imageview.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!MyApplication.getInstance().isUserLogin(activity)) {
                    return;
                }

                if (bean != null) {
                    CKProductDeatilsInfoBean productinfobean = bean.getData();
                    if (productinfobean != null) {
                        String content = ToolsUtil.nullToString(productinfobean.getShareDesc());
                        String url = productinfobean.getShareLink();
                        List<ProductsDetailsTagInfo> piclist = productinfobean.getProductPic();
                        String img_name = "";
                        if (piclist.size() > 0) {
                            img_name = piclist.get(0).getLogo();
                        }
                        String icon = ToolsUtil.getImage(ToolsUtil.nullToString(img_name), 320, 0);
                        ToolsUtil.shareUrl(activity, Integer.valueOf(productinfobean.getProductId()), "", content, url, icon);
                    }
                }
            }
        });

        //设置隐藏 喜欢人的列表
        View approvebuyerdetails_attention_linearlayout = parentView.findViewById(R.id.approvebuyerdetails_attention_linearlayout);
        approvebuyerdetails_attention_linearlayout.setVisibility(View.GONE);
        //隐藏 只支持字体的文字
        View approvebuyerdatails_layout_desc1_textview = parentView.findViewById(R.id.approvebuyerdatails_layout_desc1_textview);
        approvebuyerdatails_layout_desc1_textview.setVisibility(View.GONE);


        // 收藏按钮
        approvebuyerdetails_layout_shoucang_linerlayout_textview = (TextView) parentView.findViewById(R.id.approvebuyerdetails_layout_shoucang_linerlayout_textview);
        approvebuyerdetails_layout_shoucang_linerlayout_textview.setOnClickListener(this);
        if(Data.isFavorite())
        {
            approvebuyerdetails_layout_shoucang_linerlayout_textview.setText("已收藏");
        }else
        {
            approvebuyerdetails_layout_shoucang_linerlayout_textview.setText("收藏");
        }
        //头像包裹视图
        ll_attentionpeople_contener = (LinearLayout) parentView.findViewById(R.id.ll_attentionpeople_contener);
        // 活动父视图
        approvebuyerdetails_closeingtime_linearlayout = (LinearLayout) parentView.findViewById(R.id.approvebuyerdetails_closeingtime_linearlayout);
        // 打烊时间
        approvebuyerdetails_closeingtime_textview = (TextView) parentView.findViewById(R.id.approvebuyerdetails_closeingtime_textview);
        // 打烊信息
        approvebuyerdetails_closeinginfo_textview = (TextView) parentView.findViewById(R.id.approvebuyerdetails_closeinginfo_textview);
        approvebuyerdetails_footer = (LinearLayout) parentView.findViewById(R.id.approvebuyerdetails_footer);
        approvebuyerdetails_srcollview = (MyScrollView) parentView.findViewById(R.id.approvebuyerdetails_srcollview);
        approvebuyerdetails_srcollview.setOnScrollListener(new MyScrollView.OnScrollListener() {
            @Override
            public void onScroll(int scrollY) {
                Log.i("TAG", "onScroll scrollY:" + scrollY + " buyLayoutTop:" + buyLayoutTop + "  buyLayoutHeight:" + buyLayoutHeight);
                buyLayoutHeight = approvebuydetails_ck_tab_bak_linearlayout.getHeight();
                buyLayoutTop = approvebuydetails_ck_tab_bak_linearlayout.getTop();
                myScrollViewTop = approvebuyerdetails_srcollview.getTop();
                if (scrollY >= buyLayoutTop) {
                    if (approvebuydetails_ck_suspensiontab_bak_linearlayout != null) {
                        approvebuydetails_ck_suspensiontab_bak_linearlayout.setVisibility(View.VISIBLE);
                        approvebuydetails_ck_suspensiontab_bak_linearlayout_view.setVisibility(View.VISIBLE);
                        approvebuydetails_ck_tab_bak_linearlayout.setVisibility(View.INVISIBLE);
                        approvebuydetails_ck_tab_bak_linearlayout_view.setVisibility(View.INVISIBLE);
                        Log.i("TAG", "onScroll scrollY：悬浮显示 ");
                    }
                } else if (scrollY <= buyLayoutTop + buyLayoutHeight) {
                    if (approvebuydetails_ck_suspensiontab_bak_linearlayout != null) {
                        approvebuydetails_ck_suspensiontab_bak_linearlayout.setVisibility(View.GONE);
                        approvebuydetails_ck_suspensiontab_bak_linearlayout_view.setVisibility(View.GONE);
                        approvebuydetails_ck_tab_bak_linearlayout.setVisibility(View.VISIBLE);
                        approvebuydetails_ck_tab_bak_linearlayout_view.setVisibility(View.VISIBLE);
                        Log.i("TAG", "onScroll scrollY: 悬浮隐藏");
                    }
                }

            }
        });
        appprovebuyer_viewpager_footer_linerlayout = (LinearLayout) parentView.findViewById(R.id.appprovebuyer_viewpager_footer_linerlayout);
        appprovebuyer_viewpager = (ViewPager) parentView.findViewById(R.id.appprovebuyer_viewpager);
        appprovebuyer_viewpager_relativelayout = (RelativeLayout) parentView.findViewById(R.id.appprovebuyer_viewpager_relativelayout);
        DisplayMetrics dm = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
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
        appprovebuyer_viewpager.setOnPageChangeListener(new OnPageChangeListener() {

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

        approvebuyerdetails_icon_imageview = (RoundImageView) parentView.findViewById(R.id.approvebuyerdetails_icon_imageview);
        approvebuyerdetails_icon_imageview.setOnClickListener(this);

        approvebuyerdetails_layout_siliao_linerlayout_textview = (TextView) parentView.findViewById(R.id.approvebuyerdetails_layout_siliao_linerlayout_textview);
        approvebuyerdetails_layout_siliao_linerlayout_textview.setOnClickListener(this);

        approvebuyer_addcartbutton = (Button) parentView.findViewById(R.id.approvebuyer_addcartbutton);
        footer_right_linerlayout = (LinearLayout) parentView.findViewById(R.id.footer_right_linerlayout);
        approvebuyerbuybutton = (Button) parentView.findViewById(R.id.approvebuyerbuybutton);
        approvebuyerbuybutton.setOnClickListener(this);

        /****************
         * 颜色的 分类
         *
         * ********************/

        productColorTypeAdapter = new ProductColorTypeAdapter(activity, colortypelist);
        product_spec_layout_colortype_mygridview = (MyGridView) parentView.findViewById(R.id.product_spec_layout_colortype_mygridview);
        product_spec_layout_colortype_mygridview.setAdapter(productColorTypeAdapter);
        product_spec_layout_colortype_mygridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.i("TAG", "onItemSelected position=" + position);
                setSpecColorOnCheck(position);
            }
        });


        /**********
         * 规格 的 分类
         * *****/
        product_spec_layout_dimentype_mygridview = (MyGridView) parentView.findViewById(R.id.product_spec_layout_dimentype_mygridview);
        productSPECAdapter = new ProductSPECAdapter(activity, spectypelist);
        product_spec_layout_dimentype_mygridview.setAdapter(productSPECAdapter);
        product_spec_layout_dimentype_mygridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                setSpecdimentOnCheck(position);
                Log.i("TAG", "onItemSelected position=" + position);
            }
        });


        /*****************
         * 购买数量的设置
         * *******************/
        product_spec_layout_stockvalue_textview = (TextView) parentView.findViewById(R.id.product_spec_layout_stockvalue_textview);//库存
        createorder_dialog_layout_countvalue_edittext = (EditText) parentView.findViewById(R.id.createorder_dialog_layout_countvalue_edittext);
        createorder_dialog_layout_countvalue_edittext.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                int maxValue = Integer.parseInt(product_spec_layout_stockvalue_textview.getText().toString().trim());
                CharSequence text = createorder_dialog_layout_countvalue_edittext.getText();
                if (text instanceof Spannable) {
                    Spannable spanText = (Spannable) text;
                    Selection.setSelection(spanText, text.length());
                }
                if (s.toString().equals("")) {
                    createorder_dialog_layout_countvalue_edittext.setText(0 + "");
                    return;
                }
                int value = Integer.parseInt(s.toString());
                if (value < 0) {
                    createorder_dialog_layout_countvalue_edittext.setText(0 + "");
                    approvebuyerbuybutton.setEnabled(false);
                } else if (value > maxValue) {
                    createorder_dialog_layout_countvalue_edittext.setText(Integer.toString(maxValue));
                    approvebuyerbuybutton.setEnabled(true);
                }
                if (s.toString().length() > 1)//如果位数大于1位
                {
                    char c = s.toString().charAt(0);
                    if (c == 0) {
                        createorder_dialog_layout_countvalue_edittext.setText(Integer.toString(value));
                    }
                }
                isTextButtonEnable();
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        //减
        create_dialog_jian_button = (Button) parentView.findViewById(R.id.create_dialog_jian_button);
        create_dialog_jian_button.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                int value = Integer.parseInt(createorder_dialog_layout_countvalue_edittext.getText().toString());
                value--;
                if (value <= 0) {
                    value = 0;
                }
                createorder_dialog_layout_countvalue_edittext.setText(Integer.toString(value));
                isTextButtonEnable();
            }
        });
        //加
        create_dialog_jia_button = (Button) parentView.findViewById(R.id.create_dialog_jia_button);
        create_dialog_jia_button.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                int values = Integer.parseInt(createorder_dialog_layout_countvalue_edittext.getText().toString());
                int maxValue = Integer.parseInt(product_spec_layout_stockvalue_textview.getText().toString());
                values++;
                if (values >= maxValue) {
                    values = maxValue;
                }
                createorder_dialog_layout_countvalue_edittext.setText(Integer.toString(values));
                isTextButtonEnable();
            }
        });

        /*************
         * 购买按钮
         * **************/
        ll_footer = (LinearLayout) parentView.findViewById(R.id.ll_footer);

        /*****
         * 进圈按钮
         * ***/
        Button in_circle_button=(Button)parentView.findViewById(R.id.in_circle_button);
        in_circle_button.setOnClickListener(this);

    }


    /**********
     * 设置当前选中的颜色
     ************/
    void setSpecColorOnCheck(int position) {
        if (colortypelist != null) {
            for (int i = 0; i < colortypelist.size(); i++) {
                colortypelist.get(i).setIsChecked(false);
            }
            if (position <= colortypelist.size() - 1) {
                ProductColorTypeBean bean = colortypelist.get(position);
                bean.setIsChecked(true);
                checked_ProductColorTypeBean = bean;
                //根据选择的颜色 设置尺寸
                spectypelist.clear();
                spectypelist.addAll(bean.getSize());
                if(spectypelist.size()>0)
                {
                    setSpecdimentOnCheck(0);
                }
            }

        }
        if (productColorTypeAdapter != null) {
            productColorTypeAdapter.notifyDataSetChanged();
        }
        if (productSPECAdapter != null) {
            productSPECAdapter.notifyDataSetChanged();
        }

    }


    /**********
     * 设置当前选中的规格
     ************/
    void setSpecdimentOnCheck(int position) {
        if (spectypelist != null) {
            for (int i = 0; i < spectypelist.size(); i++) {
                spectypelist.get(i).setIschecked(false);
            }
            if (position <= spectypelist.size() - 1) {
                ProductSPECbean bean = spectypelist.get(position);
                bean.setIschecked(true);
                checked_ProductSPECbean = bean;
                product_spec_layout_stockvalue_textview.setText(Integer.toString(bean.getInventory()));
                isTextButtonEnable();
            }

        }
        if (productSPECAdapter != null) {
            productSPECAdapter.notifyDataSetChanged();
        }

    }


    /***********
     * 计算 去掉 状态栏  head内容  footer 内容的高度后  中间主要内容区域的高度
     **********/
    int calculateContactHeight() {
        int footerheight = ll_footer.getHeight();//底部高度
        int statusheight = ToolsUtil.getStatusHeight(activity);//状态栏高度
        int tabheight = approvebuydetails_ck_tab_bak_linearlayout.getHeight();
        Log.i("TAG", "calculateContactHeight footerheight:" + footerheight + "  statusheight:" + statusheight + "  tabheight:" + tabheight);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int allHeight = displayMetrics.heightPixels;
        int contantHeight = allHeight - statusheight - footerheight - tabheight;
        Log.i("TAG", "calculateContactHeight contantHeight:" + contantHeight);
        if (approvebuydetails_ck_bak_viewpager != null) {
            approvebuydetails_ck_bak_viewpager.setSmallHieght(contantHeight);
            approvebuydetails_ck_bak_viewpager.invalidate();
            Log.i("TAG", "calculateContactHeight approvebuydetails_ck_bak_viewpager-height:" + approvebuydetails_ck_bak_viewpager.getHeight());
        }
        return contantHeight;
    }

    /********
     * 控制scroll 滑动到指定的位置
     ********/
    void scrollToxy() {
        int tabviewTop = approvebuydetails_ck_tab_bak_linearlayout.getTop();//获取当前TAB对象距顶端的距离
        int srcollY = approvebuyerdetails_srcollview.getScrollY();//获取 当前滚动视图距Y 轴滑动的距离
        if (approvebuyerdetails_srcollview != null) {
            if (srcollY < tabviewTop) {
                Log.i("TAG", "scrollToxy tabviewTop:" + tabviewTop);
                //自动滑动到顶点
                approvebuyerdetails_srcollview.smoothScrollTo(0, tabviewTop);
                //发送通知 判断滑动是否到位
                handler.sendMessageDelayed(handler.obtainMessage(200), 10);
            }
        }
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 200:
                    scrollToxy();
                    break;
                case 100:
                    if(approvebuyerdetails_srcollview!=null)
                    {
                        approvebuyerdetails_srcollview.smoothScrollTo(0, 0);
                    }
                    break;
            }
        }
    };


    /*****
     * 根据库存 即 商品数量 设置 按钮颜色
     ***/
    void isTextButtonEnable() {
        create_dialog_jian_button.setSelected(true);
        create_dialog_jia_button.setSelected(true);
        //库存
        int Stock = Integer.valueOf(product_spec_layout_stockvalue_textview.getText().toString().trim());
        //当前选择的购买数量
        int count = Integer.valueOf(createorder_dialog_layout_countvalue_edittext.getText().toString().trim());
        if (Stock <= 0) {
            create_dialog_jian_button.setSelected(false);
            create_dialog_jia_button.setSelected(false);
        } else if (count <= 0) {
            create_dialog_jian_button.setSelected(false);
        } else if (count >= Stock) {
            create_dialog_jia_button.setSelected(false);
        }

    }


    void forwardSiLiao() {
        if (!MyApplication.getInstance().isUserLogin(activity) || !isSucess) {
            return;
        }

        ToolsUtil.forwardChatActivity(getActivity(),ToolsUtil.nullToString(bean.getData().getBuyerName()),Integer.valueOf(bean.getData().getBuyerId()),0,null,null,null);
    }


    /*******
     * 跳转到订单确认页面
     ****/
    void startChatActivity() {
        //如果 用户没有登录 或者 当前页面的数据没有加载完成 则直接返回
        if (!MyApplication.getInstance().isUserLogin(activity) || !isSucess) {
            return;
        }

        if (checked_ProductColorTypeBean == null) {
            MyApplication.getInstance().showMessage(activity, "请选择颜色");
            return;
        }

        if (checked_ProductSPECbean == null) {
            MyApplication.getInstance().showMessage(activity, "请选择尺寸");
            return;
        }

        int count=Integer.valueOf(createorder_dialog_layout_countvalue_edittext.getText().toString().trim());
        if(count<=0)
        {
            MyApplication.getInstance().showMessage(activity, "购买数量必须大于0");
            return;
        }

        if (bean != null) {
            AffirmProductInfo affirmProductInfo = new AffirmProductInfo();
            affirmProductInfo.setData(bean);
            affirmProductInfo.setColorId(checked_ProductColorTypeBean.getColorId());
            affirmProductInfo.setColorName(checked_ProductColorTypeBean.getColorName());
            affirmProductInfo.setPic(checked_ProductColorTypeBean.getPic());
            affirmProductInfo.setSizeId(checked_ProductSPECbean.getSizeId());
            affirmProductInfo.setSizeName(checked_ProductSPECbean.getSizeName());
            affirmProductInfo.setBuycount(count);

            Intent intent = new Intent(activity, ConfirmOrderForZhuanGui.class);
            intent.putExtra("ProductInfo", affirmProductInfo);
            startActivity(intent);
        }
    }

    /***
     * 设置文本值
     *
     * @param res int 视图id
     * @param str 要显示内容 null 不进行负值
     ***/
    void setdataValue(int res, String str) {
        View v = parentView.findViewById(res);
        if (v != null && v instanceof TextView) {
            if (str != null) {
                ((TextView) v).setText(str);
            }
            FontManager.changeFonts(activity, ((TextView) v));
        }
    }

    /**********
     * 获取商品规格信息
     ******/
    void getCkrProductSPECDetails() {
        httpControl.getCkrProductSPECDetails(productID, new HttpCallBackInterface() {
            @Override
            public void http_Success(Object obj) {
                if (obj instanceof RequestCk_SPECDetails) {
                    requestCk_SPECDetails = (RequestCk_SPECDetails) obj;
                    setPSECValue();//设置规格尺寸数据
                    isSucess=true;
                }
            }

            @Override
            public void http_Fails(int error, String msg) {
                MyApplication.getInstance().showMessage(activity, msg);
                activity.finish();
            }
        }, activity, true, true);
    }


    /***************
     * 设置 产品的 颜色 规格尺寸
     **************/
    void setPSECValue() {
        if (requestCk_SPECDetails == null || requestCk_SPECDetails.getData() == null) {
            return;
        } else {
            colortypelist.clear();
            colortypelist.addAll(requestCk_SPECDetails.getData());
            if (colortypelist != null && colortypelist.size() > 0) {
                for (int i = 0; i < colortypelist.size(); i++) {
                    ProductColorTypeBean productColorTypeBean = colortypelist.get(i);
                    if (productColorTypeBean.getSize() != null && productColorTypeBean.getSize().size() > 0) {
                        for (int j = 0; j < productColorTypeBean.getSize().size(); j++) {
                            ProductSPECbean productSPECbean = productColorTypeBean.getSize().get(j);
                            if (productSPECbean.getInventory() > 0) {
                                setSpecColorOnCheck(i);
                                setSpecdimentOnCheck(j);
                                isSucess = true;
                                return;
                            }
                        }
                    }
                }
            }
        }

    }


    /********
     * 根据类型 返回 对应视图的fragment
     ***/
    Fragment getFragment(ProductExtTab1Fragment.Type type) {
        ProductExtTab1Fragment productExtTab1Fragment = new ProductExtTab1Fragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("ProductDetails_data", bean);
        bundle.putSerializable("type", type);
        productExtTab1Fragment.setArguments(bundle);
        return productExtTab1Fragment;
    }

    /***********
     * 初始化 tab 视图与Fragment的信息
     ****/
    void initTabView() {
        tab_list.add(new FragmentBean("图片详情", -1, getFragment(ProductExtTab1Fragment.Type.Img_details)));
        tab_list.add(new FragmentBean("尺码参考", -1, getFragment(ProductExtTab1Fragment.Type.Size_explare)));
        tab_list.add(new FragmentBean("售后服务", -1, getFragment(ProductExtTab1Fragment.Type.Aftermarket_Server)));

        /*********************
         *  备注信息等 设置
         * ************************/
        approvebuydetails_ck_bak_viewpager = (CustomViewPager) parentView.findViewById(R.id.approvebuydetails_ck_bak_viewpager);

        ViewTreeObserver vto2 = approvebuydetails_ck_bak_viewpager.getViewTreeObserver();
        vto2.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                calculateContactHeight();
            }
        });


        approvebuydetails_ck_bak_viewpager.setOffscreenPageLimit(2);


        /************
         * 悬浮TAB切换视图
         * *********/
        approvebuydetails_ck_suspensiontab_bak_linearlayout = (LinearLayout) parentView.findViewById(R.id.approvebuydetails_ck_suspensiontab_bak_linearlayout);
        approvebuydetails_ck_suspensiontab_bak_linearlayout_view = (View) parentView.findViewById(R.id.approvebuydetails_ck_suspensiontab_bak_linearlayout_view);
        suspenstabViewpagerManager = new TabViewpagerManager(activity, tab_list, approvebuydetails_ck_suspensiontab_bak_linearlayout, approvebuydetails_ck_bak_viewpager);
        suspenstabViewpagerManager.setTabOnClickListener(new TabViewpagerManager.TabOnClickListener() {

            @Override
            public void onTabClick(int i) {
                scrollToxy();
            }
        });
        suspenstabViewpagerManager.initFragmentViewPager(this.getFragmentManager(), null);
        suspenstabViewpagerManager.setCurrView(0);


        /************
         * TAB切换视图
         * *********/
        approvebuydetails_ck_tab_bak_linearlayout = (LinearLayout) parentView.findViewById(R.id.approvebuydetails_ck_tab_bak_linearlayout);
        approvebuydetails_ck_tab_bak_linearlayout_view = parentView.findViewById(R.id.approvebuydetails_ck_tab_bak_linearlayout_view);
        tabViewpagerManager = new TabViewpagerManager(activity, tab_list, approvebuydetails_ck_tab_bak_linearlayout, approvebuydetails_ck_bak_viewpager);
        tabViewpagerManager.setTabOnClickListener(new TabViewpagerManager.TabOnClickListener() {

            @Override
            public void onTabClick(int i) {
                scrollToxy();
            }
        });
        tabViewpagerManager.initFragmentViewPager(this.getFragmentManager(), null);
        tabViewpagerManager.setCurrView(0);
        approvebuydetails_ck_bak_viewpager.setOnPageChangeListener(new OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                suspenstabViewpagerManager.setCurrView(position);
                tabViewpagerManager.setCurrView(position);
                View view = approvebuydetails_ck_bak_viewpager.getChildAt(position);
                Log.i("TAG", "onPageSelected position:" + position);
                if (view != null) {
                    int height = view.getMeasuredHeightAndState();
                    LayoutParams layoutParams = (LinearLayout.LayoutParams) approvebuydetails_ck_bak_viewpager.getLayoutParams();
                    layoutParams.height = height;
                    Log.i("TAG", "onPageSelected height:" + height);
                    /*int smollHeight = calculateContactHeight();
                    if (height < smollHeight) {
                        layoutParams.height = smollHeight;
                    }*/
                    approvebuydetails_ck_bak_viewpager.setLayoutParams(layoutParams);
                }
                approvebuydetails_ck_bak_viewpager.invalidate();
                scrollToxy();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        if (customPagerAdapter != null) {
            customPagerAdapter.notifyDataSetChanged();
        }

    }

    /*****
     * 设置商品基础数据
     ****/
    void setDatValue() {

        initTabView();

        // 自提地址
        String address = ToolsUtil.nullToString(Data.getPickAddress());
        // 买手头像
        String usericon = ToolsUtil.nullToString(Data.getBuyerLogo());
        // 买手昵称
        String username = ToolsUtil.nullToString(Data.getBuyerName());
        // 关注人列表
        LikeUsersInfoBean usersInfoList = Data.getLikeUsers();
        //城市
        String cityAddress = ToolsUtil.nullToString(Data.getCityName());
        setdataValue(R.id.address_name_textview, cityAddress);
        // 价格
        double price = Data.getPrice();
        // 商品名称
        String productName = ToolsUtil.nullToString(Data.getProductName());
        initPic(usericon, approvebuyerdetails_icon_imageview);
        // 自提地址
        setdataValue(R.id.approvebuyerdetails_addressvalue_textview, address);
        //隐藏提货地址
        View approvebuyerdetails_addressvalue_textview = parentView.findViewById(R.id.approvebuyerdetails_addressvalue_textview);
        //approvebuyerdetails_addressvalue_textview.setVisibility(View.GONE);
        View approvebuyerdetails_address_textview = parentView.findViewById(R.id.approvebuyerdetails_address_textview);
        //approvebuyerdetails_address_textview.setVisibility(View.GONE);

        TextView approvebuyerdetails_buyeraddress_textview = (TextView) parentView.findViewById(R.id.approvebuyerdetails_buyeraddress_textview);
        approvebuyerdetails_buyeraddress_textview.setText(ToolsUtil.nullToString(Data.getPickAddress()));
        setdataValue(R.id.approvebuyerdetails_name_textview, username);
        // 金额
        setdataValue(R.id.approvebuyerdetails_price_textview,
                "￥" + Double.toString(price));
        //吊牌价
        setdataValue(R.id.hangtag_price_textview, "￥" + Double.toString(Data.getUnitPrice()));
        TextView hangtag_price_textview = (TextView) parentView.findViewById(R.id.hangtag_price_textview);
        hangtag_price_textview.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        hangtag_price_textview.setVisibility(View.VISIBLE);

        // 商品名称
        setdataValue(R.id.approvebuyerdetails_producename_textview, productName);
        //设置服务描述
        TextView product_spec_layout_serve_textview = (TextView) parentView.findViewById(R.id.product_spec_layout_serve_textview);

        LikeUsersInfoBean likeUsersInfoBean = Data.getLikeUsers();

        if (Data.getProductPic() != null && Data.getProductPic().size() > 0) {
            //图片和标签
            List<ProductsDetailsTagInfo> productsDetailsTagInfo_list = Data.getProductPic();
            for (int i = 0; i < productsDetailsTagInfo_list.size(); i++) {
                RelativeLayout rl = new RelativeLayout(activity);
                ImageView iv = new ImageView(activity);
                iv.setBackgroundColor(ApproveBuyerDetails_ck_Fragment.this.getResources().getColor(R.color.color_lightgrey));
                iv.setScaleType(ScaleType.CENTER_CROP);
                rl.addView(iv, new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));
                TagImageView tiv = new TagImageView(activity);
                //tiv.setBackgroundColor(ApproveBuyerDetailsActivity.this.getResources().getColor(R.color.color_blue));
                //tiv.setAlpha(0.5f);
                List<ProductsDetailsTagsInfo> productsDetailsTagsInfo_list = productsDetailsTagInfo_list.get(i).getTags();
                if (productsDetailsTagsInfo_list != null && productsDetailsTagsInfo_list.size() > 0) {
                    for (int j = 0; j < productsDetailsTagsInfo_list.size(); j++) {
                        DisplayMetrics dm = new DisplayMetrics();
                        activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
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
            customPagerAdapter = new ScrollViewPagerAdapter(activity, viewlist);
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
        footer_right_linerlayout.setVisibility(View.VISIBLE);
        approvebuyerbuybutton.setVisibility(View.VISIBLE);
        approvebuyerdetails_layout_siliao_linerlayout_textview.setVisibility(View.VISIBLE);
        ll_footer.setVisibility(View.VISIBLE);
        handler.sendMessageDelayed(handler.obtainMessage(100),300);
    }

    @Override
    public void onResume() {
        super.onResume();
        startTimeToViewPager();
    }


    @Override
    public void onPause() {
        super.onPause();
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
            View v = new View(activity);
            v.setBackgroundResource(R.drawable.tabround_background);
            int width = (int) ApproveBuyerDetails_ck_Fragment.this.getResources()
                    .getDimension(R.dimen.shop_main_lineheight8_dimen);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    width, width);
            params.leftMargin = (int) ApproveBuyerDetails_ck_Fragment.this
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
        setdataValue(R.id.product_spec_layout_stockvalue_textview, null);
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
        FontManager.changeFonts(activity, approvebuyer_addcartbutton);
        FontManager.changeFonts(activity, approvebuyerbuybutton);

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
                activity.runOnUiThread(new Runnable() {

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
                if (!MyApplication.getInstance().isUserLogin(activity)) {
                    return;
                }
                try
                {
                    ToolsUtil.forwardShopMainActivity(activity, Integer.valueOf(bean.getData().getBuyerId()));
                }catch(Exception e)
                {
                    MyApplication.getInstance().showMessage(getActivity(),"商户id错误");
                }

                break;
            case R.id.approvebuyerdetails_layout_siliao_linerlayout_textview:
                forwardSiLiao();
                break;
            case R.id.approvebuyerbuybutton:
                if(!ckProductCountDownBean.isDayangGou())
                {
                    MyApplication.getInstance().showMessage(getActivity(),"活动还没开始");
                    return;
                }
                startChatActivity();
                break;
            case R.id.approvebuyerdetails_layout_shoucang_linerlayout_textview:
                if (!MyApplication.getInstance().isUserLogin(activity)) {
                    return;
                }
                if (Data != null) {
                    if (Data.isFavorite()) {
                        submitAttention(0, Data, v);
                    } else {
                        submitAttention(1, Data, v);
                    }
                }
                break;
            case R.id.in_circle_button://进圈
                if (!MyApplication.getInstance().isUserLogin(activity)) {
                    return;
                }
                try
                {
                    ToolsUtil.forwardShopMainCircleActivity(getActivity(),Integer.valueOf(bean.getData().getBuyerId()));
                }catch(Exception e)
                {
                    MyApplication.getInstance().showMessage(getActivity(),"商户id错误");
                }
                break;
        }

    }

    void  dangyanggouTime()
    {
        if(ckProductCountDownBean!=null)
        {
            ckProductCountDownBean.setTimerLinstener(new CKProductCountDownBean.TimerLinstener() {
                @Override
                public void timerCallBack() {
                    if(approvebuyerbuybutton!=null)
                    {
                        if(getActivity()!=null)
                        {
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    //如果打样够开始
                                    if(ckProductCountDownBean.isDayangGou())
                                    {
                                        approvebuyerbuybutton.setText("立即购买");
                                    }else
                                    {
                                        approvebuyerbuybutton.setText("剩余开始时间："+ckProductCountDownBean.getShowstr());
                                    }
                                }
                            });

                        }
                    }
                }
            });
        }
    }



    /****
     * 提交收藏与取消收藏商品
     *
     * @param Status int 0表示取消收藏 1表示收藏
     * @param bean   ProductsDetailsInfoBean 商品对象
     **/
    void submitAttention(final int Status, final CKProductDeatilsInfoBean bean, final View v) {
        httpControl.setFavor(bean.getProductId(), Status, new HttpCallBackInterface() {

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
                            ((TextView) v).setText("已收藏");
                            v.setSelected(true);
                            bean.setIsFavorite(true);
                            break;
                    }
                    //观察者通知数据改变
                    PubuliuBeanInfo pubuliuBeanInfo=new PubuliuBeanInfo();
                    pubuliuBeanInfo.setId(bean.getProductId());
                    pubuliuBeanInfo.setIscollection(bean.isFavorite());
                    CollectobserverManage.getInstance().dataChangeNotication(pubuliuBeanInfo);

                }
            }

            @Override
            public void http_Fails(int error, String msg) {
                MyApplication.getInstance().showMessage(activity, msg);
            }
        }, activity);
    }

    @Override
    public void observerCallNotification(PubuliuBeanInfo pubuliuBeanInfo) {
        if(pubuliuBeanInfo!=null)
        {
            if(pubuliuBeanInfo.getId().equals(bean.getData().getProductId()))
            {
                bean.getData().setIsFavorite(pubuliuBeanInfo.iscollection());
                if(approvebuyerdetails_layout_shoucang_linerlayout_textview!=null)
                {
                    approvebuyerdetails_layout_shoucang_linerlayout_textview.setSelected(bean.getData().isFavorite());
                }
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        CollectobserverManage.getInstance().removeObserver(this);
    }
}
