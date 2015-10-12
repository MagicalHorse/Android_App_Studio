package com.shenma.yueba.baijia.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.shenma.yueba.R;
import com.shenma.yueba.baijia.modle.ProductColorTypeBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2015/10/10.
 */
public class ProductColorTypeAdapter extends BaseAdapter {

    List<ProductColorTypeBean> bean=new ArrayList<ProductColorTypeBean>();
    Activity activity;

    public ProductColorTypeAdapter(Activity activity, List<ProductColorTypeBean> obj) {
        this.activity = activity;
        this.bean=obj;
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
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Holder holder;
        if(convertView==null)
        {
            holder=new Holder();
            convertView=LayoutInflater.from(activity).inflate(R.layout.productcolor_item,null);
            holder.iv=(ImageView)convertView.findViewById(R.id.productcolor_item_pic_imageview);
            holder.tv=(TextView)convertView.findViewById(R.id.productcolor_item_textview);
            convertView.setTag(holder);
        }else
        {
            holder=(Holder)convertView.getTag();
        }
        //赋值
        setValue(holder,position);
        return convertView;
    }


    void setValue(Holder holder,int position)
    {
        ProductColorTypeBean productColorTypeBean= bean.get(position);
        if(productColorTypeBean.isChecked())
        {
            holder.iv.setSelected(true);
            holder.tv.setTextColor(activity.getResources().getColor(R.color.red_text_color));
        }else
        {
            holder.iv.setSelected(false);
            holder.tv.setTextColor(activity.getResources().getColor(R.color.text_gray_color));
        }
    }

    class Holder
    {
        ImageView iv;//商品颜色图片
        TextView tv;//商品文字信息
    }
}