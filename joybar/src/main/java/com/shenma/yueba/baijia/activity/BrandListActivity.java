package com.shenma.yueba.baijia.activity;


import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.shenma.yueba.R;
import com.shenma.yueba.application.MyApplication;
import com.shenma.yueba.baijia.modle.BrandInfoInfo;
import com.shenma.yueba.baijia.modle.RequestBrandInfoInfoBean;
import com.shenma.yueba.baijia.modle.newmodel.PubuliuBeanInfo;
import com.shenma.yueba.constants.Constants;
import com.shenma.yueba.util.CollectobserverManage;
import com.shenma.yueba.util.HttpControl;
import com.shenma.yueba.util.PubuliuManager;
import com.shenma.yueba.util.ToolsUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2015/11/16.
 * 品牌下的商品列表（以瀑布流形式显示 ）
 */
public class BrandListActivity extends BaseActivityWithTopView {
    String brandName = "品牌名称";
    int brandId=-1;
    int currPage= Constants.CURRPAGE_VALUE;
    int pageSize=20;
    boolean showDialog=true;

    //瀑布流管理
    PubuliuManager pubuliuManager;
    List<PubuliuBeanInfo> item=new ArrayList<PubuliuBeanInfo>();
    Activity activity;
    HttpControl httpControl;
    PullToRefreshScrollView brand_list_layout_pulltorefreshscrollview;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.brand_list_layout);
        super.onCreate(savedInstanceState);
        activity=this;
        initView();
        httpControl=new HttpControl();
        brandName=this.getIntent().getStringExtra("BrandName");
        brandId=this.getIntent().getIntExtra("BrandId", -1);
        requestFalshData();
    }

    void initView() {
        brand_list_layout_pulltorefreshscrollview=(PullToRefreshScrollView)findViewById(R.id.brand_list_layout_pulltorefreshscrollview);
        brand_list_layout_pulltorefreshscrollview.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
        ToolsUtil.initPullResfresh(brand_list_layout_pulltorefreshscrollview, activity);
        brand_list_layout_pulltorefreshscrollview.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2() {

            @Override
            public void onPullDownToRefresh(PullToRefreshBase refreshView) {

                requestFalshData();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase refreshView) {
                Log.i("TAG", "onPullUpToRefresh");
                requestData();
            }
        });

        setTitle(ToolsUtil.nullToString(brandName));
        setLeftTextView(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BrandListActivity.this.finish();
            }
        });

        //瀑布流的父类
        LinearLayout brand_list_layout_pubuliy_linearlayout = (LinearLayout) findViewById(R.id.brand_list_layout_pubuliy_linearlayout);
        pubuliuManager = new PubuliuManager(this, brand_list_layout_pubuliy_linearlayout);
        //添加观察者
        CollectobserverManage.getInstance().addObserver(pubuliuManager);
    }


    void requestData() {
        sendHttp(currPage, 1);

    }

    void requestFalshData() {
        showDialog = true;
        sendHttp(1, 0);
    }


    /***
     * 刷新viewpager数据
     * ***/
    void falshData(RequestBrandInfoInfoBean bean) {
        currPage++;
        if(bean!=null && bean.getData()!=null && bean.getData().getItems()!=null)
        {
            item.clear();
            List<PubuliuBeanInfo> childitem=getTransformData(bean.getData().getItems());
            pubuliuManager.onResher(childitem);
            item.addAll(childitem);
        }

        showDialog=false;
    }


    /***
     * 加载数据
     * **/
    void addData(RequestBrandInfoInfoBean bean) {
        currPage++;
        if(bean!=null && bean.getData()!=null && bean.getData().getItems()!=null)
        {
            List<PubuliuBeanInfo> childitem=getTransformData(bean.getData().getItems());
            pubuliuManager.onaddData(childitem);
            item.addAll(getTransformData(bean.getData().getItems()));
        }
    }



    /*****
     * 请求数据
     * @param page int 当前页
     * ***/
    void sendHttp(final int page,final int type)
    {
        ToolsUtil.showNoDataView(activity,false);
        httpControl.getBaijiaBrandDetails(page, pageSize, brandId, null, showDialog, new HttpControl.HttpCallBackInterface() {

            @Override
            public void http_Success(Object obj) {
                ToolsUtil.pullResfresh(brand_list_layout_pulltorefreshscrollview);
                currPage = page;
                showDialog = false;
                if (obj != null) {
                    RequestBrandInfoInfoBean bean = (RequestBrandInfoInfoBean) obj;
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
                    http_Fails(500, activity.getResources().getString(R.string.errorpagedata_str));
                }
            }

            @Override
            public void http_Fails(int error, String msg) {
                MyApplication.getInstance().showMessage(activity, msg);
                ToolsUtil.pullResfresh(brand_list_layout_pulltorefreshscrollview);
            }
        }, activity);
    }


    void setPageStatus(RequestBrandInfoInfoBean data, int page) {
        if (page == 1 && (data.getData() == null
                || data.getData().getItems() == null || data
                .getData().getItems().size() == 0)) {
            if(brand_list_layout_pulltorefreshscrollview!=null)
            {
                brand_list_layout_pulltorefreshscrollview.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
            }
            ToolsUtil.showNoDataView(activity,true);
        } else if (page != 1
                && (data.getData() == null || data.getData().getItems()==null || data.getData().getItems().size() == 0)) {

            if(brand_list_layout_pulltorefreshscrollview!=null)
            {
                brand_list_layout_pulltorefreshscrollview.setMode(PullToRefreshBase.Mode.BOTH);
            }

            MyApplication.getInstance().showMessage(
                    activity, activity.getResources().getString(R.string.lastpagedata_str));
        } else {
            if(brand_list_layout_pulltorefreshscrollview!=null)
            {
                brand_list_layout_pulltorefreshscrollview.setMode(PullToRefreshBase.Mode.BOTH);
            }
        }
    }


    /******
     * 数据进行转换
     * *****/
    List<PubuliuBeanInfo> getTransformData(List<BrandInfoInfo> brandInfoInfos)
    {
        List<PubuliuBeanInfo> pubuliuBeanInfos=new ArrayList<PubuliuBeanInfo>();
        if(brandInfoInfos!=null)
        {
            for(int i=0;i<brandInfoInfos.size();i++)
            {
                PubuliuBeanInfo pubuliuBeanInfo=new PubuliuBeanInfo();
                BrandInfoInfo brandInfoInfo= brandInfoInfos.get(i);
                pubuliuBeanInfo.setFavoriteCount(0);
                pubuliuBeanInfo.setId(Integer.toString(brandInfoInfo.getProductId()));
                pubuliuBeanInfo.setIscollection(false);
                pubuliuBeanInfo.setName(brandInfoInfo.getProductName());
                pubuliuBeanInfo.setPicurl(brandInfoInfo.getPic());
                pubuliuBeanInfo.setPrice(0.00);
                pubuliuBeanInfo.setRation(1);

                pubuliuBeanInfos.add(pubuliuBeanInfo);
            }
        }
        return pubuliuBeanInfos;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //移除观察者
        CollectobserverManage.getInstance().removeObserver(pubuliuManager);
    }
}
