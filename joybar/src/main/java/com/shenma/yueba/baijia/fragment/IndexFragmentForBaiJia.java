package com.shenma.yueba.baijia.fragment;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.shenma.yueba.R;
import com.shenma.yueba.application.MyApplication;
import com.shenma.yueba.baijia.activity.ChooseCityActivity;
import com.shenma.yueba.baijia.activity.SearchProductActivity;
import com.shenma.yueba.baijia.modle.CityInfoBackBean;
import com.shenma.yueba.baijia.modle.CityListItembean;
import com.shenma.yueba.baijia.modle.FragmentBean;
import com.shenma.yueba.baijia.view.BaseView;
import com.shenma.yueba.baijia.view.BuyerStreetView;
import com.shenma.yueba.baijia.view.MyBuyerView;
import com.shenma.yueba.baijia.view.TabViewpagerManager;
import com.shenma.yueba.constants.Constants;
import com.shenma.yueba.inter.CityChangeRefreshInter;
import com.shenma.yueba.inter.LocationBackListner;
import com.shenma.yueba.util.HttpControl;
import com.shenma.yueba.util.LocationUtil;
import com.shenma.yueba.util.SharedUtil;
import com.shenma.yueba.util.ToolsUtil;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/******
 * @author gyj
 * @date 2015-05-19 买手主页Fragment 主要布局采用viewPager+Linerlayout实现TAB效果切换数据
 ****/
public class IndexFragmentForBaiJia extends Fragment implements CityChangeRefreshInter {

    // 存储切换的数据
    List<FragmentBean> fragment_list = new ArrayList<FragmentBean>();
    // 存储Tab切换的视图对象
    List<View> footer_list = new ArrayList<View>();
    ViewPager baijia_fragment_tab1_pagerview;
    LinearLayout baijia_fragment_tab1_head_linearlayout;
    BuyerStreetView buyerStreetView;
    FragmentManager fragmentManager;
    View v;
    TabViewpagerManager tabViewpagerManager;
    TextView tv_city;//当前城市
    @Override
    public void onAttach(Activity activity) {
        // TODO Auto-generated method stub
        super.onAttach(activity);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if (v == null) {
            v = inflater.inflate(R.layout.indexfragmentforbaijia_layout, null);
            initView(v);
            //baijia_fragment_tab1_pagerview.setOffscreenPageLimit(0);
        }
        ViewGroup vp = (ViewGroup) v.getParent();
        if (vp != null) {
            vp.removeView(v);
        }
        return v;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        MyApplication.getInstance().getCityChangeRefreshService().addToList(this);
        super.onCreate(savedInstanceState);
    }

    /***
     * 初始化视图
     ***/
    void initView(View v) {
        fragmentManager = ((FragmentActivity) getActivity()).getSupportFragmentManager();
        buyerStreetView = new BuyerStreetView(getActivity());
        MyBuyerView myBuyerView = new MyBuyerView(getActivity());

        if (fragment_list != null) {
            fragment_list.clear();
        }
        fragment_list.add(new FragmentBean("买手街", -1, buyerStreetView));
        // fragment_list.add(new FragmentBean("TA们说", -1, theySayFragment));
        fragment_list.add(new FragmentBean("我的买手", -1, myBuyerView));
        baijia_fragment_tab1_head_linearlayout = (LinearLayout) v.findViewById(R.id.baijia_fragment_tab1_head_linearlayout);
        tv_city = new TextView(getActivity());
        final ProgressBar progressBar = new ProgressBar(getActivity());
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.CENTER_IN_PARENT);
        tv_city.setLayoutParams(params);
        progressBar.setLayoutParams(params);
        Resources res = getResources();
        Drawable myImage = res.getDrawable(R.drawable.arrow_down);
        myImage.setBounds(0, 0, myImage.getMinimumWidth(), myImage.getMinimumHeight());
        tv_city.setText("北京");
        tv_city.setPadding(ToolsUtil.dip2px(getActivity(), 10), 0, 0, 0);
        tv_city.setCompoundDrawablePadding(10);
        tv_city.setTextColor(getResources().getColor(R.color.gray));
        tv_city.setCompoundDrawables(null, null, myImage, null);
        progressBar.setPadding(ToolsUtil.dip2px(getActivity(), 10), 0, 0, 0);


