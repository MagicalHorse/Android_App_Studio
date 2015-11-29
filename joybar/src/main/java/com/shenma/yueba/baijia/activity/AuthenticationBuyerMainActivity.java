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
import com.shenma.yueba.baijia.modle.RequestMyCircleInfoBean;
import com.shenma.yueba.baijia.modle.newmodel.PubuliuBeanInfo;
import com.shenma.yueba.baijia.modle.newmodel.StoreIndexBackBean;
import com.shenma.yueba.baijia.modle.newmodel.StoreIndexBean;
import com.shenma.yueba.baijia.modle.newmodel.StoreIndexItem;
import com.shenma.yueba.constants.Constants;
import com.shenma.yueba.util.AbsBrandListManager;
import com.shenma.yueba.util.AutoBrandListManager;
import com.shenma.yueba.util.BrandListManager;
import com.shenma.yueba.util.HttpControl;
import com.shenma.yueba.util.PubuliuManager;
import com.shenma.yueba.util.SharedUtil;
import com.shenma.yueba.util.ToolsUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2015/11/11.
 * 专柜主页
 */
public class AuthenticationBuyerMainActivity extends BaseActivityWithTopView {
    //最大显示品牌个数
    int maxBrandCount = 6;
    PullToRefreshListView baijia_authentication_listview;
    //图片
    ImageView baijia_marketmain_head_background_layout_imageview;
    //商店名称
    TextView baijia_marketmain_head_name_layout_textview;
    //地址
    TextView baijia_marketmain_head_address_layout_textview;
    //品牌父视图
    LinearLayout baijia_authencationmain_brand_linearlayout;;
    //品牌列表 管理
    AbsBrandListManager bm;
    //瀑布流管理
    PubuliuManager pubuliuManager;
    String titlename = "XX商场";
    String StoreId=null;
    HttpControl httpControl=new HttpControl();
    String SortType="5";
    int currPage= Constants.CURRPAGE_VALUE;
    int PageSize=Constants.PAGESIZE_VALUE;
    List<PubuliuBeanInfo> items_array=new ArrayList<PubuliuBeanInfo>();
    PullToRefreshScrollView baijia_market_main_layout_pullTorefreshscrollview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);//必须在setContentView()上边
        setContentView(R.layout.baijia_market_main_layout);
        super.onCreate(savedInstanceState);
        StoreId=this.getIntent().getStringExtra("StoreId");
        if(StoreId==null|| StoreId.equals(""))
        {
            MyApplication.getInstance().showMessage(AuthenticationBuyerMainActivity.this, "数据错误");
            finish();
            return;
        }
        initView();
        initPuBuLiu();
        initHeadView();
        setBrandData();
        requestData();
    }

    void initView() {
        setTitle(ToolsUtil.nullToString(titlename));

        baijia_market_main_layout_pullTorefreshscrollview=(PullToRefreshScrollView)findViewById(R.id.baijia_market_main_layout_pullTorefreshscrollview);
        baijia_market_main_layout_pullTorefreshscrollview.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2() {

            @Override
            public void onPullDownToRefresh(PullToRefreshBase refreshView) {

                requestFalshData();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase refreshView) {

                requestData();
            }
        });

        ToolsUtil.initPullResfresh(baijia_market_main_layout_pullTorefreshscrollview, this);

        setLeftTextView(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AuthenticationBuyerMainActivity.this.finish();
            }
        });

        tv_top_right.setCompoundDrawablesWithIntrinsicBounds(null, null, getResources().getDrawable(R.drawable.search), null);
        setTopRightTextView("", new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }

    void initHeadView() {
        //地址图片
        ImageView baijia_marketmain_head_address_layout_imageview = (ImageView) findViewById(R.id.baijia_marketmain_head_address_layout_imageview);
        baijia_marketmain_head_address_layout_imageview.setVisibility(View.VISIBLE);
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
    }


    /***********
     * 设置品牌显示
     *********/
    void setBrandData() {
        List<String> list = new ArrayList<String>();
        for (int i = 0; i < 13; i++) {
            list.add("品牌" + i);
        }

        bm = new AutoBrandListManager(this,baijia_authencationmain_brand_linearlayout);
        bm.setChildMargin(getResources().getDimensionPixelSize(R.dimen.branditem_margin));
        bm.setLastText("更多品牌", R.dimen.text_authentication_textsize);
        bm.settextSize(R.dimen.text_authentication_textsize);
        bm.setOnClickListener(new BrandListManager.OnBrandItemListener() {
            @Override
            public void onItemClick(View v, int i) {
                //MyApplication.getInstance().showMessage(AuthenticationBuyerMainActivity.this, "数据" + i);
                Intent intent = new Intent(AuthenticationBuyerMainActivity.this, BrandListActivity.class);
                startActivity(intent);
            }

            @Override
            public void OnLastItemClick(View v) {
                Intent intent=new Intent(AuthenticationBuyerMainActivity.this, SearchBrandListActivity.class);
                AuthenticationBuyerMainActivity.this.startActivity(intent);
                MyApplication.getInstance().showMessage(AuthenticationBuyerMainActivity.this, "更多别点击了");
            }
        });
        bm.nofication(list);
    }

    /*********
     *@param  currPage int 当前页
     *@param  type int 类型  0：刷新 1：加载
     * ********/
    void requestData(final int currPage, final int type)
    {
        String UserId= SharedUtil.getStringPerfernece(AuthenticationBuyerMainActivity.this, SharedUtil.user_id);
        if(UserId==null || UserId.equals(""))
        {
            UserId="0";
        }
        httpControl.getStoreIndex(StoreId, UserId, currPage, PageSize, SortType, new HttpControl.HttpCallBackInterface() {
            @Override
            public void http_Success(Object obj) {
                StoreIndexBackBean storeIndexBackBean = (StoreIndexBackBean) obj;
                switch (type) {
                    case 0:
                        refreshData(storeIndexBackBean.getData());
                        break;
                    case 1:
                        addData(storeIndexBackBean.getData());
                        break;
                }
                ToolsUtil.pullResfresh(baijia_market_main_layout_pullTorefreshscrollview);
                setPageStatus(storeIndexBackBean, currPage);
            }

            @Override
            public void http_Fails(int error, String msg) {
                MyApplication.getInstance().showMessage(AuthenticationBuyerMainActivity.this, msg);
            }
        }, AuthenticationBuyerMainActivity.this);
    }


    void setPageStatus(StoreIndexBackBean data, int page) {
        if (page == 1 && (data.getData()==null || data.getData().getItems() == null || data.getData().getItems().size()==0)) {
            if(baijia_market_main_layout_pullTorefreshscrollview!=null)
            {
                baijia_market_main_layout_pullTorefreshscrollview.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
            }

            ToolsUtil.showNoDataView(this,true);
        } else if (page != 1 && (data.getData()==null || data.getData().getItems()== null || data.getData().getItems().size() == 0)) {
            if(baijia_market_main_layout_pullTorefreshscrollview!=null)
            {
                baijia_market_main_layout_pullTorefreshscrollview.setMode(PullToRefreshBase.Mode.BOTH);
            }

            MyApplication.getInstance().showMessage(this, this.getResources().getString(R.string.lastpagedata_str));
        }else
        {
            if(baijia_market_main_layout_pullTorefreshscrollview!=null)
            {
                baijia_market_main_layout_pullTorefreshscrollview.setMode(PullToRefreshBase.Mode.BOTH);
            }
        }
    }


    void requestFalshData()
    {
        requestData(1, 0);
    }

    void requestData()
    {
        requestData(currPage,1);
    }

    void refreshData(StoreIndexBean data)
    {
        currPage++;
        items_array.clear();
        transformData(data);
        pubuliuManager.onResher(items_array);
    }

    void addData(StoreIndexBean data)
    {
        currPage++;
        transformData(data);
        pubuliuManager.onaddData(items_array);
    }

    void transformData(StoreIndexBean data)
    {
        if(data!=null && data.getItems()!=null)
        {
            for(int i=0;i<data.getItems().size();i++)
            {
                StoreIndexItem storeIndexItem= data.getItems().get(i);
                PubuliuBeanInfo pubuliuBeanInfo=new PubuliuBeanInfo();
                pubuliuBeanInfo.setFavoriteCount(Integer.valueOf(storeIndexItem.getFavoriteCount()));
                pubuliuBeanInfo.setPrice(Double.parseDouble(storeIndexItem.getPrice()));
                pubuliuBeanInfo.setIscollection(storeIndexItem.isFavorite());
                pubuliuBeanInfo.setRation(storeIndexItem.getRatio());
                pubuliuBeanInfo.setName(ToolsUtil.nullToString(storeIndexItem.getProductName()));
                pubuliuBeanInfo.setPicurl(ToolsUtil.nullToString(storeIndexItem.getPic()));
                items_array.add(pubuliuBeanInfo);
            }

        }
    }
}
