package com.shenma.yueba.baijia.activity;

import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.shenma.yueba.R;
import com.shenma.yueba.application.MyApplication;
import com.shenma.yueba.baijia.adapter.ChooseCityAdapter;
import com.shenma.yueba.util.FontManager;

import java.util.ArrayList;

/**
 * Created by a on 2015/9/28.
 */
public class ChooseCityActivity extends BaseActivityWithTopView {
    private ListView lv_city;
    private ArrayList<String> cityList = new ArrayList<String>();
    private View headerView;
    private View footerView;
    private ChooseCityAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.choose_city_layout);
        super.onCreate(savedInstanceState);
        MyApplication.getInstance().addActivity(this);
        initView();
    }

    private void initView() {
        setTitle("选择城市");
        setLeftTextView(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChooseCityActivity.this.finish();
                overridePendingTransition(0, R.anim.out_from_bottom);
            }
        });
        adapter = new ChooseCityAdapter(mContext,cityList);
        lv_city = (ListView) findViewById(R.id.lv_city);
        cityList.add("北京");
        cityList.add("上海");
        cityList.add("南京");
        lv_city.setAdapter(adapter);
        headerView = View.inflate(mContext,R.layout.choose_city_item,null);
        TextView tv_city_name =   (TextView)headerView.findViewById(R.id.tv_city_name);
        TextView tv_grey_title = (TextView) headerView.findViewById(R.id.tv_grey_title);
        tv_grey_title.setText("当前定位城市");
        ImageView iv_address = (ImageView) headerView.findViewById(R.id.iv_address);
        iv_address.setVisibility(View.VISIBLE);
        View  line_top =   headerView.findViewById(R.id.line_top);
        View  line_bottom =   headerView.findViewById(R.id.line_bottom);
        line_top.setVisibility(View.VISIBLE);
        line_bottom.setVisibility(View.VISIBLE);
        footerView = View.inflate(mContext,R.layout.choose_city_footer,null);
        TextView tv_more_city = (TextView) footerView.findViewById(R.id.tv_more_city);
        lv_city.addHeaderView(headerView);
        lv_city.addFooterView(footerView);
        FontManager.changeFonts(mContext, tv_more_city, tv_grey_title,tv_top_title,tv_city_name);
    }









    @Override
    protected void onDestroy() {
        super.onDestroy();
        MyApplication.getInstance().removeActivity(this);//加入回退栈
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(0,R.anim.out_from_bottom);
    }
}
