package com.shenma.yueba.baijia.activity;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.shenma.yueba.R;
import com.shenma.yueba.application.MyApplication;
import com.shenma.yueba.baijia.modle.MyFavoriteProductListInfo;
import com.shenma.yueba.baijia.modle.MyFavoriteProductListPic;
import com.shenma.yueba.baijia.modle.RequestMyFavoriteProductListInfoBean;
import com.shenma.yueba.baijia.modle.newmodel.PubuliuBeanInfo;
import com.shenma.yueba.constants.Constants;
import com.shenma.yueba.util.CollectobserverManage;
import com.shenma.yueba.util.FontManager;
import com.shenma.yueba.util.HttpControl;
import com.shenma.yueba.util.HttpControl.HttpCallBackInterface;
import com.shenma.yueba.util.PubuliuManager;
import com.shenma.yueba.util.SharedUtil;
import com.shenma.yueba.util.ToolsUtil;
import com.umeng.socialize.utils.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * 我的收藏
 *
 * @author gyj
 * @version 创建时间：2015-5-20 上午11:14:57 程序的简单说明
 */


public class MyCollectionActivity extends BaseActivityWithTopView implements CollectobserverManage.ObserverListener {
    HttpControl httpCntrol = new HttpControl();
    int currPage = Constants.CURRPAGE_VALUE;
    int pageSize = Constants.PAGESIZE_VALUE;
    boolean showDialog = true;
    PullToRefreshScrollView shop_main_layout_title_pulltorefreshscrollview;
    LinearLayout baijia_market_main_layout_pubuliu_linearlayout;
    List<PubuliuBeanInfo> item = new ArrayList<PubuliuBeanInfo>();
    PubuliuManager pubuliuManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        MyApplication.getInstance().addActivity(this);
        requestWindowFeature(getWindow().FEATURE_NO_TITLE);
        setContentView(R.layout.shop_main_layoutbak);
        super.onCreate(savedInstanceState);
        initView();
        requestFalshData();
    }

    void initView() {
        setTitle("我的收藏");
        FontManager.changeFonts(MyCollectionActivity.this, tv_top_title);
        setLeftTextView(new OnClickListener() {

            @Override
            public void onClick(View v) {

                MyCollectionActivity.this.finish();
            }
        });

        setLeftTextView(new OnClickListener() {

            @Override
            public void onClick(View v) {
                MyCollectionActivity.this.finish();
            }
        });
        baijia_market_main_layout_pubuliu_linearlayout = (LinearLayout) findViewById(R.id.baijia_market_main_layout_pubuliu_linearlayout);

        shop_main_layout_title_pulltorefreshscrollview = (PullToRefreshScrollView) findViewById(R.id.shop_main_layout_title_pulltorefreshscrollview);
        ToolsUtil.initPullResfresh(shop_main_layout_title_pulltorefreshscrollview, MyCollectionActivity.this);
        shop_main_layout_title_pulltorefreshscrollview.setMode(Mode.PULL_FROM_START);
        shop_main_layout_title_pulltorefreshscrollview.setOnRefreshListener(new OnRefreshListener2() {

            @Override
            public void onPullDownToRefresh(PullToRefreshBase refreshView) {

                requestFalshData();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase refreshView) {

                requestData();
            }
        });

        pubuliuManager = new PubuliuManager(this, baijia_market_main_layout_pubuliu_linearlayout);
        //添加观察者
        CollectobserverManage.getInstance().addObserver(this);
    }

    void requestData() {
        // shop_main_layout_title_pulltorefreshscrollview.setRefreshing();
        sendHttp(currPage, 1);
    }

    void requestFalshData() {
        // shop_main_layout_title_pulltorefreshscrollview.setRefreshing();
        currPage = 1;
        sendHttp(1, 0);
    }

    /***
     * 加载数据获取我收藏的商品
     *
     * @param page int 访问页
     * @param type int 0: 刷新 1:加载更多
     **/
    void sendHttp(final int page, final int type) {
        ToolsUtil.showNoDataView(MyCollectionActivity.this, false);
        Log.i("TAG", "currpage=" + page + "   pagesize=" + pageSize);
        int userid = -1;
        if (SharedUtil.getStringPerfernece(this, SharedUtil.user_id) != null) {
            userid = Integer.parseInt(SharedUtil.getStringPerfernece(this, SharedUtil.user_id));
        }
        httpCntrol.getUserFavoriteProductList(userid, page, pageSize, showDialog, new HttpCallBackInterface() {

            @Override
            public void http_Success(Object obj) {
                currPage = page;
                ToolsUtil.pullResfresh(shop_main_layout_title_pulltorefreshscrollview);
                showDialog = false;
                if (obj != null && obj instanceof RequestMyFavoriteProductListInfoBean) {
                    RequestMyFavoriteProductListInfoBean bean = (RequestMyFavoriteProductListInfoBean) obj;
                    switch (type) {
                        case 0:
                            falshData(bean);
                            break;
                        case 1:
                            addData(bean);
                            break;
                    }
                    setPageStatus(bean, page);
                } else {

                    http_Fails(500, MyCollectionActivity.this.getResources().getString(R.string.errorpagedata_str));
                }

            }

            @Override
            public void http_Fails(int error, String msg) {
                ToolsUtil.pullResfresh(shop_main_layout_title_pulltorefreshscrollview);
                MyApplication.getInstance().showMessage(MyCollectionActivity.this, msg);
            }
        }, MyCollectionActivity.this);
    }


    /***
     * 刷新viewpager数据
     ***/
    void falshData(RequestMyFavoriteProductListInfoBean bean) {
        currPage++;
        if (bean != null && bean.getData() != null && bean.getData().getItems() != null) {
            item.clear();
            List<PubuliuBeanInfo> childitem = getTransformData(bean.getData().getItems());
            pubuliuManager.onResher(childitem);
            item.addAll(childitem);
        }

        showDialog = false;
    }


    /***
     * 加载数据
     **/
    void addData(RequestMyFavoriteProductListInfoBean bean) {
        currPage++;
        if (bean != null && bean.getData() != null && bean.getData().getItems() != null) {
            List<PubuliuBeanInfo> childitem = getTransformData(bean.getData().getItems());
            pubuliuManager.onaddData(childitem);
            item.addAll(childitem);
        }
    }


    /******
     * 数据进行转换
     *****/
    List<PubuliuBeanInfo> getTransformData(List<MyFavoriteProductListInfo> brandInfoInfos) {
        List<PubuliuBeanInfo> pubuliuBeanInfos = new ArrayList<PubuliuBeanInfo>();
        if (brandInfoInfos != null) {
            for (int i = 0; i < brandInfoInfos.size(); i++) {
                PubuliuBeanInfo pubuliuBeanInfo = new PubuliuBeanInfo();
                MyFavoriteProductListInfo storeIndexItem = brandInfoInfos.get(i);
                pubuliuBeanInfo.setFavoriteCount(pubuliuBeanInfo.getFavoriteCount());
                pubuliuBeanInfo.setId(Integer.toString(storeIndexItem.getId()));
                pubuliuBeanInfo.setIscollection(storeIndexItem.isFavorite());
                pubuliuBeanInfo.setName(storeIndexItem.getName());
                MyFavoriteProductListPic pic = storeIndexItem.getPic();
                if (pic != null) {
                    pubuliuBeanInfo.setPicurl(pic.getPic());
                    pubuliuBeanInfo.setRation(pic.getRatio());
                }
                pubuliuBeanInfo.setPrice(storeIndexItem.getPrice());
                pubuliuBeanInfos.add(pubuliuBeanInfo);
            }
        }
        return pubuliuBeanInfos;
    }

    void setPageStatus(RequestMyFavoriteProductListInfoBean data, int page) {
        if (page == 1 && (data.getData() == null
                || data.getData().getItems() == null || data
                .getData().getItems().size() == 0)) {
            if (shop_main_layout_title_pulltorefreshscrollview != null) {
                shop_main_layout_title_pulltorefreshscrollview.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
            }
            ToolsUtil.showNoDataView(MyCollectionActivity.this, true);
        } else if (page != 1
                && (data.getData() == null || data.getData().getItems() == null || data.getData().getItems().size() == 0)) {

            if (shop_main_layout_title_pulltorefreshscrollview != null) {
                shop_main_layout_title_pulltorefreshscrollview.setMode(PullToRefreshBase.Mode.BOTH);
            }

            MyApplication.getInstance().showMessage(MyCollectionActivity.this, MyCollectionActivity.this.getResources().getString(R.string.lastpagedata_str));
        } else {
            if (shop_main_layout_title_pulltorefreshscrollview != null) {
                shop_main_layout_title_pulltorefreshscrollview.setMode(PullToRefreshBase.Mode.BOTH);
            }
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        //移除观察者
        CollectobserverManage.getInstance().removeObserver(this);
    }

    @Override
    public synchronized void observerCallNotification(PubuliuBeanInfo pubuliuBeanInfo) {
        for(int i=0;i<item.size();i++)
        {
            if(item.get(i).getId().equals(pubuliuBeanInfo.getId()))
            {
                item.remove(i);
                if(pubuliuManager!=null)
                {
                    pubuliuManager.onResher(item);
                }
                break;
            }
        }
    }
}
