package com.shenma.yueba.baijia.activity;


import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;

import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.shenma.yueba.R;
import com.shenma.yueba.baijia.modle.newmodel.PubuliuBeanInfo;
import com.shenma.yueba.util.PubuliuManager;
import com.shenma.yueba.util.ToolsUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2015/11/16.
 * 品牌列表
 */
public class BrandListActivity extends BaseActivityWithTopView {
    String brandName = "品牌名称";
    //瀑布流管理
    PubuliuManager pubuliuManager;
    List<PubuliuBeanInfo> item=new ArrayList<PubuliuBeanInfo>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.brand_list_layout);
        super.onCreate(savedInstanceState);
        initView();
    }

    void initView() {
        PullToRefreshScrollView brand_list_layout_pulltorefreshscrollview=(PullToRefreshScrollView)findViewById(R.id.brand_list_layout_pulltorefreshscrollview);
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
        for (int i = 0; i < 10; i++) {
            item.add(new PubuliuBeanInfo());
        }
        pubuliuManager.onResher(item);
    }
}

