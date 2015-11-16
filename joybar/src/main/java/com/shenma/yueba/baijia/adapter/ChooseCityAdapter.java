package com.shenma.yueba.baijia.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.shenma.yueba.R;
import com.shenma.yueba.application.MyApplication;
import com.shenma.yueba.baijia.modle.CityListItembean;
import com.shenma.yueba.util.FontManager;

import java.util.List;

/**
 * Created by a on 2015/10/9.
 */
public class ChooseCityAdapter extends BaseAdapter {

    private Context ctx;
    private List<CityListItembean> cityList;


    public ChooseCityAdapter(Context ctx,List<CityListItembean> cityList){
        this.ctx = ctx;
        this.cityList = cityList;
    }
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
        View view = View.inflate(ctx, R.layout.choose_city_item,null);
        TextView tv_grey_title = (TextView)view.findViewById(R.id.tv_grey_title);
        TextView tv_city_name = (TextView) view.findViewById(R.id.tv_city_name);
        ImageView iv_arrow = (ImageView) view.findViewById(R.id.iv_arrow);
        View  line_top =   view.findViewById(R.id.line_top);
        View  line_bottom =   view.findViewById(R.id.line_bottom);
        iv_arrow.setVisibility(View.VISIBLE);
        if(position == 0){
            tv_grey_title.setVisibility(View.VISIBLE);
            tv_grey_title.setText("当前已开通SHOPPING的城市");
        }else{
            tv_grey_title.setVisibility(View.GONE);
        }
        line_bottom.setVisibility(View.VISIBLE);
        tv_city_name.setText(cityList.get(position).getName());
        tv_city_name.setTag(cityList.get(position));
        tv_city_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyApplication.getInstance().getCityChangeRefreshService().refreshList((CityListItembean)v.getTag());
                ((Activity) ctx).onBackPressed();
            }
        });
        FontManager.changeFonts(ctx, tv_city_name, tv_grey_title);
        return view;
    }
}