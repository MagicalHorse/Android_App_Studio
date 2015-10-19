package com.shenma.yueba.baijia.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.shenma.yueba.R;
import com.shenma.yueba.baijia.modle.ProductSPECbean;
import com.shenma.yueba.util.ToolsUtil;

import java.util.List;

/**
 * Created by Administrator on 2015/10/12.
 */
public class ProductSPECAdapter extends BaseAdapter {
    Activity activity;
    List<ProductSPECbean> bean;
    public ProductSPECAdapter(Activity activity,List<ProductSPECbean> bean)
    {
        this.activity=activity;
        this.bean=bean;
    }
    @Override
    public int getCount() {
        return bean.size();
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
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView==null)
        {
            convertView= LayoutInflater.from(activity).inflate(R.layout.productspectype_layout,null);
        }
        TextView tv=(TextView)convertView.findViewById(R.id.layout_spec_textview);
        ProductSPECbean speCbean=bean.get(position);
        tv.setText(ToolsUtil.nullToString(speCbean.getSizeName()));
        if(speCbean.ischecked())
        {
            tv.setSelected(true);
            tv.setTextColor(activity.getResources().getColor(R.color.white));
        }else
        {
            tv.setSelected(false);
            tv.setTextColor(activity.getResources().getColor(R.color.text_gray_color));
        }
        return convertView;
    }

}
