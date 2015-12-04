package com.shenma.yueba.baijia.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.shenma.yueba.R;
import com.shenma.yueba.application.MyApplication;
import com.shenma.yueba.baijia.fragment.ChatFragment;
import com.shenma.yueba.baijia.fragment.ShopMainFragment;
import com.shenma.yueba.baijia.modle.FragmentBean;
import com.shenma.yueba.baijia.view.TabViewpagerManager;
import com.shenma.yueba.util.HttpControl;
import com.shenma.yueba.util.SharedUtil;
import com.shenma.yueba.yangjia.modle.CircleDetailBackBean;

import java.util.ArrayList;
import java.util.List;

/*****
 * 本类定义 店铺商品首页显示页面
 * 1.显示商家logo  名称  地址   店铺描述 以及 商品图片等信息
 *
 * @author gyj
 * @date 2015-05-05
 ***/
public class ShopMainActivity extends FragmentActivity implements OnClickListener {
    LinearLayout shop_main_layout_headcontant_linearlayout;//TAB按钮父类
    TextView tv_top_left;//返回按钮
    ImageView circlesettings_imageview;//圈子设置按钮
    List<FragmentBean> head_data = new ArrayList<FragmentBean>();
    // 存储Tab切换的视图对象
    List<View> footer_list = new ArrayList<View>();
    int userID = -1;//用户id
    ViewPager shop_main_layout_contact_viewpager;//切换的viewpager控件
    FragmentManager fragmentManager;
    TabViewpagerManager tabViewpagerManager;
    boolean isrunning=false;
    HttpControl httpControl=new HttpControl();
    CircleDetailBackBean circleDetailBackBean;
    ShopMainFragment shopMainFragment;
    ChatFragment chatFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        MyApplication.getInstance().addActivity(this);//加入回退栈
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shop_main_layout);
        userID = this.getIntent().getIntExtra("userID", -1);
        if (userID < 0) {
            MyApplication.getInstance().showMessage(ShopMainActivity.this, "数据错误，请重试");
            finish();
            return;
        }
        fragmentManager = getSupportFragmentManager();
        initView();
        setCurrView(0);
    }

    /****
     * 初始化视图信息
     **/
    void initView() {
        tv_top_left = (TextView) findViewById(R.id.tv_top_left);
        tv_top_left.setOnClickListener(this);
        circlesettings_imageview = (ImageView) findViewById(R.id.circlesettings_imageview);
        circlesettings_imageview.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!MyApplication.getInstance().isUserLogin(ShopMainActivity.this)) {
                    return;
                }
                if (circlesettings_imageview.getTag()!=null && circlesettings_imageview.getTag() instanceof Integer) {
                    int circleId = (Integer)circlesettings_imageview.getTag();
                    Intent intent = new Intent(ShopMainActivity.this, CircleInfoActivity.class);
                    intent.putExtra("circleId", circleId);
                    startActivity(intent);
                }

            }
        });
        initHeadTab();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_top_left:
                finish();
                break;
        }
    }

    /*****
     * 初始化 顶部TAB 切换的 数据
     ***/
    void initHeadTab() {
        if(shopMainFragment==null)
        {
            shopMainFragment=new ShopMainFragment();
        }

        if(chatFragment==null)
        {
            chatFragment=new ChatFragment();
        }
        chatFragment=new ChatFragment();
        shop_main_layout_headcontant_linearlayout = (LinearLayout) findViewById(R.id.shop_main_layout_headcontant_linearlayout);
        head_data.add(new FragmentBean("店铺", -1, shopMainFragment));
        head_data.add(new FragmentBean("圈子", -1, chatFragment));
        shop_main_layout_contact_viewpager = (ViewPager) findViewById(R.id.shop_main_layout_contact_viewpager);
        tabViewpagerManager = new TabViewpagerManager(this, head_data, shop_main_layout_headcontant_linearlayout, shop_main_layout_contact_viewpager);
        Bundle bundle = new Bundle();
        bundle.putInt("userID", userID);
        tabViewpagerManager.initFragmentViewPager(fragmentManager, bundle);
        shop_main_layout_contact_viewpager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                setCurrView(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    /***
     * 设置当前需要显示的 item
     ***/
    void setCurrView(int i) {
        shop_main_layout_contact_viewpager.setCurrentItem(i);
        tabViewpagerManager.setCurrView(i);
    }

}
