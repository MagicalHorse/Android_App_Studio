package com.shenma.yueba.baijia.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.shenma.yueba.R;
import com.shenma.yueba.application.MyApplication;
import com.shenma.yueba.baijia.adapter.AuthenticationMainAdapter;
import com.shenma.yueba.baijia.modle.newmodel.PubuliuBeanInfo;
import com.shenma.yueba.util.AbsBrandListManager;
import com.shenma.yueba.util.AutoBrandListManager;
import com.shenma.yueba.util.BrandListManager;
import com.shenma.yueba.util.PubuliuManager;
import com.shenma.yueba.util.ToolsUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2015/11/11.
 * 商场主页
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
    LinearLayout baijia_authencationmain_brand_linearlayout;
    AuthenticationMainAdapter authenticationMainAdapter;
    //品牌列表 管理
    AbsBrandListManager bm;
    //瀑布流管理
    PubuliuManager pubuliuManager;
    List<PubuliuBeanInfo> item;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);//必须在setContentView()上边
        setContentView(R.layout.baijia_market_main_layout);
        super.onCreate(savedInstanceState);
        item = new ArrayList<PubuliuBeanInfo>();
        initView();
        initPuBuLiu();
        initHeadView();
        setBrandData();
    }

    void initView() {

        setTitle("认证买手");

        setLeftTextView(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AuthenticationBuyerMainActivity.this.finish();
            }
        });

        setTopRightTextView("", new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        tv_top_right.setCompoundDrawablesWithIntrinsicBounds(null, null, getResources().getDrawable(R.drawable.search), null);

    }

    void initHeadView() {
        //地址图片
        ImageView baijia_marketmain_head_address_layout_imageview = (ImageView) findViewById(R.id.baijia_marketmain_head_address_layout_imageview);
        baijia_marketmain_head_address_layout_imageview.setVisibility(View.GONE);
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
        for (int i = 0; i < 10; i++) {
            item.add(new PubuliuBeanInfo());
        }
        pubuliuManager.onResher(item);
    }


    /***********
     * 设置品牌显示
     *********/
    void setBrandData() {
        List<String> list = new ArrayList<String>();
        for (int i = 0; i < 13; i++) {
            list.add("品牌" + i);
            //list.add("更多品牌");
        }
        bm = new AutoBrandListManager(this,baijia_authencationmain_brand_linearlayout);
        bm.setChildMargin(getResources().getDimensionPixelSize(R.dimen.branditem_margin));
        bm.setLastText("更多品牌", R.dimen.text_authentication_textsize);
        bm.settextSize(R.dimen.text_authentication_textsize);
        bm.setOnClickListener(new BrandListManager.OnBrandItemListener() {
            @Override
            public void onItemClick(View v, int i) {
                //MyApplication.getInstance().showMessage(MarketMainActivity.this, "数据" + i);
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
}