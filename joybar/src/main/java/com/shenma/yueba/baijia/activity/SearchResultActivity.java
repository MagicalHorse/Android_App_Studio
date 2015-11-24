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
import com.shenma.yueba.yangjia.fragment.HuoKuanIncomeAndOutGoingFragment;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.List;


/**
 * 货款收支
 *
 * @author a
 */

public class SearchResultActivity extends BaseFragmentActivity implements
        OnClickListener {
    private static final String[] TITLE = new String[]{"商品", "品牌", "买手",
            "商场"};
    private ProductSearchFragment productSearchFragment;//商品
    private BrandSearchFragment brandSearchFragment;//品牌
    private BuyerSearchFragment buyerSearchFragment;//买手
    private MarketSearchFragment marketSearchFragment;//商场
    private List<Fragment> fragmentList = new ArrayList<Fragment>();
    private EditText et_search;
    private ImageView iv_back, iv_search;
    private ImageView iv_cursor_left;
    private ImageView iv_cursor_left2;
    private ImageView iv_cursor_right2;
    private ImageView iv_cursor_right;
    private TextView tv_product;
    private TextView tv_brand;
    private TextView tv_buyer;
    private TextView tv_market;
    private ViewPager search_result_viewpager;
    private ArrayList<ImageView> cursorImageList = new ArrayList<ImageView>();
    private ArrayList<TextView> titleTextList = new ArrayList<TextView>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        MyApplication.getInstance().addActivity(this);// 加入回退栈
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.search_result_layout);
        super.onCreate(savedInstanceState);
        setFragmentList();
        initView();
        initViewPager();
        //fragmentList.get(0).getData(true,0,SearchResultActivity.this);
    }


    private void initView() {
        iv_back = (ImageView) findViewById(R.id.iv_back);
        iv_search = (ImageView) findViewById(R.id.iv_search);
        et_search = (EditText) findViewById(R.id.et_search);
        iv_back.setOnClickListener(this);
        iv_search.setOnClickListener(this);
        iv_cursor_left = (ImageView) findViewById(R.id.iv_cursor_left);
        iv_cursor_left2 = (ImageView) findViewById(R.id.iv_cursor_left2);
        iv_cursor_right2 = (ImageView) findViewById(R.id.iv_cursor_right2);
        iv_cursor_right = (ImageView) findViewById(R.id.iv_cursor_right);
        cursorImageList.add(iv_cursor_left);
        cursorImageList.add(iv_cursor_left2);
        cursorImageList.add(iv_cursor_right2);
        cursorImageList.add(iv_cursor_right);
        iv_cursor_left.setVisibility(View.VISIBLE);
        tv_product = (TextView) findViewById(R.id.tv_product);
        tv_brand = (TextView) findViewById(R.id.tv_brand);
        tv_buyer = (TextView) findViewById(R.id.tv_buyer);
        tv_market = (TextView) findViewById(R.id.tv_market);
        tv_product.setTextSize(20);
        tv_product.setTextColor(getResources().getColor(R.color.main_color));
        titleTextList.add(tv_product);
        titleTextList.add(tv_brand);
        titleTextList.add(tv_buyer);
        titleTextList.add(tv_market);
        tv_product.setOnClickListener(this);
        tv_brand.setOnClickListener(this);
        tv_buyer.setOnClickListener(this);
        tv_market.setOnClickListener(this);

        search_result_viewpager = (ViewPager) findViewById(R.id.search_result_viewpager);
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
//                fragmentList.get(arg0).getData(false, arg0, SearchResultActivity.this);
//                for (int i = 0; i < fragmentList.size(); i++) {
//                    if (arg0 != i) {
//                        fragmentList.get(arg0).tv_nodata.setVisibility(View.GONE);
//                    }
//                }
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
        productSearchFragment = new ProductSearchFragment();
        brandSearchFragment = new BrandSearchFragment();
        buyerSearchFragment = new BuyerSearchFragment();
        marketSearchFragment = new MarketSearchFragment();
        fragmentList.add(productSearchFragment);
        fragmentList.add(brandSearchFragment);
        fragmentList.add(buyerSearchFragment);
        fragmentList.add(marketSearchFragment);
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
                SearchResultActivity.this.finish();
                break;
            case R.id.tv_can_withdraw://可提现
                search_result_viewpager.setCurrentItem(0);
                break;
            case R.id.tv_freeze:// 已冻结
                search_result_viewpager.setCurrentItem(1);
                break;
            case R.id.tv_had_withdraw://已提现
                search_result_viewpager.setCurrentItem(2);
                break;
            case R.id.tv_back:// 售后服务
                search_result_viewpager.setCurrentItem(3);
                break;
            case R.id.iv_search://查找

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
