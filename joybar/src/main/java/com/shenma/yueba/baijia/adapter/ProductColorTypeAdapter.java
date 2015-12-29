package com.shenma.yueba.baijia.adapter;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.shenma.yueba.R;
import com.shenma.yueba.application.MyApplication;
import com.shenma.yueba.baijia.modle.ProductColorTypeBean;
import com.shenma.yueba.util.BitmapUtil;
import com.shenma.yueba.util.ToolsUtil;
import com.shenma.yueba.view.MyGridView;

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
        if(convertView==null && activity!=null)
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
        int item_width=calculateGirdViewwitdh();
        ProductColorTypeBean productColorTypeBean= bean.get(position);
        MyApplication.getInstance().getBitmapUtil().display(holder.iv,ToolsUtil.nullToString(productColorTypeBean.getPic()));
        //MyApplication.getInstance().getImageLoader().displayImage(ToolsUtil.nullToString(productColorTypeBean.getPic()), holder.iv, MyApplication.getInstance().getDisplayImageOptions());
        holder.tv.setText(ToolsUtil.nullToString(productColorTypeBean.getColorName()));
        if(item_width>0)
        {
           RelativeLayout.LayoutParams params=(RelativeLayout.LayoutParams)holder.iv.getLayoutParams();
            params.height=item_width;
            holder.iv.setLayoutParams(params);
        }

        if(productColorTypeBean.isChecked())
        {
            holder.iv.setSelected(true);
            if(activity!=null)
            {
                holder.tv.setTextColor(activity.getResources().getColor(R.color.red_text_color));
            }

        }else
        {
            holder.iv.setSelected(false);
            if(activity!=null)
            {
                holder.tv.setTextColor(activity.getResources().getColor(R.color.text_gray_color));
            }
        }
    }

    class Holder
    {
        ImageView iv;//商品颜色图片
        TextView tv;//商品文字信息
    }

    /*******
     * 计算 每一个item 的宽度
     * ***/
    int calculateGirdViewwitdh()
    {
        int item_width=0;
        if(activity!=null)
        {
            MyGridView  product_spec_layout_colortype_mygridview = (MyGridView)activity.findViewById(R.id.product_spec_layout_colortype_mygridview);
            if(product_spec_layout_colortype_mygridview!=null)
            {
                int mygridview_width=product_spec_layout_colortype_mygridview.getMeasuredWidth();
                item_width=mygridview_width/4;
                Log.i("TAG", "calculateGirdViewwitdh :" + item_width);
            }
        }
        return item_width;
    }
}
