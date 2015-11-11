package com.shenma.yueba.adapter.baijia;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.shenma.yueba.R;
import com.shenma.yueba.models.Abs_HomeItemInfo;
import com.shenma.yueba.models.HomeItem_1_Info;
import com.shenma.yueba.models.HomeItem_2_Info;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by Administrator on 2015/11/6.
 * 主页 列表的 item
 */
public class HomeAdapter extends BaseAdapter {
    Context context;
    List<Abs_HomeItemInfo> infoList = new ArrayList<Abs_HomeItemInfo>();


    public HomeAdapter(Context context) {
        this.context = context;
        for(int i=0;i<10;i++)
        {
            Abs_HomeItemInfo abs_HomeItemInfo;

            int aaa=(int)(Math.random()*3);
            if(aaa%2==0)
            {
                abs_HomeItemInfo=new HomeItem_1_Info();
            }else
            {
                abs_HomeItemInfo=new HomeItem_2_Info();
            }

            infoList.add(abs_HomeItemInfo);
        }
    }

    @Override
    public int getCount() {
        return infoList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView=LayoutInflater.from(context).inflate(R.layout.home_item_layout, null);
        }
        Abs_HomeItemInfo abs_HomeItemInfo=infoList.get(position);
        abs_HomeItemInfo.setParetView(convertView,context);
        return convertView;
    }

}
