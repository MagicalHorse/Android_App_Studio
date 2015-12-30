package com.shenma.yueba.baijia.adapter;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.shenma.yueba.R;
import com.shenma.yueba.application.MyApplication;
import com.shenma.yueba.baijia.modle.BaijiaOrderDetailsInfo;
import com.shenma.yueba.baijia.modle.OrderPromotions;
import com.shenma.yueba.util.FontManager;
import com.shenma.yueba.util.ToolsUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * @author gyj
 * @version 创建时间：2015-6-1 上午11:22:39
 *          程序的简单说明   定义 订单确认 的商品列表适配器
 */

public class BaijiaOrderDetailsPromotionAdapter extends BaseAdapter {
    Context context;
    List<OrderPromotions> obj_list = new ArrayList<OrderPromotions>();

    public BaijiaOrderDetailsPromotionAdapter(Context context, List<OrderPromotions> obj_list) {
        this.context = context;
        this.obj_list = obj_list;
    }

    @Override
    public int getCount() {

        return obj_list.size();
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
        Holder holder;
        if (convertView == null) {
            holder = new Holder();
            convertView = (LinearLayout) LinearLayout.inflate(context, R.layout.promotion_item_layout, null);
            holder.baijia_orderdetails_promotionsname_textview = (TextView) convertView.findViewById(R.id.baijia_orderdetails_promotionsname_textview);
            holder.baijia_orderdetails_promotionsvalue_textview = (TextView) convertView.findViewById(R.id.baijia_orderdetails_promotionsvalue_textview);
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }
        setValue(holder, position);
        return convertView;
    }

    class Holder {
        TextView baijia_orderdetails_promotionsname_textview;//活动名称
        TextView baijia_orderdetails_promotionsvalue_textview;//活动金额
    }

    void setValue(Holder holder, int position) {
        OrderPromotions infobean = obj_list.get(position);
        holder.baijia_orderdetails_promotionsname_textview.setText(ToolsUtil.nullToString(infobean.getPromotionName()+"："));
        holder.baijia_orderdetails_promotionsvalue_textview.setText("立减 "+ToolsUtil.DounbleToString_2(infobean.getAmount()));
    }

}
