package com.shenma.yueba.baijia.activity;

import android.os.Bundle;
import android.view.Window;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.shenma.yueba.R;
import com.shenma.yueba.baijia.adapter.BuyerAdapter;
import com.shenma.yueba.baijia.modle.HomeProductListInfoBean;
import com.shenma.yueba.baijia.modle.ProductListInfoBean;
import com.shenma.yueba.baijia.modle.ProductsInfoBean;
import com.shenma.yueba.baijia.modle.RequestProductListInfoBean;
import com.shenma.yueba.util.FontManager;
import com.shenma.yueba.util.HttpControl;
import com.shenma.yueba.util.SharedUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by a on 2015/10/15.
 */
public class SearchProductResultActivity extends BaseActivityWithTopView{

    private TextView tv_search_key;
    private PullToRefreshListView plv_products;
    private TextView tv_sales_volume;
    private TextView tv_price;
    private String key;
    BuyerAdapter buyerAdapter;
    private List<ProductsInfoBean> mList = new ArrayList<ProductsInfoBean>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.search_product_result_layout);
        super.onCreate(savedInstanceState);
        initView();
        getIntentData();
        buyerAdapter = new BuyerAdapter(mList,mContext);
        getProductListByKey();
    }

    private void initView() {
        setTitle("搜索");
      tv_search_key =   getView(R.id.tv_search_key);
      plv_products = getView(R.id.plv_products);
      tv_sales_volume = getView(R.id.tv_sales_volume);
      tv_price = getView(R.id.tv_price);
        FontManager.changeFonts(mContext, tv_search_key, tv_sales_volume, tv_price);
    }

    private void getProductListByKey(){
        HttpControl httpControl = new HttpControl();
        httpControl.getProductListByKey(new HttpControl.HttpCallBackInterface() {
            @Override
            public void http_Success(Object obj) {
                RequestProductListInfoBean bean = (RequestProductListInfoBean) obj;
                HomeProductListInfoBean infoBean = bean.getData();
                ProductListInfoBean productBean =  infoBean.getItems();
                mList.addAll(productBean.getProducts());
                buyerAdapter.notifyDataSetChanged();
            }

            @Override
            public void http_Fails(int error, String msg) {

            }
        }, mContext, key, SharedUtil.getCurrentCityId(mContext), "1", "0");
    }

    public void getIntentData() {
        key = getIntent().getStringExtra("key");
    }
}
