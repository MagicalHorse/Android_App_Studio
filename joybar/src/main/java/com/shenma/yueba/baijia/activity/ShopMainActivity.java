package com.shenma.yueba.baijia.activity;

import java.util.ArrayList;
import java.util.List;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.shenma.yueba.R;
import com.shenma.yueba.application.MyApplication;
import com.shenma.yueba.baijia.fragment.ChatFragment;
import com.shenma.yueba.baijia.fragment.ShopMainFragment;
import com.shenma.yueba.baijia.fragment.ShopPuBuliuFragment;
import com.shenma.yueba.baijia.modle.FragmentBean;
import com.shenma.yueba.baijia.modle.RequestUserInfoBean;
import com.shenma.yueba.baijia.modle.UserInfoBean;
import com.shenma.yueba.constants.Constants;
import com.shenma.yueba.util.FontManager;
import com.shenma.yueba.util.HttpControl;
import com.shenma.yueba.util.HttpControl.HttpCallBackInterface;
import com.shenma.yueba.util.ToolsUtil;
import com.shenma.yueba.view.RoundImageView;
import com.umeng.analytics.MobclickAgent;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

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
    // 当前选中的id
    int currid = -1;
    int userID=-1;//用户id
    int circleId=-1;//圈子id
    ViewPager shop_main_layout_contact_viewpager;//切换的viewpager控件
    FragmentManager fragmentManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        MyApplication.getInstance().addActivity(this);//加入回退栈
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shop_main_layout);
        userID=this.getIntent().getIntExtra("userID", -1);
        if(userID<0)
        {
            MyApplication.getInstance().showMessage(ShopMainActivity.this, "数据错误，请重试");
            finish();
            return;
        }
        fragmentManager=getSupportFragmentManager();
        initView();
        setCurrView(0);
    }

    /****
     * 初始化视图信息
     **/
    void initView() {
        tv_top_left = (TextView) findViewById(R.id.tv_top_left);
        tv_top_left.setOnClickListener(this);
        circlesettings_imageview=(ImageView)findViewById(R.id.circlesettings_imageview);
        circlesettings_imageview.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!MyApplication.getInstance().isUserLogin(ShopMainActivity.this)) {
                    return;
                }
                Intent intent=new Intent(ShopMainActivity.this, CircleInfoActivity.class);
                intent.putExtra("circleId",circleId);
                startActivity(intent);
            }
        });
        initHeadTab();
        initViewPager();
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
        shop_main_layout_headcontant_linearlayout = (LinearLayout) findViewById(R.id.shop_main_layout_headcontant_linearlayout);
        head_data.add(new FragmentBean("店铺", -1, new ShopMainFragment()));
        head_data.add(new FragmentBean("圈子", -1, new ChatFragment()));
        for(int i=0;i<head_data.size();i++)
        {
            FragmentBean fragmentBean= head_data.get(i);
            RelativeLayout rl = (RelativeLayout)LayoutInflater.from(ShopMainActivity.this).inflate(R.layout.tab_line_layout,null);
            TextView tv = (TextView) rl.findViewById(R.id.tab_line_textview);
            FontManager.changeFonts(ShopMainActivity.this, tv);
            rl.setTag(i);
            rl.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    int i = (Integer) v.getTag();
                    setCurrView(i);
                }
            });
            tv.setGravity(Gravity.CENTER);
            tv.setText(fragmentBean.getName());
            LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT);
            param.weight = 1;
            param.gravity = Gravity.CENTER;
            shop_main_layout_headcontant_linearlayout.addView(rl, param);
            footer_list.add(rl);
        }
    }

    /***
     * 设置当前需要显示的 item
     * ***/
    void setCurrView(int i) {
        if (currid == i) {
            return;
        }
        currid = i;
        setTextColor(i);
        shop_main_layout_contact_viewpager.setCurrentItem(i);
        switch(i)
        {
            case 1:
                circlesettings_imageview.setVisibility(View.VISIBLE);
                break;
            default:
                circlesettings_imageview.setVisibility(View.INVISIBLE);
        }
    }

    /*****
     * 设置字体颜色及选中后显示的图片
     * ***/
    void setTextColor(int value) {
        for (int i = 0; i < footer_list.size(); i++) {
            RelativeLayout rl = (RelativeLayout) footer_list.get(i);
            TextView tv = (TextView) rl.findViewById(R.id.tab_line_textview);
            View v = (View) rl.findViewById(R.id.tab_line_view);
            if (i == value) {
                tv.setTextColor(this.getResources().getColor(
                        R.color.color_deeoyellow));
                tv.setTextSize(Constants.title_text_selected_size);
                v.setVisibility(View.VISIBLE);
            } else {
                tv.setTextColor(this.getResources().getColor(
                        R.color.text_gray_color));
                tv.setTextSize(Constants.title_text_normal_size);
                v.setVisibility(View.INVISIBLE);
            }

        }
    }

    /*****
     * 初始化 ViewPager 的 操作
     * ***/
    void initViewPager()
    {
        shop_main_layout_contact_viewpager=(ViewPager)findViewById(R.id.shop_main_layout_contact_viewpager);
        shop_main_layout_contact_viewpager.setOffscreenPageLimit(0);
        shop_main_layout_contact_viewpager.setAdapter(new FragmentPagerAdapter(fragmentManager) {
            @Override
            public Fragment getItem(int position) {
                FragmentBean itembean= head_data.get(position);
                Fragment fragment=(Fragment)itembean.getFragment();
                Bundle bundle=new Bundle();
                bundle.putInt("userID",userID);
                fragment.setArguments(bundle);
                return fragment;
            }

            @Override
            public int getCount() {
                return head_data.size();
            }
        });

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
}
