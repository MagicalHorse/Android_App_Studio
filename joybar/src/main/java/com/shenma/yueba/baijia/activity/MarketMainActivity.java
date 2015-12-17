package com.shenma.yueba.baijia.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.shenma.yueba.R;
import com.shenma.yueba.application.MyApplication;
import com.shenma.yueba.baijia.modle.BrandInfo;
import com.shenma.yueba.baijia.modle.MyFavoriteProductListInfo;
import com.shenma.yueba.baijia.modle.MyFavoriteProductListPic;
import com.shenma.yueba.baijia.modle.newmodel.PubuliuBeanInfo;
import com.shenma.yueba.baijia.modle.newmodel.StoreIndexBackBean;
import com.shenma.yueba.baijia.modle.newmodel.StoreIndexBean;
import com.shenma.yueba.constants.Constants;
import com.shenma.yueba.util.AbsBrandListManager;
import com.shenma.yueba.util.AutoBrandListManager;
import com.shenma.yueba.util.BrandListManager;
import com.shenma.yueba.util.CollectobserverManage;
import com.shenma.yueba.util.HttpControl;
import com.shenma.yueba.util.PubuliuManager;
import com.shenma.yueba.util.SharedUtil;
import com.shenma.yueba.util.ToolsUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2015/11/11.
 * 商场主页
 */
public class MarketMainActivity extends BaseActivityWithTopView {

