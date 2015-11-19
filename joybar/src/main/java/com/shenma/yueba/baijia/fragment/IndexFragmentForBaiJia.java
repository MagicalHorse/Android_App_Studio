package com.shenma.yueba.baijia.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.shenma.yueba.R;
import com.shenma.yueba.application.MyApplication;
import com.shenma.yueba.baijia.activity.ChooseCityActivity;
import com.shenma.yueba.baijia.activity.SearchProductActivity;
import com.shenma.yueba.baijia.adapter.HomeAdapter;
import com.shenma.yueba.baijia.modle.CityListItembean;
import com.shenma.yueba.baijia.modle.newmodel.Request_CityInfo;
import com.shenma.yueba.util.CityChangeRefreshObserver;
import com.shenma.yueba.util.LocationUtil;
import com.shenma.yueba.util.PerferneceUtil;
import com.shenma.yueba.util.TabViewPgerImageManager;
import com.shenma.yueba.view.RoundImageView;

import java.util.ArrayList;
import java.util.List;

import config.PerferneceConfig;
import https.CommonHttpControl;
import interfaces.CityChangeRefreshInter;
import interfaces.HttpCallBackInterface;
import interfaces.LocationBackListner;

/******
 * @author gyj
 * @date 2015-05-19 买手主页Fragment 主要布局采用viewPager+Linerlayout实现TAB效果切换数据
 ****/
public class IndexFragmentForBaiJia extends Fragment implements CityChangeRefreshInter {
    FragmentManager fragmentManager;
    TextView tv_city;//当前城市
    View parentView;
    PullToRefreshListView baijia_contact_listview;
    boolean isRunning = false;//访问网络是否进行中
    boolean isSuncess = false;//数据是否加载完成
    HomeAdapter homeAdapter;

    @Override
    public void onAttach(Activity activity) {
        // TODO Auto-generated method stub
        super.onAttach(activity);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            if (!isSuncess) {
                refreshDataByHttp();
            }

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        if (parentView == null) {
            parentView = inflater.inflate(R.layout.baijia_indexfragment_layout, null);
            initTitle(parentView);
            initView(parentView);
            initTabImage();
        }
        ViewGroup vp = (ViewGroup) parentView.getParent();
        if (vp != null) {
            vp.removeView(parentView);
        }
        return parentView;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        //MyApplication.getInstance().getCityChangeRefreshService().addToList(this);
        super.onCreate(savedInstanceState);
        fragmentManager = getActivity().getSupportFragmentManager();
    }


    /*********
     * 从网络加载数据
     *********/
    void refreshDataByHttp() {
        if (isRunning) {
            return;
        }
        isRunning = true;
        //模拟加载
        new Thread() {
            @Override
            public void run() {
                super.run();
                SystemClock.sleep(5000);
                isRunning = false;
                isSuncess = true;
            }
        }.start();
    }


