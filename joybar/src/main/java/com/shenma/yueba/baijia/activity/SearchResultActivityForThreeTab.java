package com.shenma.yueba.baijia.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.shenma.yueba.BaseFragmentActivity;
import com.shenma.yueba.R;
import com.shenma.yueba.application.MyApplication;
import com.shenma.yueba.baijia.fragment.BrandSearchFragment;
import com.shenma.yueba.baijia.fragment.BuyerSearchFragment;
import com.shenma.yueba.baijia.fragment.MarketSearchFragment;
import com.shenma.yueba.baijia.fragment.ProductSearchFragment;
import com.shenma.yueba.constants.Constants;
import com.shenma.yueba.util.FontManager;
import com.shenma.yueba.util.ToolsUtil;
import com.shenma.yueba.yangjia.fragment.HuoKuanIncomeAndOutGoingFragment;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.List;


/**
 * 货款收支
 *
 * @author a
 */

public class SearchResultActivityForThreeTab extends BaseFragmentActivity implements
        OnClickListener {
    private ProductSearchFragment productSearchFragment;//商品
    private BrandSearchFragment brandSearchFragment;//品牌
    private BuyerSearchFragment buyerSearchFragment;//买手
    private List<Fragment> fragmentList = new ArrayList<Fragment>();
    private EditText et_search;
    private ImageView iv_back, iv_search;
    private ImageView iv_cursor_left;
    private ImageView iv_cursor_center;
    private ImageView iv_cursor_right;
    private TextView tv_product;
    private TextView tv_brand;
    private TextView tv_buyer;
    private String key;
    private ViewPager search_result_viewpager;
    private ArrayList<ImageView> cursorImageList = new ArrayList<ImageView>();
    private ArrayList<TextView> titleTextList = new ArrayList<TextView>();
    private String storeId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        MyApplication.getInstance().addActivity(this);// 加入回退栈
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.search_result_layou_for_three_tab);
        super.onCreate(savedInstanceState);
        key = getIntent().getStringExtra("key");
        setFragmentList();
        initView();
        initViewPager();
        //fragmentList.get(0).getData(true,0,SearchResultActivity.this);
    }


    private void initView() {
        iv_back = (ImageView) findViewById(R.id.iv_back);
        iv_search = (ImageView) findViewById(R.id.iv_search);
        et_search = (EditText) findViewById(R.id.et_search);
        et_search.setText(ToolsUtil.nullToString(key));
        iv_back.setOnClickListener(this);
        iv_search.setOnClickListener(this);
        iv_cursor_left = (ImageView) findViewById(R.id.iv_cursor_left);
        iv_cursor_center = (ImageView) findViewById(R.id.iv_cursor_center);
        iv_cursor_right = (ImageView) findViewById(R.id.iv_cursor_right);
        cursorImageList.add(iv_cursor_left);
        cursorImageList.add(iv_cursor_center);
        cursorImageList.add(iv_cursor_right);
        iv_cursor_left.setVisibility(View.VISIBLE);
        tv_product = (TextView) findViewById(R.id.tv_product);
        tv_brand = (TextView) findViewById(R.id.tv_brand);
        tv_buyer = (TextView) findViewById(R.id.tv_buyer);
        tv_product.setTextSize(20);
        tv_product.setTextColor(getResources().getColor(R.color.main_color));
        titleTextList.add(tv_product);
        titleTextList.add(tv_brand);
        titleTextList.add(tv_buyer);
        tv_product.setOnClickListener(this);
        tv_brand.setOnClickListener(this);
        tv_buyer.setOnClickListener(this);
        search_result_viewpager = (ViewPager) findViewById(R.id.search_result_viewpager);
        productSearchFragment.getProductList(SearchResultActivityForThreeTab.this,true,false,key);
    }

    private void initViewPager() {
        search_result_viewpager.setAdapter(new TabPageIndicatorAdapter(
                getSupportFragmentManager()));
        search_result_viewpager.setCurrentItem(0);
        search_result_viewpager.setOnPageChangeListener(new OnPageChangeListener() {
            public void onPageScrolled(int arg0, float arg1, int arg2) {
            }

            /*
             * 页面跳转完成后调用的方法
             */
            public void onPageSelected(int arg0) {
                switch (arg0){
                    case 0:
                        productSearchFragment.getProductList(SearchResultActivityForThreeTab.this, true, false, ToolsUtil.nullToString(et_search.getText().toString().trim()));
                        break;
                    case 1:
                        brandSearchFragment.getBrand(SearchResultActivityForThreeTab.this, true, false, ToolsUtil.nullToString(et_search.getText().toString().trim()));
                        break;
                    case 2:
                        buyerSearchFragment.getSearchBuyerList(SearchResultActivityForThreeTab.this, storeId, true, false, ToolsUtil.nullToString(et_search.getText().toString().trim()));
                        break;

                }
                setCursorAndText(arg0, cursorImageList, titleTextList);

            }

            @Override
            public void onPageScrollStateChanged(int arg0) {
                // TODO Auto-generated method stub

            }
        });
    }

    /**
     * 初始化ItemFragment3
     */
    private void setFragmentList() {
        productSearchFragment = new ProductSearchFragment(key,storeId);
        brandSearchFragment = new BrandSearchFragment(key);
        buyerSearchFragment = new BuyerSearchFragment(key,storeId);
        fragmentList.add(productSearchFragment);
        fragmentList.add(brandSearchFragment);
        fragmentList.add(buyerSearchFragment);
    }

    /**
     * ViewPager适配器
     *
     * @author len
     */
    class TabPageIndicatorAdapter extends FragmentPagerAdapter {
        public TabPageIndicatorAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return fragmentList.get(position);
        }

        @Override
        public int getCount() {
            return fragmentList.size();
        }

        @Override
        public int getItemPosition(Object object) {
            return super.getItemPosition(object);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:// 返回
                SearchResultActivityForThreeTab.this.finish();
                break;
            case R.id.tv_product://商品
                search_result_viewpager.setCurrentItem(0);
                break;
            case R.id.tv_brand:// 品牌
                search_result_viewpager.setCurrentItem(1);
                break;
            case R.id.tv_buyer://买手
                search_result_viewpager.setCurrentItem(2);
                break;
            case R.id.tv_market:// 商场
                search_result_viewpager.setCurrentItem(3);
                break;
            case R.id.iv_search://查找
                int position = search_result_viewpager.getCurrentItem();
                switch (position){
                    case 0:
                        productSearchFragment.getProductList(SearchResultActivityForThreeTab.this, true, true, ToolsUtil.nullToString(et_search.getText().toString().trim()));
                        break;
                    case 1:
                        brandSearchFragment.getBrand(SearchResultActivityForThreeTab.this, true, true, ToolsUtil.nullToString(et_search.getText().toString().trim()));
                        break;
                    case 2:
                        buyerSearchFragment.getSearchBuyerList(SearchResultActivityForThreeTab.this, storeId, true, true, ToolsUtil.nullToString(et_search.getText().toString().trim()));
                        break;

                }
                break;
            default:
                break;
        }

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Constants.REQUESTCODE && resultCode == Constants.RESULTCODE) {//提现货款
//            fragmentList.get(0).reSetTv_bottom();//重置提现货款的底部按钮
//            fragmentList.get(0).getData(true, 0, SearchResultActivity.this);//重新刷新列表
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    @Override
    protected void onDestroy() {
        MyApplication.getInstance().removeActivity(this);//加入回退栈
        super.onDestroy();
    }


    public void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }
}