        tv_city.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ChooseCityActivity.class);
                startActivityForResult(intent, Constants.REQUESTCODE);
                getActivity().overridePendingTransition(R.anim.enter_from_bottom, R.anim.no);

            }
        });

        RelativeLayout LeftParents = new RelativeLayout(getActivity());
        RelativeLayout.LayoutParams RelativeParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.MATCH_PARENT);
        LeftParents.setLayoutParams(RelativeParams);
        LeftParents.addView(tv_city);
        LeftParents.addView(progressBar);
        tv_city.setVisibility(View.INVISIBLE);
        LocationUtil locationUtil = new LocationUtil(getActivity());
        locationUtil.getLocation(new LocationBackListner() {
            @Override
            public void callBack(boolean result) {
                if (result) {
                    Toast.makeText(getActivity(), "定位成功", Toast.LENGTH_SHORT).show();
                    //开始调用接口，根据经纬度获取城市名称
                    HttpControl httpControl=new HttpControl();
                    httpControl.getCityInfoById(new HttpControl.HttpCallBackInterface() {
                        @Override
                        public void http_Success(Object obj) {
                            CityInfoBackBean back = (CityInfoBackBean) obj;
                            String str = back.getData().getName();
                            progressBar.setVisibility(View.GONE);
                            tv_city.setVisibility(View.VISIBLE);
                            tv_city.setText(str);
                            SharedUtil.setCurrentCityId(getActivity(), back.getData().getId());
                            buyerStreetView.requestFalshData();
                        }

                        @Override
                        public void http_Fails(int error, String msg) {
                            progressBar.setVisibility(View.GONE);
                            tv_city.setVisibility(View.VISIBLE);
                            tv_city.setText("全国");
                            SharedUtil.setCurrentCityId(getActivity(),"");
                        }
                    },getActivity(),SharedUtil.getStringPerfernece(getActivity(),Constants.LONGITUDE), SharedUtil.getStringPerfernece(getActivity(), Constants.LATITUDE));
                } else {
                    Toast.makeText(getActivity(), "定位失败", Toast.LENGTH_SHORT).show();
                    SharedUtil.setCurrentCityId(getActivity(), "");
                    tv_city.setText("全国");
                    progressBar.setVisibility(View.INVISIBLE);
                }
            }
        });
//        baijia_fragment_tab1_head_linearlayout.addView(RelativeParams);
//        baijia_fragment_tab1_head_linearlayout.addView(tv_city);
        baijia_fragment_tab1_head_linearlayout.addView(LeftParents);

        baijia_fragment_tab1_pagerview = (ViewPager) v.findViewById(R.id.baijia_fragment_tab1_pagerview);
        tabViewpagerManager = new TabViewpagerManager(getActivity(), fragment_list, baijia_fragment_tab1_head_linearlayout, baijia_fragment_tab1_pagerview);
        tabViewpagerManager.initViewPager();
        baijia_fragment_tab1_pagerview
                .setOnPageChangeListener(new OnPageChangeListener() {

                    @Override
                    public void onPageSelected(int arg0) {
                        //currid = arg0;
                        //setTextColor(arg0);
                        switch (arg0) {
                            case 0:
                                tabViewpagerManager.setCurrView(0);
                                ((BaseView) fragment_list.get(arg0).getFragment()).firstInitData();
                                break;
                            case 1:
                                if (!MyApplication.getInstance().isUserLogin(
                                        getActivity())) {
                                    baijia_fragment_tab1_pagerview.setCurrentItem(0, false);
                                } else {
                                    baijia_fragment_tab1_pagerview.setCurrentItem(1, true);
                                    tabViewpagerManager.setCurrView(1);

                                    ((BaseView) fragment_list.get(arg0).getFragment()).firstInitData();
                                }
                                break;
                        }
                    }

                    @Override
                    public void onPageScrolled(int arg0, float arg1, int arg2) {

                    }

                    @Override
                    public void onPageScrollStateChanged(int arg0) {

                    }
                });



        Button bt_right = new Button(getActivity());
        LinearLayout.LayoutParams paramsA = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        paramsA.height = ToolsUtil.dip2px(getActivity(), 16);
        paramsA.width = ToolsUtil.dip2px(getActivity(), 16);
        paramsA.gravity = Gravity.CENTER_VERTICAL;
        paramsA.setMargins(0, 0, ToolsUtil.dip2px(getActivity(), 10), 0);
        bt_right.setLayoutParams(paramsA);
        bt_right.setBackgroundResource(R.drawable.search);
        baijia_fragment_tab1_head_linearlayout.addView(bt_right);
        bt_right.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), SearchProductActivity.class);
                startActivity(intent);
            }
        });



        tabViewpagerManager.setCurrView(0);

    }

    @Override
    public void onDetach() {
        super.onDetach();
        try {
            Field childFragmentManager = Fragment.class.getDeclaredField("mChildFragmentManager");
            childFragmentManager.setAccessible(true);
            childFragmentManager.set(this, null);

        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public void refresh(CityListItembean bean) {
        tv_city.setText(bean.getName());
        buyerStreetView.requestFalshData();
    }
}
