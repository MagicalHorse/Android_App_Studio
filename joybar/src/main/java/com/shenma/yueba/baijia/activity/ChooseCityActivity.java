package com.shenma.yueba.baijia.activity;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.shenma.yueba.R;
import com.shenma.yueba.util.FontManager;

import java.util.ArrayList;
import java.util.List;

import im.control.TextViewManager;

/**
 * Created by a on 2015/9/28.
 */
public class ChooseCityActivity extends BaseActivityWithTopView {
    private ListView lv_city;
    private List<String> cityList = new ArrayList<String>();
    private View headerView;
    private View footerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.choose_city_layout);
        initView();
    }

    private void initView() {
        lv_city = (ListView) findViewById(R.id.lv_city);
        lv_city.setAdapter(new ChooseCityAdapter());

        headerView = View.inflate(mContext,R.layout.choose_city_item,null);
        TextView tv_grey_title = (TextView) headerView.findViewById(R.id.tv_grey_title);
        tv_grey_title.setText("当前定位城市");
        ImageView iv_address = (ImageView) headerView.findViewById(R.id.iv_address);
        iv_address.setVisibility(View.VISIBLE);
        footerView = View.inflate(mContext,R.layout.choose_city_footer,null);
        TextView tv_more_city = (TextView) footerView.findViewById(R.id.tv_more_city);
        lv_city.addHeaderView(headerView);
        lv_city.addFooterView(footerView);
        FontManager.changeFonts(mContext, tv_more_city, tv_grey_title);
    }






    class ChooseCityAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return cityList.size();
        }

        @Override
        public Object getItem(int position) {
            return cityList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = View.inflate(mContext,R.layout.choose_city_item,null);
            TextView tv_grey_title = (TextView)view.findViewById(R.id.tv_grey_title);
            TextView tv_city_name = (TextView) view.findViewById(R.id.tv_city_name);
            ImageView iv_arrow = (ImageView) view.findViewById(R.id.iv_arrow);
            iv_arrow.setVisibility(View.VISIBLE);
            if(position == 0){
                tv_grey_title.setVisibility(View.VISIBLE);
                tv_grey_title.setText("当前已开通SHOPPING的城市");
            }else{
                tv_grey_title.setVisibility(View.GONE);
            }
            tv_city_name.setText(cityList.get(position));
            return view;
        }
    }
}
