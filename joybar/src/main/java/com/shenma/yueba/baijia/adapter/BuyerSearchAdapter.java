package com.shenma.yueba.baijia.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.shenma.yueba.R;
import com.shenma.yueba.util.CreateAutoSizeViewManager;

import java.util.List;

/**
 * Created by Administrator on 2015/11/12.
 * 认证买手 列表 适配器
 */
public class BuyerSearchAdapter extends BaseAdapter {
    Activity activity;

    public BuyerSearchAdapter(Activity activity) {
        this.activity = activity;
    }

    @Override
    public int getCount() {
        return 10;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final Holder holder;
        if (convertView == null) {
            holder = new Holder();
            convertView = LayoutInflater.from(activity).inflate(R.layout.buyer_for_search_item, null);
            LinearLayout authentication_item_productlist_linearlayout = (LinearLayout) convertView.findViewById(R.id.authentication_item_productlist_linearlayout);
            int marginvalue = activity.getResources().getDimensionPixelSize(R.dimen.item_margin);
            holder.cvm = new CreateAutoSizeViewManager(activity, marginvalue, R.layout.authentication_chid_item_layout, 3, authentication_item_productlist_linearlayout, new CreateAutoSizeViewManager.InflaterSucessListener() {
                @Override
                public void returnChildListView(List<View> view_array) {
                    setValue(position, holder);
                }
            });

            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }

        setValue(position, holder);

        return convertView;
    }

    class Holder {
        CreateAutoSizeViewManager cvm;
    }

    void setValue(int position, Holder holder) {
        List<View> view_array = holder.cvm.getChildView();
        for (int i = 0; i < view_array.size(); i++) {
            ImageView iv = (ImageView) view_array.get(i).findViewById(R.id.authentication_child_iten_layout_pic_imageview);

        }
    }

}
