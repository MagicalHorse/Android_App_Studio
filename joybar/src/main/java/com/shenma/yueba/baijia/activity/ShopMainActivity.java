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
    int buyerId = -1;//买手用户id
    ViewPager shop_main_layout_contact_viewpager;//切换的viewpager控件
    FragmentManager fragmentManager;
    TabViewpagerManager tabViewpagerManager;
    boolean isrunning = false;
    HttpControl httpControl = new HttpControl();
    CircleDetailBackBean circleDetailBackBean;
    ShopMainFragment shopMainFragment;
    ChatFragment chatFragment;
    String Type = null;//类型 如果不为空 且为 Circle 则加载圈子 否则加载店铺

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        MyApplication.getInstance().addActivity(this);//加入回退栈
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shop_main_layout);
        buyerId = this.getIntent().getIntExtra("buyerId", -1);
        if (buyerId < 0) {
            MyApplication.getInstance().showMessage(ShopMainActivity.this, "数据错误，请重试");
            finish();
            return;
        }

        fragmentManager = getSupportFragmentManager();
        initView();
        setCurrTabView();
    }


    void setCurrTabView() {
        Type = this.getIntent().getStringExtra("Type");
        if (Type != null && Type.equals("Circle")) {
            setCurrView(1);
        } else {
            setCurrView(0);
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (intent != null) {
            int newuserID = intent.getIntExtra("buyerId", -1);
            if (newuserID != buyerId) {
                startActivity(intent);
                finish();
            } else {
                setIntent(intent);
                setCurrTabView();
            }
        } else {
            startActivity(intent);
            finish();

        }

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
                if (circlesettings_imageview.getTag() != null && circlesettings_imageview.getTag() instanceof Integer) {
                    int circleId = (Integer) circlesettings_imageview.getTag();
                    Intent intent = new Intent(ShopMainActivity.this, CircleInfoActivity.class);
                    intent.putExtra("circleId", circleId);
                    startActivity(intent);
                }

            }
        });
        initHeadTab();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (chatFragment != null) {
            chatFragment.onActivityResult(requestCode, resultCode, data);
        }
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
        if (shopMainFragment == null) {
            shopMainFragment = new ShopMainFragment();
        }

        if (chatFragment == null) {
            chatFragment = new ChatFragment();
        }
        chatFragment = new ChatFragment();
        shop_main_layout_headcontant_linearlayout = (LinearLayout) findViewById(R.id.shop_main_layout_headcontant_linearlayout);
        head_data.add(new FragmentBean("店铺", -1, shopMainFragment));
        head_data.add(new FragmentBean("圈子", -1, chatFragment));
        shop_main_layout_contact_viewpager = (ViewPager) findViewById(R.id.shop_main_layout_contact_viewpager);
        tabViewpagerManager = new TabViewpagerManager(this, head_data, shop_main_layout_headcontant_linearlayout, shop_main_layout_contact_viewpager){
            //重写 方法
            @Override
            public synchronized void setCurrView(int i) {
                //super.setCurrView(i);
                switch (i)
                {
                    case 0:
                        tabViewpagerManager.setCurrView(0,true);
                        break;
                    case 1:
                        if(!MyApplication.getInstance().isUserLogin(ShopMainActivity.this))
                        {
                            tabViewpagerManager.setCurrView(0,false);
                        }else
                        {
                            tabViewpagerManager.setCurrView(1,true);
                        }
                        break;
                }
            }
        };

        this.getIntent().putExtra("chatType","DefaultCircle");
        tabViewpagerManager.initFragmentViewPager(fragmentManager, null);

        shop_main_layout_contact_viewpager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position)
                {
                    case 0:
                        setCurrView(0);
                        break;
                    case 1:
                        if(!MyApplication.getInstance().isUserLogin(ShopMainActivity.this))
                        {
                            setCurrView(0);
                            shop_main_layout_contact_viewpager.setCurrentItem(0,false);
                        }else
                        {
                            setCurrView(1);

                        }
                        break;
                }
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
        tabViewpagerManager.setCurrView(i);
        switch (i) {
            case 0:
                circlesettings_imageview.setVisibility(View.INVISIBLE);
                break;
            case 1:
                if (circlesettings_imageview.getTag() != null) {
                    circlesettings_imageview.setVisibility(View.VISIBLE);
                }
                break;
        }
    }

}
