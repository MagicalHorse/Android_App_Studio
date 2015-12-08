package com.shenma.yueba.baijia.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.shenma.yueba.R;
import com.shenma.yueba.application.MyApplication;
import com.shenma.yueba.baijia.activity.ChooseCityActivity;
import com.shenma.yueba.baijia.activity.SearchProductActivity;
import com.shenma.yueba.baijia.activity.WebActivity;
import com.shenma.yueba.baijia.adapter.HomeAdapter;
import com.shenma.yueba.baijia.modle.CityInfoBackBean;
import com.shenma.yueba.baijia.modle.CityListItembean;
import com.shenma.yueba.baijia.modle.newmodel.BannerBean;
import com.shenma.yueba.baijia.modle.newmodel.BinnerBackBean;
import com.shenma.yueba.baijia.modle.newmodel.IndexBackBean;
import com.shenma.yueba.baijia.modle.newmodel.IndexItems;
import com.shenma.yueba.baijia.modle.newmodel.SubjectrBean;
import com.shenma.yueba.constants.Constants;
import com.shenma.yueba.util.CityChangeRefreshObserver;
import com.shenma.yueba.util.CustomProgressDialog;
import com.shenma.yueba.util.HttpControl;
import com.shenma.yueba.util.LocationUtil;
import com.shenma.yueba.util.PerferneceUtil;
import com.shenma.yueba.util.TabViewPgerImageManager;
import com.shenma.yueba.util.ToolsUtil;
import com.shenma.yueba.view.RoundImageView;

import java.util.ArrayList;
import java.util.List;

import config.PerferneceConfig;
import interfaces.CityChangeRefreshInter;
import interfaces.LocationBackListner;

/******
 * @author gyj
 * @date 2015-05-19 买手主页Fragment 主要布局采用viewPager+Linerlayout实现TAB效果切换数据
 ****/
public class IndexFragmentForBaiJia extends Fragment implements CityChangeRefreshInter {
    FragmentManager fragmentManager;
    TextView tv_city;//当前城市
    String selectCityId = "0";//选择的城市id 默认0 全国
    View parentView;
    PullToRefreshListView baijia_contact_listview;
    boolean isRunning = false;//访问网络是否进行中
    boolean isSuncess = false;//数据是否加载完成
    HomeAdapter homeAdapter;
    List<String> array_str = new ArrayList<String>();
    TabViewPgerImageManager tabViewPgerImageManager;
    //首页顶部 列表
    List<BannerBean> bannerBeans_array = new ArrayList<BannerBean>();
    List<SubjectrBean> subjectrBean_array = new ArrayList<SubjectrBean>();

    BinnerBackBean binnerBackBean;//顶部 滑动图片 及 滑动 原图 数据
    HorizontalScrollView horizontalScrollView;
    int currPage = Constants.CURRPAGE_VALUE;
    int pageSize = Constants.PAGESIZE_VALUE;
    CustomProgressDialog customProgressDialog;
    //门店列表数据
    List<IndexItems> indexItemses_list = new ArrayList<IndexItems>();
    //是否所以数据加载完毕
    boolean isAllLoadSucess = false;
    LinearLayout head_ll;
    HttpControl httpControl = new HttpControl();