    /*********
     * 初始化 标题
     ********/
    void initTitle(View v) {
        //左侧地址
        tv_city = (TextView) v.findViewById(R.id.tv_top_left);
        tv_city.setVisibility(View.VISIBLE);
        tv_city.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ChooseCityActivity.class);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.enter_from_bottom, R.anim.no);
            }
        });
        tv_city.setText("全国");
        tv_city.setCompoundDrawablesWithIntrinsicBounds(null, null, this.getResources().getDrawable(R.drawable.arrow_down), null);
        //标题
        TextView title_layout_titlename_textview = (TextView) v.findViewById(R.id.tv_top_title);
        title_layout_titlename_textview.setVisibility(View.VISIBLE);
        title_layout_titlename_textview.setText("打样购");
        //右侧搜索
        TextView title_layout_right_textview = (TextView) v.findViewById(R.id.bt_top_right);
        title_layout_right_textview.setBackgroundColor(getActivity().getResources().getColor(R.color.color_transparent));
        title_layout_right_textview.setVisibility(View.VISIBLE);
        title_layout_right_textview.setCompoundDrawablesWithIntrinsicBounds(null, null, this.getResources().getDrawable(R.drawable.search), null);
        title_layout_right_textview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(),SearchProductActivity.class);
                startActivity(intent);
            }
        });
    }


    /************************
     * 初始化图片循环滚动视图 并添加到当前页面中
     ************/
    void initTabImage() {
        List<String> array_str = new ArrayList<String>();
        for (int i = 0; i < 5; i++) {
            array_str.add("http://c.hiphotos.baidu.com/image/pic/item/b03533fa828ba61e5a8540284334970a304e594a.jpg");
        }
        TabViewPgerImageManager tabViewPgerImageManager = new TabViewPgerImageManager(getActivity(), array_str);
        //将 视图加入到 listview的 head中
        //LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT
        LinearLayout ll = new LinearLayout(getActivity());
        ll.removeAllViews();
        ll.setOrientation(LinearLayout.VERTICAL);
        ll.addView(tabViewPgerImageManager.getTabView());
        ll.addView(getScrollRoundView());
        baijia_contact_listview.getRefreshableView().addHeaderView(ll);
        //通知 数据更新 刷新视图
        tabViewPgerImageManager.notification();
    }


    /***
     * 初始化视图
     ***/
    void initView(View v) {
        homeAdapter = new HomeAdapter(getActivity());
        baijia_contact_listview = (PullToRefreshListView) parentView.findViewById(R.id.baijia_contact_listview);
        baijia_contact_listview.setAdapter(homeAdapter);
        CityChangeRefreshObserver.getInstance().addObserver(this);
        LocationUtil.getLocation(getActivity(), new LocationBackListner() {
            @Override
            public void callBack(boolean result) {
                if (result) {
                    Toast.makeText(getActivity(), "定位成功", Toast.LENGTH_SHORT).show();
                    //开始调用接口，根据经纬度获取城市名称
                    CommonHttpControl.getCityNameByGPS(new HttpCallBackInterface<Request_CityInfo>() {
                        @Override
                        public void http_Success(Request_CityInfo back) {
                            String str = back.getData().getName();
                            tv_city.setText(str);
                            PerferneceUtil.setString(PerferneceConfig.CURRENT_CITY_ID, back.getData().getId());
                        }

                        @Override
                        public void http_Fails(int error, String msg) {
                            tv_city.setText("全国");
                            PerferneceUtil.setString(PerferneceConfig.CURRENT_CITY_ID, "");
                        }
                    });
                } else {
                    Toast.makeText(getActivity(), "定位失败", Toast.LENGTH_SHORT).show();
                    PerferneceUtil.setString(PerferneceConfig.CURRENT_CITY_ID, "");
                    tv_city.setText("全国");
                }
            }
        });


    }

    @Override
    public void onDetach() {
        super.onDetach();
        /*try {
            Field childFragmentManager = Fragment.class.getDeclaredField("mChildFragmentManager");
            childFragmentManager.setAccessible(true);
            childFragmentManager.set(this, null);

        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }*/

    }

    /********
     * 城市选择回调刷新
     ****/
    @Override
    public void refresh(CityListItembean bean) {
        tv_city.setText(bean.getName());
        //判断是否需要刷新数据
        MyApplication.getInstance().showMessage(getActivity(), "获取到城市信息 需要判断当前的 城市与 选择是的 城市是否未同一个");
    }

    /********
     * 显示或隐藏  无数据 提示 视图
     *
     * @param status booelan true 显示  false隐藏
     *********/
    void showNoData(boolean status) {
        TextView nodata_layout_textview = (TextView) parentView.findViewById(R.id.nodata_layout_textview);
        if (status) {
            nodata_layout_textview.setVisibility(View.VISIBLE);
            baijia_contact_listview.setVisibility(View.GONE);
        } else {
            nodata_layout_textview.setVisibility(View.GONE);
            baijia_contact_listview.setVisibility(View.VISIBLE);
        }

    }

    /********
     * 生成 顶部 圆形  滚动 视图
     * **********/
    View getScrollRoundView() {
        List round_array = new ArrayList();
        for (int i = 0; i < 10; i++) {
            round_array.add(null);
        }
        HorizontalScrollView horizontalScrollView = new HorizontalScrollView(getActivity());
        horizontalScrollView.setVerticalScrollBarEnabled(false);
        horizontalScrollView.setHorizontalScrollBarEnabled(false);
        horizontalScrollView.setPadding(0,20,0,20);
        LinearLayout ll = new LinearLayout(getActivity());
        HorizontalScrollView.LayoutParams params = new HorizontalScrollView.LayoutParams(HorizontalScrollView.LayoutParams.MATCH_PARENT, HorizontalScrollView.LayoutParams.WRAP_CONTENT);
        horizontalScrollView.addView(ll, params);
        for (int i = 0; i < round_array.size(); i++) {
            RoundImageView riv = new RoundImageView(getActivity());
            riv.setImageResource(R.drawable.default_pic);
            int width=getActivity().getResources().getDimensionPixelOffset(R.dimen.dimen_scrollwidth);
            LinearLayout.LayoutParams chidparam = new LinearLayout.LayoutParams(width, width);
            chidparam.leftMargin = 3;
            ll.addView(riv, chidparam);
            riv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MyApplication.getInstance().showMessage(getActivity(),"点击事件");
                }
            });
        }
        return horizontalScrollView;
    }


}