    PullToRefreshListView baijia_authentication_listview;
    //图片
    ImageView baijia_marketmain_head_background_layout_imageview;
    //商店名称
    TextView baijia_marketmain_head_name_layout_textview;
    //地址
    TextView baijia_marketmain_head_address_layout_textview;
    //品牌父视图
    LinearLayout baijia_authencationmain_brand_linearlayout;
    ;
    //品牌列表 管理
    AbsBrandListManager bm;
    //瀑布流管理
    PubuliuManager pubuliuManager;
    String titlename = "";
    String StoreId = null;
    HttpControl httpControl = new HttpControl();
    String SortType = "5";
    int currPage = Constants.CURRPAGE_VALUE;
    int PageSize = Constants.PAGESIZE_VALUE;
    List<PubuliuBeanInfo> items_array = new ArrayList<PubuliuBeanInfo>();
    PullToRefreshScrollView baijia_market_main_layout_pullTorefreshscrollview;
    ImageView baijia_marketmain_head_address_layout_imageview;
    List<BrandInfo> brandInfos_array = new ArrayList<BrandInfo>();
    List<String> brandstr_array = new ArrayList<String>();
    boolean isShow = false;
    boolean isrunning = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);//必须在setContentView()上边
        setContentView(R.layout.baijia_market_main_layout);
        super.onCreate(savedInstanceState);
        StoreId = this.getIntent().getStringExtra("StoreId");
        if (StoreId == null || StoreId.equals("")) {
            MyApplication.getInstance().showMessage(MarketMainActivity.this, "数据错误");
            finish();
            return;
        }
        initView();
        initPuBuLiu();
        initHeadView();
        requestFalshData();
    }

    void initView() {
        baijia_market_main_layout_pullTorefreshscrollview = (PullToRefreshScrollView) findViewById(R.id.baijia_market_main_layout_pullTorefreshscrollview);
        baijia_market_main_layout_pullTorefreshscrollview.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2() {

            @Override
            public void onPullDownToRefresh(PullToRefreshBase refreshView) {

                requestFalshData();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase refreshView) {

                requestaddData();
            }
        });

        ToolsUtil.initPullResfresh(baijia_market_main_layout_pullTorefreshscrollview, this);

        setLeftTextView(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MarketMainActivity.this.finish();
            }
        });

        tv_top_right.setCompoundDrawablesWithIntrinsicBounds(null, null, getResources().getDrawable(R.drawable.search), null);
        setTopRightTextView("", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, SearchResultActivityForThreeTab.class);
                intent.putExtra("storeId", StoreId);
                startActivity(intent);
            }
        });

    }

    void initHeadView() {
        //地址图片
        baijia_marketmain_head_address_layout_imageview = (ImageView) findViewById(R.id.baijia_marketmain_head_address_layout_imageview);
        //图片
        baijia_marketmain_head_background_layout_imageview = (ImageView) findViewById(R.id.baijia_marketmain_head_background_layout_imageview);
        int width = ToolsUtil.getDisplayWidth(this);
        int height = width / 2;

        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) baijia_marketmain_head_background_layout_imageview.getLayoutParams();
        params.height = height;
        baijia_marketmain_head_background_layout_imageview.setLayoutParams(params);
        //商店名称
        baijia_marketmain_head_name_layout_textview = (TextView) findViewById(R.id.baijia_marketmain_head_name_layout_textview);
        //地址
        baijia_marketmain_head_address_layout_textview = (TextView) findViewById(R.id.baijia_marketmain_head_address_layout_textview);
        //品牌列表父视图
        baijia_authencationmain_brand_linearlayout = (LinearLayout) findViewById(R.id.baijia_authencationmain_brand_linearlayout);

    }

    void initPuBuLiu() {
        //瀑布流父类
        LinearLayout baijia_market_main_layout_pubuliu_linearlayout = (LinearLayout) findViewById(R.id.baijia_market_main_layout_pubuliu_linearlayout);
        pubuliuManager = new PubuliuManager(this, baijia_market_main_layout_pubuliu_linearlayout);
        CollectobserverManage.getInstance().addObserver(pubuliuManager);
    }


    /***********
     * 设置品牌显示
     *********/
    void setBrandData() {
        brandstr_array.clear();
        for (int i = 0; i < brandInfos_array.size(); i++) {
            brandstr_array.add(ToolsUtil.nullToString(brandInfos_array.get(i).getBrandName()));
        }
        bm = new AutoBrandListManager(this, baijia_authencationmain_brand_linearlayout);
        bm.setChildMargin(getResources().getDimensionPixelSize(R.dimen.branditem_margin));
        bm.setLastText("更多品牌", R.dimen.text_authentication_textsize);
        bm.settextSize(R.dimen.text_authentication_textsize);
        bm.setOnClickListener(new BrandListManager.OnBrandItemListener() {
            @Override
            public void onItemClick(View v, int i) {
                BrandInfo brandInfo = brandInfos_array.get(i);
                Intent intent = new Intent(MarketMainActivity.this, BrandListActivity.class);
                intent.putExtra("StoreId", StoreId);
                intent.putExtra("BrandName", brandInfo.getBrandName());
                intent.putExtra("BrandId", brandInfo.getBrandId());
                startActivity(intent);
            }

            @Override
            public void OnLastItemClick(View v) {
                Intent intent = new Intent(MarketMainActivity.this, SearchBrandListActivity.class);
                intent.putExtra("StoreId", StoreId);
                MarketMainActivity.this.startActivity(intent);
            }
        });
        if (brandstr_array.size() > 0) {
            baijia_authencationmain_brand_linearlayout.setVisibility(View.VISIBLE);
            bm.nofication(brandstr_array);
        } else {
            baijia_authencationmain_brand_linearlayout.setVisibility(View.GONE);
        }

    }

    /*********
     * @param currPage int 当前页
     * @param type     int 类型  0：刷新 1：加载
     ********/
    void requestData(final int currPage, final int type) {
        isrunning = true;
        String UserId = SharedUtil.getStringPerfernece(MarketMainActivity.this, SharedUtil.user_id);
        if (UserId == null || UserId.equals("")) {
            UserId = "0";
        }
        httpControl.getStoreIndex(isShow, StoreId, UserId, currPage, PageSize, SortType, new HttpControl.HttpCallBackInterface() {
            @Override
            public void http_Success(Object obj) {
                isShow = false;
                isrunning = false;
                StoreIndexBackBean storeIndexBackBean = (StoreIndexBackBean) obj;
                switch (type) {
                    case 0:
                        refreshData(storeIndexBackBean);
                        break;
                    case 1:
                        addData(storeIndexBackBean);
                        break;
                }
                ToolsUtil.pullResfresh(baijia_market_main_layout_pullTorefreshscrollview);
                setPageStatus(storeIndexBackBean, currPage);
            }

            @Override
            public void http_Fails(int error, String msg) {
                isrunning = false;
                MyApplication.getInstance().showMessage(MarketMainActivity.this, msg);
                ToolsUtil.pullResfresh(baijia_market_main_layout_pullTorefreshscrollview);
            }
        }, MarketMainActivity.this);
    }


    void setPageStatus(StoreIndexBackBean data, int page) {
        if (page == 1 && (data.getData() == null || data.getData().getItems() == null || data.getData().getItems().size() == 0)) {
            if (baijia_market_main_layout_pullTorefreshscrollview != null) {
                baijia_market_main_layout_pullTorefreshscrollview.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
            }

            ToolsUtil.showNoDataView(this, true);
        } else if (page != 1 && (data.getData() == null || data.getData().getItems() == null || data.getData().getItems().size() == 0)) {
            if (baijia_market_main_layout_pullTorefreshscrollview != null) {
                baijia_market_main_layout_pullTorefreshscrollview.setMode(PullToRefreshBase.Mode.BOTH);
            }

            MyApplication.getInstance().showMessage(this, this.getResources().getString(R.string.lastpagedata_str));
        } else {
            if (baijia_market_main_layout_pullTorefreshscrollview != null) {
                baijia_market_main_layout_pullTorefreshscrollview.setMode(PullToRefreshBase.Mode.BOTH);
            }
        }
    }


    /********
     * 设置门店信息
     *******/
    void setHeadDataValue(StoreIndexBean data) {
        if (data != null) {
            titlename = ToolsUtil.nullToString(data.getStoreName());
            setTitle(ToolsUtil.nullToString(titlename));
            MyApplication.getInstance().getImageLoader().displayImage(ToolsUtil.nullToString(data.getLogo()), baijia_marketmain_head_background_layout_imageview, MyApplication.getInstance().getDisplayImageOptions());
            baijia_marketmain_head_name_layout_textview.setText(ToolsUtil.nullToString(data.getStoreName()));
            //如果是认证买手
            if (data.getStoreLeave().equals("8")) {
                baijia_marketmain_head_address_layout_imageview.setVisibility(View.GONE);
                baijia_marketmain_head_address_layout_textview.setText(ToolsUtil.nullToString(data.getDescription()));
            } else {
                baijia_marketmain_head_address_layout_imageview.setVisibility(View.VISIBLE);
                String address=ToolsUtil.nullToString(data.getStoreLocal());
                if(address.equals(""))
                {
                    address="未知";
                }
                baijia_marketmain_head_address_layout_textview.setText(address);
            }
        }
        if (data != null && data.getBrands() != null && data.getBrands().size() > 0) {
            brandInfos_array = data.getBrands();
        }
        setBrandData();
    }


    void requestFalshData() {
        if (isrunning) {
            return;
        }
        isShow = true;
        requestData(1, 0);
    }

    void requestaddData() {
        if (isrunning) {
            return;
        }
        requestData(currPage, 1);
    }

    void refreshData(StoreIndexBackBean bean) {
        //赋值门店信息
        setHeadDataValue(bean.getData());
        currPage++;
        if(bean!=null && bean.getData()!=null && bean.getData().getItems()!=null)
        {
            items_array.clear();
            List<PubuliuBeanInfo> childitem=getTransformData(bean.getData().getItems());
            pubuliuManager.onResher(childitem);
            items_array.addAll(childitem);
        }
    }

    void addData(StoreIndexBackBean bean) {
        currPage++;
        if(bean!=null && bean.getData()!=null && bean.getData().getItems()!=null)
        {
            List<PubuliuBeanInfo> childitem=getTransformData(bean.getData().getItems());
            pubuliuManager.onaddData(childitem);
            items_array.addAll(childitem);
        }
    }


    /******
     * 数据进行转换
     * *****/
    List<PubuliuBeanInfo> getTransformData(List<MyFavoriteProductListInfo> brandInfoInfos)
    {
        List<PubuliuBeanInfo> pubuliuBeanInfos=new ArrayList<PubuliuBeanInfo>();
        if(brandInfoInfos!=null)
        {
            for(int i=0;i<brandInfoInfos.size();i++)
            {
                PubuliuBeanInfo pubuliuBeanInfo=new PubuliuBeanInfo();
                MyFavoriteProductListInfo storeIndexItem= brandInfoInfos.get(i);
                pubuliuBeanInfo.setFavoriteCount(pubuliuBeanInfo.getFavoriteCount());
                pubuliuBeanInfo.setId(Integer.toString(storeIndexItem.getId()));
                pubuliuBeanInfo.setIscollection(false);
                pubuliuBeanInfo.setName(storeIndexItem.getName());
                MyFavoriteProductListPic pic=storeIndexItem.getPic();
                if(pic!=null)
                {
                    pubuliuBeanInfo.setPicurl(pic.getPic());
                    pubuliuBeanInfo.setRation(pic.getRatio());
                }
                pubuliuBeanInfo.setPrice(storeIndexItem.getPrice());
                pubuliuBeanInfos.add(pubuliuBeanInfo);
            }
        }
        return pubuliuBeanInfos;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        CollectobserverManage.getInstance().removeObserver(pubuliuManager);
    }
}