    @Override
    public void onAttach(Activity activity) {
        // TODO Auto-generated method stub
        super.onAttach(activity);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        if (parentView == null) {
            parentView = inflater.inflate(R.layout.baijia_indexfragment_layout, null);
            customProgressDialog = CustomProgressDialog.createDialog(getActivity());
            initTitle(parentView);
            initView(parentView);

        }
        ViewGroup vp = (ViewGroup) parentView.getParent();
        if (vp != null) {
            vp.removeView(parentView);
        }
        return parentView;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        MyApplication.getInstance().getCityChangeRefreshService().addToList(this);
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
        customProgressDialog.show();
        baijia_contact_listview.setRefreshing();
        requestBannerData();
        requestRefreshData();
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
        tv_city.setCompoundDrawablePadding(ToolsUtil.dip2px(getActivity(), 10));
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
                Intent intent = new Intent(getActivity(), SearchProductActivity.class);
                startActivity(intent);
            }
        });
    }


    /************************
     * 初始化图片循环滚动视图 并添加到当前页面中
     ************/
    void initTabImage() {
        if (head_ll == null) {
            head_ll = new LinearLayout(getActivity());
            baijia_contact_listview.getRefreshableView().addHeaderView(head_ll);
        }

        head_ll.removeAllViews();
        head_ll.setOrientation(LinearLayout.VERTICAL);
        //加载tab 图片
        if (array_str.size() > 0) {
            if (tabViewPgerImageManager == null) {
                tabViewPgerImageManager = new TabViewPgerImageManager(getActivity(), array_str);
                tabViewPgerImageManager.setTabViewPagerImageOnClickListener(new TabViewPgerImageManager.TabViewPagerImageOnClickListener() {
                    @Override
                    public void onClick_TabViewPagerImage(int i) {
                        if (i < bannerBeans_array.size()) {
                            Intent intent = new Intent(getActivity(), WebActivity.class);
                            intent.putExtra("url", ToolsUtil.nullToString(bannerBeans_array.get(i).getLink()));
                            startActivity(intent);
                        }
                    }
                });
            }
            if (tabViewPgerImageManager != null) {
                //通知 数据更新 刷新视图
                tabViewPgerImageManager.notification();
            }
        }

        if (tabViewPgerImageManager != null) {
            head_ll.addView(tabViewPgerImageManager.getTabView());
        }

        //加载主题
        if (horizontalScrollView == null) {
            horizontalScrollView = new HorizontalScrollView(getActivity());
            horizontalScrollView.setBackgroundColor(getActivity().getResources().getColor(R.color.white));
            horizontalScrollView.setVerticalScrollBarEnabled(false);
            horizontalScrollView.setHorizontalScrollBarEnabled(false);
            horizontalScrollView.setPadding(0, 20, 0, 20);
        }
        head_ll.addView(horizontalScrollView);
        horizontalScrollView.removeAllViews();

        getScrollRoundView();

    }


    /***
     * 初始化视图
     ***/
    void initView(View v) {
        homeAdapter = new HomeAdapter(getActivity(), indexItemses_list);
        baijia_contact_listview = (PullToRefreshListView) parentView.findViewById(R.id.baijia_contact_listview);
        baijia_contact_listview.getRefreshableView().setItemsCanFocus(true);
        baijia_contact_listview.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2() {

            @Override
            public void onPullDownToRefresh(PullToRefreshBase refreshView) {
                if (tabViewPgerImageManager != null) {
                    tabViewPgerImageManager.stopTimerToViewPager();
                }

                refreshDataByHttp();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase refreshView) {
                //requestAddData();
            }
        });
        baijia_contact_listview.setOnLastItemVisibleListener(new PullToRefreshBase.OnLastItemVisibleListener() {
            @Override
            public void onLastItemVisible() {
                if (!isAllLoadSucess && !isRunning && !baijia_contact_listview.isRefreshing()) {
                    requestAddData();
                } else {
                    if (isAllLoadSucess) {
                        MyApplication.getInstance().showMessage(getActivity(), getActivity().getResources().getString(R.string.lastpagedata_str));
                    }
                }
            }
        });

        baijia_contact_listview.setAdapter(homeAdapter);
        CityChangeRefreshObserver.getInstance().addObserver(this);
        LocationUtil.getLocation(getActivity(), new LocationBackListner() {
            @Override
            public void callBack(boolean result) {
                if (result) {
                    customProgressDialog.show();
                    //开始调用接口，根据经纬度获取城市名称
                    String longitude = PerferneceUtil.getString(PerferneceConfig.LONGITUDE);
                    String latitude = PerferneceUtil.getString(PerferneceConfig.LATITUDE);
                    httpControl.getCityInfoById(new HttpControl.HttpCallBackInterface() {
                        @Override
                        public void http_Success(Object obj) {
                            CityInfoBackBean back = (CityInfoBackBean) obj;
                            customProgressDialog.cancel();
                            String str = back.getData().getName();
                            tv_city.setText(str);
                            Toast.makeText(getActivity(), "定位成功,当前城市--" + str, Toast.LENGTH_SHORT).show();
                            selectCityId = back.getData().getId();
                            PerferneceUtil.setString(PerferneceConfig.CURRENT_CITY_ID, back.getData().getId());
                            PerferneceUtil.setString(PerferneceConfig.SELECTED_CITY_ID, back.getData().getId());
                            refreshDataByHttp();
                        }

                        @Override
                        public void http_Fails(int error, String msg) {
                            customProgressDialog.cancel();
                            tv_city.setText("全国");
                            selectCityId = "0";
                            PerferneceUtil.setString(PerferneceConfig.CURRENT_CITY_ID, "");
                            PerferneceUtil.setString(PerferneceConfig.SELECTED_CITY_ID, "");
                            refreshDataByHttp();
                        }
                    }, getActivity(), longitude, latitude);

                } else {
                    Toast.makeText(getActivity(), "定位失败", Toast.LENGTH_SHORT).show();
                    PerferneceUtil.setString(PerferneceConfig.CURRENT_CITY_ID, "");
                    PerferneceUtil.setString(PerferneceConfig.SELECTED_CITY_ID, "");
                    tv_city.setText("全国");
                    selectCityId = "0";
                    refreshDataByHttp();
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
        if (!selectCityId.equals(bean.getId())) {
            selectCityId = bean.getId();
            PerferneceUtil.setString(PerferneceConfig.SELECTED_CITY_ID, selectCityId);
            //刷新数据
            refreshDataByHttp();
        }
    }


    /********
     * 生成 顶部 圆形  滚动 视图
     **********/
    void getScrollRoundView() {

        if (binnerBackBean.getData() != null) {
            if (binnerBackBean.getData().getBanners() != null && binnerBackBean.getData().getSubjects().size() > 0) {
                subjectrBean_array = binnerBackBean.getData().getSubjects();
            }
        }


        LinearLayout ll = new LinearLayout(getActivity());
        HorizontalScrollView.LayoutParams params = new HorizontalScrollView.LayoutParams(HorizontalScrollView.LayoutParams.MATCH_PARENT, HorizontalScrollView.LayoutParams.WRAP_CONTENT);
        horizontalScrollView.addView(ll, params);
        for (int i = 0; i < subjectrBean_array.size(); i++) {
            RoundImageView riv = new RoundImageView(getActivity());
            riv.setTag(subjectrBean_array.get(i));
            riv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SubjectrBean subjectrBean = (SubjectrBean) v.getTag();
                    MyApplication.getInstance().showMessage(getActivity(), "点击操作");
                }
            });
            MyApplication.getInstance().getImageLoader().displayImage(ToolsUtil.nullToString(subjectrBean_array.get(i).getPic()), riv, MyApplication.getInstance().getDisplayImageOptions());
            int width = getActivity().getResources().getDimensionPixelOffset(R.dimen.dimen_scrollwidth);
            LinearLayout.LayoutParams chidparam = new LinearLayout.LayoutParams(width, width);
            chidparam.leftMargin = 3;
            ll.addView(riv, chidparam);
        }
    }


    /*********
     * 请求首页Banner数据
     ********/
    void requestBannerData() {
        httpControl.getBinner(new HttpControl.HttpCallBackInterface() {
            @Override
            public void http_Success(Object obj) {
                isRunning = false;
                binnerBackBean = (BinnerBackBean) obj;
                setHeaderBannerDataValue();
            }

            @Override
            public void http_Fails(int error, String msg) {

            }
        }, getActivity());
    }


    /**********
     * 刷新首页门店列表
     *******/
    void requestRefreshData() {
        if (isRunning) {
            return;
        }
        isRunning = true;
        isAllLoadSucess = false;
        currPage = Constants.CURRPAGE_VALUE;
        requestIndexData(1, 0);
    }

    /**********
     * 加载更多首页门店列表
     *******/
    void requestAddData() {
        if (isRunning) {
            return;
        }
        requestIndexData(currPage, 1);
    }


    /***********
     * 请求首页门店列表
     *
     * @param currPage int 当前访问的页
     * @param type     int 0：刷新  1：加载
     ************/
    void requestIndexData(final int currPage, final int type) {
        ToolsUtil.showNoDataView(getActivity(), parentView, false);
        httpControl.getIndex(selectCityId, currPage, pageSize, new HttpControl.HttpCallBackInterface() {
            @Override
            public void http_Success(Object obj) {
                isSuncess = true;
                IndexBackBean bean = (IndexBackBean) obj;
                switch (type) {
                    case 0:
                        falshData(bean);
                        break;
                    case 1:
                        addData(bean);
                        break;

                }
                setPageStatus(bean, currPage);
                customProgressDialog.cancel();
                ToolsUtil.pullResfresh(baijia_contact_listview);
                isRunning = false;
            }

            @Override
            public void http_Fails(int error, String msg) {
                MyApplication.getInstance().showMessage(getActivity(), msg);
                ToolsUtil.pullResfresh(baijia_contact_listview);
                customProgressDialog.cancel();
                isRunning = false;
            }
        }, getActivity());
    }


    void setPageStatus(IndexBackBean data, int page) {
        if (page == 1 && (data.getData() == null || data.getData().getItems() == null || data.getData().getItems().size() == 0)) {
            if (baijia_contact_listview != null) {
                baijia_contact_listview.setMode(PullToRefreshBase.Mode.PULL_FROM_START);

            }
            isAllLoadSucess = true;
            ToolsUtil.showNoDataView(getActivity(), parentView, true);
        } else if (page != 1 && (data.getData() == null || data.getData().getItems() == null || data.getData().getItems().size() == 0)) {
            if (baijia_contact_listview != null) {
                baijia_contact_listview.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
            }
            isAllLoadSucess = true;
            MyApplication.getInstance().showMessage(getActivity(), getActivity().getResources().getString(R.string.lastpagedata_str));
        } else {
            if (baijia_contact_listview != null) {
                baijia_contact_listview.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
            }
        }
    }

    void addData(IndexBackBean bean) {
        currPage++;
        if (bean.getData() != null) {
            if (bean.getData().getItems() != null) {
                indexItemses_list.addAll(bean.getData().getItems());
            }
        }

        if (homeAdapter != null) {
            homeAdapter.notifyDataSetChanged();
        }
    }

    void falshData(IndexBackBean bean) {
        currPage++;
        indexItemses_list.clear();
        if (bean.getData() != null) {
            if (bean.getData().getItems() != null) {
                indexItemses_list.addAll(bean.getData().getItems());
            }
        }

        if (homeAdapter != null) {
            homeAdapter.notifyDataSetChanged();
        }
    }


    /**********
     * 设置banner数据信息
     *********/
    void setHeaderBannerDataValue() {
        if (binnerBackBean.getData() != null) {
            if (binnerBackBean.getData().getBanners() != null && binnerBackBean.getData().getBanners().size() > 0) {
                bannerBeans_array = binnerBackBean.getData().getBanners();
                array_str.clear();
                for (int i = 0; i < bannerBeans_array.size(); i++) {
                    array_str.add(ToolsUtil.nullToString(bannerBeans_array.get(i).getPic()));
                }
                initTabImage();
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        CityChangeRefreshObserver.getInstance().removeObserver(this);
    }
}
